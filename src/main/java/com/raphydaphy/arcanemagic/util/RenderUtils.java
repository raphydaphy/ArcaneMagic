package com.raphydaphy.arcanemagic.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.Direction;

import java.util.List;

public class RenderUtils
{
	public static void drawSplitString(TextRenderer textRenderer, String text, float x, float y, int wrap, int color, boolean verticallyCentered)
	{
		List<String> strings = textRenderer.wrapStringToWidthAsList(text, wrap);

		if (verticallyCentered)
		{

			y -= (strings.size() / 2f) * textRenderer.fontHeight;
		}
		for (String s : strings)
		{
			textRenderer.draw(s, (x - textRenderer.getStringWidth(s) / 2f), y, color);
			y += textRenderer.fontHeight;
		}
	}

	@Environment(EnvType.CLIENT)
	public static void rotateTo(Direction dir)
	{
		if (dir == Direction.EAST)
		{
			GlStateManager.rotated(-90, 0, 1, 0);
			GlStateManager.translated(0, 0, -1);
		}
		else if (dir == Direction.SOUTH)
		{
			GlStateManager.rotated(-180, 0, 1, 0);
			GlStateManager.translated(-1, 0, -1);
		}
		else if (dir == Direction.WEST)
		{
			GlStateManager.rotated(-270, 0, 1, 0);
			GlStateManager.translated(-1, 0, 0);
		}
	}
}
