package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.anima.AnimaReceiveMethod;
import com.raphydaphy.arcanemagic.network.PacketDeathParticles;
import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityDrowned;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
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
        for (int x = (int)this.posX - 1; x <= (int)this.posX + 1; x++)
        {
            for (int y = (int)this.posY - 2; y <= (int)this.posY; y++)
            {
                for (int z = (int)this.posZ - 1; z <= (int)this.posZ + 1; z++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() == ArcaneMagic.ALTAR)
                    {
                        System.out.println("hooray we fouhnd a rare edvicdence of human life it is known as the sacrificial altar!!1");
                        return pos;
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void dropLoot(boolean recentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropFewItems(recentlyHit, lootingModifier);

        if (!world.isRemote)
        {
            Entity trueSource = source.getTrueSource();
            if (trueSource instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)trueSource;

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

        Minecraft.getMinecraft().getConnection().sendPacket(new PacketDeathParticles(posX, posY, posZ, width, height, altar));
    }
}
