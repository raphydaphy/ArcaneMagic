package com.raphydaphy.arcanemagic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;
import com.raphydaphy.arcanemagic.api.wand.IScepterCap;
import com.raphydaphy.arcanemagic.api.wand.IScepterRod;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";

	public static Map<String, Essence> ESSENCE_TYPES = new HashMap<String, Essence>();

	public static Map<String, IScepterCap> SCEPTER_TIPS = new HashMap<String, IScepterCap>();
	public static Map<String, IScepterRod> SCEPTER_CORES = new HashMap<String, IScepterRod>();

	public static List<INotebookCategory> NOTEBOOK_CATEGORIES = new ArrayList<INotebookCategory>();
	public static int TOTAL_NOTEBOOK_CATEGORIES;

	public static void registerNotebookCategory(INotebookCategory category)
	{
		TOTAL_NOTEBOOK_CATEGORIES++;
		NOTEBOOK_CATEGORIES.add(category);
	}

	public static void registerScepterPart(Object part)
	{
		if (part instanceof IScepterRod)
		{
			SCEPTER_CORES.put(((IScepterRod) part).getUnlocalizedName(), (IScepterRod) part);
		} else if (part instanceof IScepterCap)
		{
			SCEPTER_TIPS.put(((IScepterCap) part).getUnlocalizedName(), (IScepterCap) part);
		}
	}
}
