package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;

public class Categories
{
	public static final INotebookCategory ANCIENT_RELICS = new CategoryAncientRelics();
	public static final INotebookCategory CRYSTALLIZATION = new CategoryCrystallization();

	private static boolean done = false;

	public static void register()
	{
		if (done)
			return;
		done = true;

		ArcaneMagicAPI.registerCategory(ANCIENT_RELICS);
		ArcaneMagicAPI.registerCategory(CRYSTALLIZATION);
	}

}
