package com.raphydaphy.arcanemagic.notebook;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;

public class CategoryNaturalHarmony implements INotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.natural_harmony";
	}

	@Override
	public List<INotebookEntry> getEntries()
	{
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		for (int i = 0; i < 3; i++)
		{
			entries.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		
		return entries;
	}
}
