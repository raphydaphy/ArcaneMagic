package com.raphydaphy.arcanemagic.client.gui;

import com.raphydaphy.arcanemagic.ArcaneMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class GuiScepter
{
	private static void drawBar(int x, int y, float r, float g, float b, int essentia, float rotation)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.rotate(rotation, 0, 0, 1);
		GlStateManager.scale(0.8, 0.8, 0.8);
		GlStateManager.color(1, 1, 1);
		GlStateManager.clearColor(1, 1, 1, 1);

		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/wand.png"));

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, 181, 16, 10, 33);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y + 33, 181, 68, 10, 4);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 0.2f);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, 29);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b, 0.7f);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, essentia);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	public static void renderWandHUD(Minecraft mc, ScaledResolution res)
	{
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.translate(res.getScaledWidth() - 80, res.getScaledHeight() - 80, 0);
		GlStateManager.scale(0.8, 0.8, 0.8);

		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/wand.png"));
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(8, 8, 7, 7, 50, 50);
		GlStateManager.scale(1 / 0.8, 1 / 0.8, 1 / 0.8);

		float rot = 76.1f;
		// Perdito
		drawBar(37, 47, 0.25f, 0.25f, 0.25f, 10, 76.1f);
		rot += 23;
		// Ordo
		drawBar(25, 61, 0.83203125f, 0.828125f, 0.921875f, 18, rot);
		rot += 23;
		// Aqua
		drawBar(8, 70, 0.234375f, 0.828125f, 0.984375f, 28, rot);
		rot += 23;
		// Ignis
		drawBar(-11, 71, 0.99609375f, 0.3515625f, 0.00390625f, 14, rot);
		rot += 23;
		// Terra
		drawBar(-28, 64, 0.3359375f, 0.75f, 0.0f, 22, rot);
		rot += 23;
		// Aer
		drawBar(-41, 52, 1f, 1f, 0.4921875f, 12, rot);

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}
}