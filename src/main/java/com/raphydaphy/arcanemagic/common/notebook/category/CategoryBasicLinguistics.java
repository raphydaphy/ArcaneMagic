package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.item.ItemStack;

public class CategoryBasicLinguistics extends NotebookCategory
{

	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.basic_linguistics";
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
	public String getRequiredTag()
	{
		return "unlockedBasicLinguistics";
	}
	
	@Override
	public String getPrerequisiteTag()
	{
		return NotebookCategories.FORGOTTEN_KNOWLEDGE.getRequiredTag();
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(ModRegistry.WRITING_DESK);
	}
}
