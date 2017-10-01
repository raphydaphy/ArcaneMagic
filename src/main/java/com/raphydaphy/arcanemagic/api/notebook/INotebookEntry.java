package com.raphydaphy.arcanemagic.api.notebook;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public interface INotebookEntry
{
	/*
	 *  The name used to compare the entry types
	 */
	public ResourceLocation getRegistryName();

	/*
	 *  Should draw the entry onto the screen at the specified coordinates
	 */
	public void draw(int x, int y, GuiScreen notebook);
	
	/*
	 * Should return the height in pixels that the entry takes up on the page
	 */
	public int getHeight(GuiScreen notebook);
}
