package com.raphydaphy.arcanemagic.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.notebook.entry.NotebookEntryText;

public class CategoryElementalParticles extends NotebookCategory
{

	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.elemental_particles";
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

	public static final String REQUIRED_TAG = "unlockedElementalParticles";

	@Override
	public String getRequiredTag()
	{
		return REQUIRED_TAG;
	}
}
