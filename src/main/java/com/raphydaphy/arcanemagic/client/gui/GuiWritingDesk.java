package com.raphydaphy.arcanemagic.client.gui;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.container.ContainerWritingDesk;
import com.raphydaphy.arcanemagic.tileentity.TileEntityWritingDesk;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiWritingDesk extends GuiContainer
{
	public static final int WIDTH = 176;
	public static final int HEIGHT = 192;
	private TileEntityWritingDesk te;
	private static final ResourceLocation background = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/writing_desk.png");

	public GuiWritingDesk(TileEntityWritingDesk tileEntity, ContainerWritingDesk container)
	{
		super(container);
		te = tileEntity;
		xSize = WIDTH;
		ySize = HEIGHT;
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
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 250, 203);
		this.fontRenderer.drawString(I18n.format("gui.arcanemagic.writing_desk"), guiLeft + 58, guiTop + 7, 0x000000);
	}
}