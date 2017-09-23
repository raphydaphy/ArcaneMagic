package com.raphydaphy.thaumcraft.api.research;

import java.util.List;

import net.minecraft.util.ResourceLocation;

public interface IThaumonomiconCategory 
{
	// The name of the category's tab
	public String getUnlocalizedName();
	
	// The icon of the tab in the thaumonomicon
	public ResourceLocation getIcon();
	
	// A list of all the entries within the category
	public List<IThaumonomiconEntry> getEntries();
}
