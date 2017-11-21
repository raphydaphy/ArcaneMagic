package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import akka.japi.Pair;
import net.minecraft.item.ItemStack;

public class CategoryFoundationsOfMagic extends NotebookCategory
{
	public CategoryFoundationsOfMagic()
	{
		setUnlocalizedName("arcanemagic.notebook.category.foundations_of_magic");
		setUnlocParchmentInfo(new Pair<String, Integer>("arcanemagic.message.parchment.plants", 2));
		setRequiredTag("unlockedFoundationsOfMagic");
		setPrerequisiteTag(NotebookCategories.ARCANE_ANALYSIS.getRequiredTag());
		setIcon(new ItemStack(ModRegistry.ANIMA));
	}

	@Override
	public List<NotebookPage> getPages(INotebookInfo info)
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> page0 = new ArrayList<INotebookEntry>();
		for (int i = 0; i < 3; i++)
		{
			page0.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page0));
		List<INotebookEntry> page1 = new ArrayList<INotebookEntry>();
		for (int i = 3; i < 6; i++)
		{
			page1.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page1));
		return pages;
	}
}
