package com.raphydaphy.arcanemagic.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Vector3f;

import java.util.List;

public class ArcaneMagicUtils
{
    public static float lerp(float a, float b, float t)
    {
        return (1 - t) * a + t * b;
    }

    public static Vector3f add(float x1, float y1, float z1, Vector3f two)
    {
        // unmapped functions are getX, getY and getZ respectively
        return new Vector3f(x1 + two.func_195899_a(), y1 + two.func_195900_b(), z1 + two.func_195902_c());
    }

    public static Vector3f subtract(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        return new Vector3f(x1 - x2, y1 - y2, z1 - z2);
    }

    public static Vector3f divide(Vector3f a, float by)
    {
        // unmapped functions are getX, getY and getZ respectively
        return new Vector3f(a.func_195899_a() / by, a.func_195900_b() / by, a.func_195902_c() / by);
    }

    public static Vector3f multiply(Vector3f a, float by)
    {
        // unmapped functions are getX, getY and getZ respectively
        return new Vector3f(a.func_195899_a() * by, a.func_195900_b() * by, a.func_195902_c() * by);
    }

    public static float magnitude(Vector3f vec)
    {
        // unmapped functions are getX, getY and getZ respectively
        return (float)Math.sqrt(vec.func_195899_a() * vec.func_195899_a() + vec.func_195900_b() * vec.func_195900_b() + vec.func_195902_c() * vec.func_195902_c());
    }

    public static Vector3f moveTowards(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, float maxDistDelta)
    {
        Vector3f a = subtract(toX, toY, toZ, fromZ, fromY, fromZ);
        float magnitude = magnitude(a);
        if(magnitude <= maxDistDelta || magnitude == 0f)
        {
            return new Vector3f(toX, toY, toZ);
        }
        return add(fromX, fromY, fromZ, multiply(divide(a, magnitude), maxDistDelta));
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
