package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.anima.AnimaReceiveMethod;
import com.raphydaphy.arcanemagic.item.ItemWrittenParchment;
import com.raphydaphy.arcanemagic.network.PacketDeathParticles;
import com.raphydaphy.arcanemagic.parchment.IParchment;
import com.raphydaphy.arcanemagic.parchment.ParchmentDrownedDiscovery;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityDrowned;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityDrowned.class)
public abstract class MixinEntityDrowned extends EntityLivingBase
{
    protected MixinEntityDrowned(EntityType<?> type, World world)
    {
        super(type, world);
    }

    private BlockPos findAltar()
    {
        for (int x = (int)this.posX - 2; x <= (int)this.posX + 2; x++)
        {
            for (int y = (int)this.posY - 2; y <= (int)this.posY; y++)
            {
                for (int z = (int)this.posZ - 2; z <= (int)this.posZ + 2; z++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() == ArcaneMagic.ALTAR)
                    {
                        return pos;
                    }
                }
            }
        }

        return null;
    }

    @Override // TODO: move to forge death event
    public void onDeath(DamageSource source)
    {
        super.onDeath(source);
        if (!world.isRemote)
        {
            Entity trueSource = source.getTrueSource();
            if (trueSource instanceof EntityPlayer)
            {
                onDeathServer((EntityPlayer)trueSource);
                updateParchment((EntityPlayer)trueSource);
            }
        }
    }

    private void onDeathServer(EntityPlayer killer)
    {
        int killed = ((EntityPlayerMP)killer).getStatFile().readStat(StatList.KILLED.addStat(EntityType.DROWNED));
        if (killed == 1)
        {
            killer.sendMessage(new TextComponentTranslation(ArcaneMagicResources.DROWNED_FIRST_KILL).setStyle(new Style().setItalic(true)));
            return;
        }
        ItemStack paper = ItemStack.EMPTY;
        ItemStack parchment = ItemStack.EMPTY;

        int parchmentsMade = ((EntityPlayerMP)killer).getStatFile().readStat(StatList.CRAFTS_STATS.addStat(ArcaneMagic.PARCHMENT));
        for (ItemStack stack : killer.inventoryContainer.getInventory())
        {
            if (stack.getItem() == Items.PAPER)
            {
               paper = stack;
            }
            else if (stack.getItem() == ArcaneMagic.WRITTEN_PARCHMENT || stack.getItem() == ArcaneMagic.PARCHMENT)
            {
                parchment = stack;
                if (parchment.getItem() == ArcaneMagic.WRITTEN_PARCHMENT)
                {
                    break;
                }
            }
        }
        // TODO: use entitydata to check if they have killed drowned with paper already
        if (!paper.isEmpty() && parchment.isEmpty() && parchmentsMade == 0)
        {
            killer.sendMessage(new TextComponentTranslation(ArcaneMagicResources.DROWNED_PAPER_KILL).setStyle(new Style().setItalic(true)));
            paper.shrink(1);
            killer.openContainer.detectAndSendChanges();
        }
        else if (parchment.getItem() == ArcaneMagic.PARCHMENT)
        {
            killer.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.DROWNED_PARCHMENT_KILL).setStyle(new Style().setItalic(true)), true);
            parchment.shrink(1);
            ItemStack written_parchment = new ItemStack(ArcaneMagic.WRITTEN_PARCHMENT);
            written_parchment.setTagCompound(new NBTTagCompound());
            if (written_parchment.getTagCompound() != null)
            {
                written_parchment.getTagCompound().setString(ItemWrittenParchment.PARCHMENT_KEY, ArcaneMagicResources.DROWNED_DISCOVERY);
            }
            if (!killer.inventory.addItemStackToInventory(written_parchment))
            {
                InventoryHelper.spawnItemStack(world, killer.posX, killer.posY, killer.posZ, written_parchment);
            }
            killer.openContainer.detectAndSendChanges();
        }
    }

    private void updateParchment(EntityPlayer player)
    {
        for (ItemStack stack : player.inventoryContainer.getInventory())
        {
            if (stack.getItem() instanceof ItemWrittenParchment && stack.getTagCompound() != null)
            {
                IParchment parchment = ParchmentRegistry.getParchment(stack);
                if (parchment instanceof ParchmentDrownedDiscovery)
                {
                    int kills = stack.getTagCompound().getInteger(ParchmentDrownedDiscovery.DROWNED_KILLS);
                    if (kills < 4)
                    {
                        stack.getTagCompound().setInteger(ParchmentDrownedDiscovery.DROWNED_KILLS, kills + 1);
                        player.openContainer.detectAndSendChanges();

                        if (kills == 3)
                        {
                            player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.DROWNED_DISCOVERY_COMPLETE).setStyle(new Style().setItalic(true)), true);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void onDeathUpdate()
    {
        ++this.deathTime;
        if (this.deathTime == 20)
        {
            int count;
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
            {
                count = this.getExperiencePoints(this.attackingPlayer);

                while(count > 0)
                {
                    int lvt_2_1_ = EntityXPOrb.getXPSplit(count);
                    count -= lvt_2_1_;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, lvt_2_1_));
                }
            }

            BlockPos altar = findAltar();

            if (!world.isRemote)
            {
                deathPacket(altar);
                if (altar != null)
                {
                    TileEntity te = world.getTileEntity(altar);
                    if (te instanceof TileEntityAltar)
                    {
                        ((TileEntityAltar)te).receiveAnima(world.rand.nextInt(100) + 150, AnimaReceiveMethod.SPECIAL);
                    }
                }
            }

            this.setDead();
        }
    }

    private void deathPacket(BlockPos altar)
    {
        // TODO: this is VERY BAD

        if (getServer() != null)
        {
            for (EntityPlayerMP player : getServer().getPlayerList().getPlayers())
            {
                player.connection.sendPacket(new PacketDeathParticles(posX, posY, posZ, width, height, altar));
            }
        }
    }
}
