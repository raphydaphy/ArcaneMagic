package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class NotebookEntryText implements INotebookEntry
{
	private String unlocalizedText;
	private int color;
	
	public NotebookEntryText(String unlocalizedText, int color)
	{
		this.unlocalizedText = unlocalizedText;
		this.color = color;
	}
	
	@Override
	public ResourceLocation getRegistryName()
	{
		return new ResourceLocation(ArcaneMagic.MODID, "notebook_entry_text");
	}

	@Override
	public void draw(int x, int y, GuiScreen notebook)
	{
		notebook.mc.fontRenderer.drawSplitString(I18n.format(unlocalizedText), x, y, 180, color);
	}

	@Override
	public int getHeight(GuiScreen notebook)
	{
		return notebook.mc.fontRenderer.getWordWrappedHeight(I18n.format(unlocalizedText), 180);
	}

}
