package com.raphydaphy.arcanemagic.util;

import net.minecraft.client.gui.FontRenderer;

import java.util.List;

public class ArcaneMagicUtils
{
    public static float lerp(float a, float b, float t)
    {
        return (1 - t) * a + t * b;
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
}
