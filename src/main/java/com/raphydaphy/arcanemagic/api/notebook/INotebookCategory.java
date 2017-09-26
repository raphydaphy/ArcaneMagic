package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

import net.minecraft.util.ResourceLocation;

public interface INotebookCategory
{
	// The name of the category's tab
	public String getUnlocalizedName();

	// The icon of the tab in the thaumonomicon
	public ResourceLocation getIcon();

	// A list of all the entries within the category
	public List<INotebookEntry> getEntries();
}
