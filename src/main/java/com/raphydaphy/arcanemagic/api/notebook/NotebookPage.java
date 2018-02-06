package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;

public class NotebookPage
{
	private final List<INotebookEntry> entries;

	public NotebookPage(List<INotebookEntry> entries)
	{
		this.entries = entries;
	}

	public boolean matchesText(String searchKey)
	{
		for (INotebookEntry entry : entries)
		{
			if (entry.containsSearchKey(searchKey))
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * Should draw the entry onto the screen at the specified coordinates
	 */
	public void draw(int x, int y, int mouseX, int mouseY, GuiScreen notebook)
	{
		int curY = 0;
		for (INotebookEntry entry : entries)
		{
			entry.draw(x, y + curY, mouseX, mouseY, notebook);
			curY += entry.getHeight(notebook) + 5;
		}
	}

	/*
	 * Anything that needs to be run after all other rendering is done ie.
	 * tooltips
	 */
	public void drawPost(int x, int y, int mouseX, int mouseY, GuiScreen notebook)
	{
		int curY = 0;
		for (INotebookEntry entry : entries)
		{
			entry.drawPost(x, y + curY, mouseX, mouseY, notebook);
			curY += entry.getHeight(notebook) + 5;
		}
	}
}
