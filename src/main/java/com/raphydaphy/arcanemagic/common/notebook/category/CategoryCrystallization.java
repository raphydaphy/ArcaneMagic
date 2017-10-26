package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.item.ItemStack;

public class CategoryCrystallization extends NotebookCategory
{
	public CategoryCrystallization()
	{
		setUnlocalizedName("arcanemagic.notebook.category.crystallization");
		setRequiredTag("unlockedCrystallization");
		setPrerequisiteTag(NotebookCategories.ELEMENTAL_CRAFTING.getRequiredTag());
		setIcon(new ItemStack(ModRegistry.CRYSTALLIZER));
	}

	@Override
	public List<NotebookPage> getPages(NotebookInfo info)
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> page0 = new ArrayList<INotebookEntry>();
		for (int i = 0; i < 3; i++)
		{
			page0.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page0));
		return pages;
	}
}
