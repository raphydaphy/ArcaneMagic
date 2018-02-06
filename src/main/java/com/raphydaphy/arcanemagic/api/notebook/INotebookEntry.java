package com.raphydaphy.arcanemagic.api.notebook;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface INotebookEntry
{
	/*
	 * The name used to compare the entry types
	 */
	public ResourceLocation getRegistryName();

	/*
	 * Does the entry contain the specified text If it does, it will show up
	 * when searched for
	 */
	@SideOnly(Side.CLIENT)
	public boolean containsSearchKey(String searchKey);

	/*
	 * Should draw the entry onto the screen at the specified coordinates
	 */
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int mouseX, int mouseY, GuiScreen notebook);

	/*
	 * Anything that needs to be run after all other rendering is done ie.
	 * tooltips
	 */
	@SideOnly(Side.CLIENT)
	public default void drawPost(int x, int y, int mouseX, int mouseY, GuiScreen notebook)
	{

	}

	/*
	 * Should return the height in pixels that the entry takes up on the page
	 */
	@SideOnly(Side.CLIENT)
	public int getHeight(GuiScreen notebook);
}
