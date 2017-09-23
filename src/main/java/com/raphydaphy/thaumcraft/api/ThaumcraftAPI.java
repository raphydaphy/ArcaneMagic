package com.raphydaphy.thaumcraft.api;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.thaumcraft.api.research.IThaumonomiconCategory;

public class ThaumcraftAPI 
{
	public static final String VERSION = "0.1";
	
	public static List<IThaumonomiconCategory> THAUMONOMICON_CATEGORIES = new ArrayList<IThaumonomiconCategory>();
	public static int TOTAL_THAUMONOMICON_CATEGORIES;
	
	public static void registerThaumonomiconCategory(IThaumonomiconCategory category)
	{
		TOTAL_THAUMONOMICON_CATEGORIES++;
		THAUMONOMICON_CATEGORIES.add(category);
	}
}
