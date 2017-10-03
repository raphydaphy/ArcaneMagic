package com.raphydaphy.arcanemagic.util;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GLHelper
{
	public static void drawCircle(double radius, double innerWidth, double x, double y, double z, Color color)
	{
		drawCircle(radius, innerWidth, x, y, z, color, 360);
	}

	public static void drawCircle(double radius, double innerWidth, double x, double y, double z, Color color,
			int segments)
	{
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();

		vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int deg = 0; deg <= segments; deg++)
		{

			double radius1 = deg % 2 == 0 ? radius : innerWidth;
			vb.pos(x + Math.cos(Math.toRadians(deg)) * (radius1), y, z + Math.sin(Math.toRadians(deg)) * radius1)
					.color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
			vb.pos(x + Math.cos(Math.toRadians(deg + 1)) * (radius), y,
					z + Math.sin(Math.toRadians(deg + 1)) * radius)
					.color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
			vb.pos(x + Math.cos(Math.toRadians(deg + 2)) * (radius1), y,
					z + Math.sin(Math.toRadians(deg + 2)) * radius1)
					.color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		}

		tes.draw();
	}

	public static void drawTriangle(double width, double x, double y, double z, Color color)
	{
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();

		double p = (3 * width) / 2;
		double area = Math.sqrt((p * (p - width) * (p - width) * (p - width)));
		double height = 2 * (area / width);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(-30, 0, 1, 0);

		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		vb.pos(0, 0, width).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0.02, 0, width).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0.02, 0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0, 0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();

		tes.draw();

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(30, 0, 1, 0);

		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		vb.pos(0, 0, width).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0.02, 0, width).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0.02, 0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0, 0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();

		tes.draw();

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x - (width / 2), y, z + height);
		GlStateManager.rotate(90, 0, 1, 0);

		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		vb.pos(0, 0, width).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0.02, 0, width).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0.02, 0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
		vb.pos(0, 0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();

		tes.draw();

		GlStateManager.popMatrix();
	}

	public static void renderFancyBeams(double x, double y, double z, Color color, long seed, long continuousTick,
			float scale)
	{
		GLHelper.renderFancyBeams(x, y, z, color, seed, continuousTick, 16, seed, 30, 10);
	}

	public static void renderFancyBeams(double x, double y, double z, Color color, long seed, long continuousTick,
			int dstJump, float scale, int countFancy, int countNormal)
	{
		Random rand = new Random(seed);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		int fancy_count = !FMLClientHandler.instance().getClient().gameSettings.fancyGraphics ? countNormal
				: countFancy;

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();

		RenderHelper.disableStandardItemLighting();
		float rotateSpeed = continuousTick / 1000.0F;
		float beamSize = 0.4F;

		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.disableAlpha();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		for (int i = 0; i < fancy_count; i++)
		{
			// rotate the current beam so it isn't 2D
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);

			// Animate the beams spinning
			GlStateManager.rotate(rand.nextFloat() * 360.0F + rotateSpeed * 360.0F, 0.0F, 0.0F, 1.0F);

			vb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
			// length and width of the individual beams
			float length = (rand.nextFloat() * 20.0F + 5.0F + beamSize * 10.0F)
					/ (30.0F / (Math.min(dstJump, 10 * scale) / 10.0F));
			float width = 0.08f;

			//vb.color(1, 1, 1, 1);
			vb.pos(0, 0, 0)
					.color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255.0F * (1.0F - beamSize)) * 10)
					.endVertex();
			vb.pos(-0.7D * width, length, -0.5F * width).color(color.getRed(), color.getGreen(), color.getBlue(), 0)
					.endVertex();
			vb.pos(0.7D * width, length, -0.5F * width).color(color.getRed(), color.getGreen(), color.getBlue(), 0)
					.endVertex();
			vb.pos(0.0D, length, 1.0F * width).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
			vb.pos(-0.7D * width, length, -0.5F * width).color(color.getRed(), color.getGreen(), color.getBlue(), 0)
					.endVertex();
			tes.draw();
		}
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();

		GlStateManager.popMatrix();
	}
}
