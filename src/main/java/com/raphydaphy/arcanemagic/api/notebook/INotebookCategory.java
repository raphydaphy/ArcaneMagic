package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.raphydaphy.arcanemagic.api.util.Pos2;

import net.minecraft.util.ResourceLocation;

public interface INotebookCategory
{
	// The name of the category's tab
	public String getUnlocalizedName();

	// The icon of the tab in the thaumonomicon
	public ResourceLocation getIcon();

	// A list of all the entries within the category
	public List<INotebookEntry> getEntries();
	
	// The background resourcelocation, and the dimensions
	public Pair<ResourceLocation, Pos2> getBackground();
}
