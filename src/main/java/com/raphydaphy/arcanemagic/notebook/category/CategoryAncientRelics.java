package com.raphydaphy.arcanemagic.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.notebook.entry.NotebookEntryText;

import net.minecraft.item.ItemStack;

public class CategoryAncientRelics extends NotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.ancient_relics";
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

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(ModRegistry.NOTEBOOK);
	}
}
