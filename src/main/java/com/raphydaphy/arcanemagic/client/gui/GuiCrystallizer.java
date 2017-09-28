package com.raphydaphy.arcanemagic.client.gui;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.container.ContainerCrystallizer;
import com.raphydaphy.arcanemagic.tileentity.TileEntityCrystallizer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiCrystallizer extends GuiContainer
{
	public static final int WIDTH = 200;
	public static final int HEIGHT = 300;

	private static final ResourceLocation background = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/crystallizer.png");

	public GuiCrystallizer(TileEntityCrystallizer tileEntity, ContainerCrystallizer container)
	{
		super(container);

		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(background);

		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 200, 300);
	}
}