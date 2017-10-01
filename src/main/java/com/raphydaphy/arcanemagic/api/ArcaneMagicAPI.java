package com.raphydaphy.arcanemagic.api;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";

	private static final List<INotebookCategory> CATEGORIES = new ArrayList<INotebookCategory>();

	public static void registerCategory(INotebookCategory category)
	{
		Preconditions.checkNotNull(category, "Cannot register a null object!");
		Preconditions.checkArgument(!CATEGORIES.contains(category), "Cannot use an already existing registry name!");
		CATEGORIES.add(category);
	}

	public static void registerEssence(Essence e)
	{
		Essence.REGISTRY.register(e);
	}

	public static int getCategoryCount()
	{
		return CATEGORIES.size();
	}

	public static List<INotebookCategory> getNotebookCategories()
	{
		return CATEGORIES;
	}

}
