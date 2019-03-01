package com.raphydaphy.arcanemagic.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
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
		} else if (dir == Direction.SOUTH)
		{
			GlStateManager.rotated(-180, 0, 1, 0);
			GlStateManager.translated(-1, 0, -1);
		} else if (dir == Direction.WEST)
		{
			GlStateManager.rotated(-270, 0, 1, 0);
			GlStateManager.translated(-1, 0, 0);
		}
	}

	/**
	 * Draws a box with the specified dimensions
	 * Expects drawing in GL_QUADS mode
	 * Set maxU/V of any side to 0 to remove it
	 */
	public static void renderBox(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, TextureBounds[] textures, int[] inversions)
	{
		if (textures[0].maxU != 0)
		{
			// Bottom
			builder.vertex(x1, y1, z1).texture(textures[0].u, textures[0].v).color(255, 255, 255, 255).normal(0, -1 * inversions[0], 0).next();
			builder.vertex(x1, y1, z2).texture(textures[0].maxU, textures[0].v).color(255, 255, 255, 255).normal(0, -1 * inversions[0], 0).next();
			builder.vertex(x2, y1, z2).texture(textures[0].maxU, textures[0].maxV).color(255, 255, 255, 255).normal(0, -1 * inversions[0], 0).next();
			builder.vertex(x2, y1, z1).texture(textures[0].u, textures[0].maxV).color(255, 255, 255, 255).normal(0, -1 * inversions[0], 0).next();
		}

		if (textures[1].maxU != 0)
		{
			// Top
			builder.vertex(x1, y2, z1).texture(textures[1].u, textures[1].v).color(255, 255, 255, 255).normal(0, inversions[1], 0).next();
			builder.vertex(x1, y2, z2).texture(textures[1].maxU, textures[1].v).color(255, 255, 255, 255).normal(0, inversions[1], 0).next();
			builder.vertex(x2, y2, z2).texture(textures[1].maxU, textures[1].maxV).color(255, 255, 255, 255).normal(0, inversions[1], 0).next();
			builder.vertex(x2, y2, z1).texture(textures[1].u, textures[1].maxV).color(255, 255, 255, 255).normal(0, inversions[1], 0).next();
		}

		if (textures[2].maxU != 0)
		{
			// North
			builder.vertex(x1, y1, z1).texture(textures[2].u, textures[2].v).color(255, 255, 255, 255).normal(0, 0, -1 * inversions[2]).next();
			builder.vertex(x2, y1, z1).texture(textures[2].maxU, textures[2].v).color(255, 255, 255, 255).normal(0, 0, -1 * inversions[2]).next();
			builder.vertex(x2, y2, z1).texture(textures[2].maxU, textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1 * inversions[2]).next();
			builder.vertex(x1, y2, z1).texture(textures[2].u, textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1 * inversions[2]).next();
		}

		if (textures[3].maxU != 0)
		{
			// South
			builder.vertex(x1, y1, z2).texture(textures[3].u, textures[3].v).color(255, 255, 255, 255).normal(0, 0, inversions[3]).next();
			builder.vertex(x2, y1, z2).texture(textures[3].maxU, textures[3].v).color(255, 255, 255, 255).normal(0, 0, inversions[3]).next();
			builder.vertex(x2, y2, z2).texture(textures[3].maxU, textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, inversions[3]).next();
			builder.vertex(x1, y2, z2).texture(textures[3].u, textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, inversions[3]).next();
		}

		if (textures[4].maxU != 0)
		{
			// West
			builder.vertex(x1, y1, z1).texture(textures[4].u, textures[4].v).color(255, 255, 255, 255).normal(-1 * inversions[4], 0, 0).next();
			builder.vertex(x1, y1, z2).texture(textures[4].maxU, textures[4].v).color(255, 255, 255, 255).normal(-1 * inversions[4], 0, 0).next();
			builder.vertex(x1, y2, z2).texture(textures[4].maxU, textures[4].maxV).color(255, 255, 255, 255).normal(-1 * inversions[4], 0, 0).next();
			builder.vertex(x1, y2, z1).texture(textures[4].u, textures[4].maxV).color(255, 255, 255, 255).normal(-1 * inversions[4], 0, 0).next();
		}

		if (textures[5].maxU != 0)
		{
			// East
			builder.vertex(x2, y1, z1).texture(textures[5].u, textures[5].v).color(255, 255, 255, 255).normal(inversions[5], 0, 0).next();
			builder.vertex(x2, y1, z2).texture(textures[5].maxU, textures[5].v).color(255, 255, 255, 255).normal(inversions[5], 0, 0).next();
			builder.vertex(x2, y2, z2).texture(textures[5].maxU, textures[5].maxV).color(255, 255, 255, 255).normal(inversions[5], 0, 0).next();
			builder.vertex(x2, y2, z1).texture(textures[5].u, textures[5].maxV).color(255, 255, 255, 255).normal(inversions[5], 0, 0).next();
		}
	}

	public static class TextureBounds
	{
		double u;
		double v;
		double maxU;
		double maxV;

		public TextureBounds(double u, double v, double maxU, double maxV)
		{
			this(u, v, maxU, maxV, 16, 16);
		}

		private TextureBounds(double u, double v, double maxU, double maxV, double textureWidth, double textureHeight)
		{
			this.u = u / textureWidth;
			this.v = v / textureHeight;
			this.maxU = maxU / textureWidth;
			this.maxV = maxV / textureHeight;
		}
	}
}
