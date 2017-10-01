package com.raphydaphy.arcanemagic.client.gui;

import java.util.Map;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.capabilities.EssenceStorage;
import com.raphydaphy.arcanemagic.container.ContainerCrystallizer;
import com.raphydaphy.arcanemagic.tileentity.TileEntityCrystallizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiCrystallizer extends GuiContainer
{
	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;
	private TileEntityCrystallizer te;
	private static final ResourceLocation background = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/crystallizer.png");

	public GuiCrystallizer(TileEntityCrystallizer tileEntity, ContainerCrystallizer container)
	{
		super(container);
		te = tileEntity;
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@SideOnly(Side.CLIENT)
	private void drawBar(int x, int y, float r, float g, float b, int essence, float rotation)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.rotate(rotation, 0, 0, 1);
		GlStateManager.color(1, 1, 1);
		GlStateManager.clearColor(1, 1, 1, 1);

		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/crystallizer.png"));

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		drawModalRectWithCustomSizedTexture(x, y, 177, 2, 12, 60, 196, 166);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 0.2f);
		drawModalRectWithCustomSizedTexture(x + 2, y + 6, 179, 63, 8, 48, 196, 166);
		drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, 29);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b, 1f);
		drawModalRectWithCustomSizedTexture(x + 2, y + 6 + (48 - essence), 179, 63, 8, essence, 196, 166);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{

		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		super.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(background);
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 196, 166);
		drawModalRectWithCustomSizedTexture(guiLeft + 19, guiTop + 39, 176, 113, 18, 18, 196, 166);
		Map<Essence, EssenceStack> essenceStored = te.getCapability(EssenceStorage.CAP, null).getStored();
		int i = 0;
		for (EssenceStack stack : essenceStored.values())
		{
			if (i == 0)
			{
				System.out.println("first essence is " + stack.getEssence().toString());
			}
			i++;
			Essence essence = stack.getEssence();
			drawBar(guiLeft + 37 + (i * 18), guiTop + 19, essence.getColor().getRed() / 256f,
					essence.getColor().getGreen() / 256f, essence.getColor().getBlue() / 256f,
					(int) Math.floor(stack.getCount() / 20), 0);
		}

		this.fontRenderer.drawString(I18n.format("gui.arcanemagic.crystallizer"), guiLeft + 58, guiTop + 7, 0x000000);
	}
}