package com.raphydaphy.arcanemagic.api;

import java.util.ArrayList;
import java.util.List;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";

	

	private static final List<INotebookCategory> NOTEBOOK_CATEGORIES = new ArrayList<INotebookCategory>();
	private static int TOTAL_NOTEBOOK_CATEGORIES = 0;

	public static void registerNotebookCategory(INotebookCategory category)
	{
		TOTAL_NOTEBOOK_CATEGORIES++;
		NOTEBOOK_CATEGORIES.add(category);
	}

	public static int getCategoryCount() {
		return TOTAL_NOTEBOOK_CATEGORIES;
	}

	public static List<INotebookCategory> getNotebookCategories() {
		return NOTEBOOK_CATEGORIES;
	}
	
}
