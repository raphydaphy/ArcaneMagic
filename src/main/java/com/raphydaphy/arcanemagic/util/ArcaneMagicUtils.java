package com.raphydaphy.arcanemagic.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    public static void drawSplitString(FontRenderer fontRenderer, String text, float x, float y, int wrap, int color)
    {
        List<String> strings = fontRenderer.listFormattedStringToWidth(text, wrap);

        for (String s : strings)
        {
            fontRenderer.func_211126_b(s, (x - fontRenderer.getStringWidth(s) / 2f), y, color);
            y += fontRenderer.FONT_HEIGHT;
        }
    }

    public static void drawCenteredSplitString(FontRenderer fontRenderer, String text, float x, float y, int wrap,
                                               int color)
    {
        List<String> strings = fontRenderer.listFormattedStringToWidth(text, wrap);

        y -= (strings.size() / 2f) * fontRenderer.FONT_HEIGHT;

        for (String s : strings)
        {
            fontRenderer.func_211126_b(s, (x - fontRenderer.getStringWidth(s) / 2f), y, color);
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
}
