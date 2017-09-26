package com.raphydaphy.arcanemagic.gui;

import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.container.ContainerArcaneWorktable;
import com.raphydaphy.arcanemagic.tileentity.TileEntityArcaneWorktable;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiArcaneWorktable extends GuiContainer
{
	public static final int WIDTH = 176;
	public static final int HEIGHT = 239;

	private static final ResourceLocation background = new ResourceLocation(Thaumcraft.MODID,
			"textures/gui/arcane_work_table.png");

	public GuiArcaneWorktable(TileEntityArcaneWorktable tileEntity, ContainerArcaneWorktable container)
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

		drawTexturedModalRect(guiLeft, guiTop + 140, 8, 144, 176, 89);

		drawTexturedModalRect(guiLeft, guiTop, 5, 3, 134, 139);

		drawTexturedModalRect(guiLeft + 140, guiTop + 16, 149, 20, 38, 71);
	}
}