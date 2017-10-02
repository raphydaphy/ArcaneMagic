package com.raphydaphy.arcanemagic.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GLHelper
{
	public static void drawCircle(double radius, double innerWidth, int x, int y, int z, Color color)
	{
		drawCircle(radius, innerWidth, x, y, z, color, 360);
	}
	public static void drawCircle(double radius, double innerWidth, int x, int y, int z, Color color, int segments)
	{
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();

		vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int deg = 0; deg <= segments; deg++)
		{

			double radius1 = deg % 2 == 0 ? radius : innerWidth;
			vb.pos(x + Math.cos(Math.toRadians(deg)) * (radius1), y + 2.8, z + Math.sin(Math.toRadians(deg)) * radius1)
					.color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
			vb.pos(x + Math.cos(Math.toRadians(deg + 1)) * (radius), y + 2.8, z + Math.sin(Math.toRadians(deg + 1)) * radius)
					.color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
			vb.pos(x + Math.cos(Math.toRadians(deg + 2)) * (radius1), y + 2.8, z + Math.sin(Math.toRadians(deg + 2)) * radius1)
					.color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		}

		tes.draw();
	}
}
