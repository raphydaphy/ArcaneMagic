package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.api.IAnimaInductible;
import com.raphydaphy.arcanemagic.client.particle.ParticleAnimaEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.tileentity.TileEntityInductor;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Objects;

public class ItemLinkingRod extends Item
{
    private static final String LINKING_TAG = "is_linking";
    public static final String LINK_POS = "link_pos";

    public ItemLinkingRod()
    {
        super(new Builder().group(ItemGroup.MISC).maxStackSize(1));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return stack.hasTagCompound() && Objects.requireNonNull(stack.getTagCompound()).getBoolean(LINKING_TAG);
    }

    public void magicParticle(Color color, BlockPos from, BlockPos to)
    {
        World world = Minecraft.getMinecraft().world;

        if (world != null)
        {

            float magic = 0.01625f;

            float distX = from.getX() - to.getX();
            float vx = magic * distX;

            float distZ = from.getZ() - to.getZ();
            float vz = magic * distZ;

            float distY = from.getY() - to.getY();
            float vy = 0.053f + 0.017f * distY;

            int life = 111 + (0 * (int) Math.abs(distY));

            float alpha = Math.min(Math.max(world.rand.nextFloat(), 0.25f), 0.30f);

            int dist = Math.abs((int) distX) + Math.abs((int) distZ) + Math.abs((int) distZ);
            float size;

            if (dist < 4)
            {
                size = Math.min(Math.max(world.rand.nextFloat() * 6, 1.5f), 2);
            } else if (dist < 6)
            {
                size = Math.min(Math.max(world.rand.nextFloat() * 10, 2.5f), 3);
            } else
            {
                size = Math.min(Math.max(world.rand.nextFloat() * 14, 3f), 3.5f);
            }

            ParticleRenderer.particleCounter += world.rand.nextInt(3);
            if (ParticleRenderer.particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1
                    : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0)
            {
                Particle p = new ParticleAnimaEntity(world, to.getX() + .5f,
                        to.getY() + .8f, to.getZ() + 0.5f, vx, vy, vz, color.getRed() / 256f, color.getGreen() / 256f,
                        color.getBlue() / 256f, alpha, size, life, 0.1f);
                //Minecraft.getMinecraft().effectRenderer.addEffect(p);
                ParticleRenderer.getInstance().addParticle(p);
            }

        }
    }

    @Override
    public EnumActionResult func_195939_a(ItemUseContext context)
    {
        ItemStack stack = context.func_195996_i();
        EntityPlayer player = context.func_195999_j();
        World world = context.func_195991_k();
        BlockPos pos = context.func_195995_a();
        if (player == null || player.isSneaking())
        {
            if (world.isRemote)
            {
                magicParticle(Color.GREEN, pos.up(), pos.add(3, 3, 3));
                return EnumActionResult.SUCCESS;
            }
            if (stack.getTagCompound() == null)
            {
                stack.setTagCompound(new NBTTagCompound());
            }
            BlockPos link = getLinkPos(stack);
            if (link != null)
            {
                // Tried to link a block to itself
                if (link.equals(context.func_195995_a()))
                {
                    if (player != null)
                    {
                        player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.LINKING_SAME_BLOCK).setStyle(new Style()), true);
                    }

                    stack.getTagCompound().setBoolean(LINKING_TAG, false);

                    return EnumActionResult.SUCCESS;
                }
                TileEntity src = world.getTileEntity(link);
                TileEntity dest = world.getTileEntity(pos);

                // TODO: don't hardcode the inductor
                // Can it receive anima from a diffuser?
                if (dest instanceof TileEntityInductor)
                {
                    // Can the source block send anima?
                    if (src instanceof IAnimaInductible)
                    {
                        ((TileEntityInductor)dest).setLinkPos(link);
                        return endLink(stack, player, ArcaneMagicResources.LINKING_SUCCESS);
                    }
                    return endLink(stack, player, ArcaneMagicResources.LINKING_INVALID_SRC);
                }
                return endLink(stack, player, ArcaneMagicResources.LINKING_INVALID_DEST);
            }
            else
            {
                TileEntity src = world.getTileEntity(pos);
                if (src instanceof IAnimaInductible)
                {
                    stack.getTagCompound().setBoolean(LINKING_TAG, true);
                    stack.getTagCompound().setLong(LINK_POS, pos.toLong());
                    if (player != null)
                    {
                        player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.LINKING_SELECTED).setStyle(new Style()), true);
                        player.openContainer.detectAndSendChanges();
                    }
                }
                else if (player != null)
                {
                    player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.LINKING_INVALID_SRC).setStyle(new Style()), true);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    private EnumActionResult endLink(ItemStack stack, EntityPlayer player, String unlocMessage)
    {
        Objects.requireNonNull(stack.getTagCompound()).setBoolean(LINKING_TAG, false);
        if (player != null)
        {
            player.sendStatusMessage(new TextComponentTranslation(unlocMessage).setStyle(new Style()), true);
            player.openContainer.detectAndSendChanges();
        }
        return EnumActionResult.SUCCESS;
    }

    private BlockPos getLinkPos(ItemStack stack)
    {
        if (stack.getTagCompound() != null && stack.getTagCompound().getBoolean(LINKING_TAG))
        {
            return BlockPos.fromLong(stack.getTagCompound().getLong(LINK_POS));
        }
        return null;
    }
}
