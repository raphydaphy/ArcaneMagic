package com.raphydaphy.arcanemagic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raphydaphy.arcanemagic.api.research.IThaumonomiconCategory;
import com.raphydaphy.arcanemagic.api.vis.Vis;
import com.raphydaphy.arcanemagic.api.wand.IWandCap;
import com.raphydaphy.arcanemagic.api.wand.IWandRod;

public class ThaumcraftAPI
{
	public static final String VERSION = "0.1";

	public static Map<String, Vis> VIS_TYPES = new HashMap<String, Vis>();

	public static Map<String, IWandCap> WAND_CAPS = new HashMap<String, IWandCap>();
	public static Map<String, IWandRod> WAND_RODS = new HashMap<String, IWandRod>();

	public static List<IThaumonomiconCategory> THAUMONOMICON_CATEGORIES = new ArrayList<IThaumonomiconCategory>();
	public static int TOTAL_THAUMONOMICON_CATEGORIES;

	public static void registerThaumonomiconCategory(IThaumonomiconCategory category)
	{
		TOTAL_THAUMONOMICON_CATEGORIES++;
		THAUMONOMICON_CATEGORIES.add(category);
	}

	public static void registerWandPart(Object part)
	{
		if (part instanceof IWandRod)
		{
			WAND_RODS.put(((IWandRod) part).getUnlocalizedName(), (IWandRod) part);
		} else if (part instanceof IWandCap)
		{
			WAND_CAPS.put(((IWandCap) part).getUnlocalizedName(), (IWandCap) part);
		}
	}
}
