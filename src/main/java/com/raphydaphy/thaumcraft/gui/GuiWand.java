package com.raphydaphy.thaumcraft.gui;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class GuiWand
{
	private static void drawBar(int x, int y, float r, float g, float b, int essentia)
	{
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, 181, 16, 10, 33);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y + 33, 181, 68, 10, 4);
		
		GlStateManager.color(1, 1, 1, 0.2f);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, 29);
		
		GlStateManager.color(r, g, b, 0.7f);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, essentia);
	}
	
	public static void renderWandHUD(Minecraft mc, ScaledResolution res)
	{
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		
		GlStateManager.pushMatrix();
		
		GlStateManager.enableBlend();
		GlStateManager.scale(0.8, 0.8, 0.8);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Thaumcraft.MODID, "textures/gui/wand.png"));
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(8, 8, 7, 7, 50, 50);
		
		// Perdito
		GlStateManager.rotate(15.9f, 0, 0, 1);
		drawBar(37, 47, 0, 0, 0,10);
		GlStateManager.rotate(-15.9f, 0, 0, 1);
		
		// Ordo
		GlStateManager.rotate(-7.1f, 0, 0, 1);
		drawBar(25, 61, 0.75f, 0.75f, 0.75f, 18);
		GlStateManager.rotate(7.1f, 0, 0, 1);
		
		// Aqua
		GlStateManager.rotate(-30.1f, 0, 0, 1);
		drawBar(8, 70, 0f, 0.24f, 0.6f, 28);
		GlStateManager.rotate(30.1f, 0, 0, 1);
		
		// Ignis
		GlStateManager.rotate(-53.1f, 0, 0, 1);
		drawBar(-11, 71, 0.99f, 0.30f, 0.18f, 14);
		GlStateManager.rotate(53.1f, 0, 0, 1);
		
		// Terra
		GlStateManager.rotate(-76.1f, 0, 0, 1);
		drawBar(-28, 64, 0.27f, 0.55f, 0.0f, 22);
		GlStateManager.rotate(76.1f, 0, 0, 1);
		
		// Aer
		GlStateManager.rotate(-99.1f, 0, 0, 1);
		drawBar(-41, 52, 0.99f, 0.82f, 0.09f, 12);
		GlStateManager.rotate(99.1f, 0, 0, 1);
		
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
	}
}