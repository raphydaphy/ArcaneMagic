package com.raphydaphy.arcanemagic.util;

import com.raphydaphy.arcanemagic.client.particle.ParticleAnimaEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class ArcaneMagicUtils
{
    public static float lerp(float a, float b, float t)
    {
        return (1 - t) * a + t * b;
    }

    public static Vector3f lerp(float x1, float y1, float z1, float x2, float y2, float z2, float alpha)
    {
        return new Vector3f(lerp(x1, x2, alpha), lerp(y1, y2, alpha), lerp(z1, z2, alpha));
    }

    public static void drawCenteredSplitString(FontRenderer fontRenderer, String text, float x, float y, int wrap,
                                               int color)
    {
        List<String> strings = fontRenderer.listFormattedStringToWidth(text, wrap);

        y -= (strings.size() / 2f) * fontRenderer.FONT_HEIGHT;

        for (String s : strings)
        {
            fontRenderer.func_211126_b(s, (x - fontRenderer.getStringWidth(s) / 2), y, color);
            y += fontRenderer.FONT_HEIGHT;
        }

    }

    public static void beforeReplacingTileEntity(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
            }
            world.removeTileEntity(pos);
        }
    }

    public static void magicParticle(Color color, BlockPos from, BlockPos to)
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

            int life = 111;

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

                Minecraft.getMinecraft().effectRenderer.addEffect(p);

                // TODO: custom particle renderer!!
                //ParticleRenderer.getInstance().addParticle(p);
            }

        }
    }
}
