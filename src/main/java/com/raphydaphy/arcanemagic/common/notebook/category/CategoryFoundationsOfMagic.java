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

import akka.japi.Pair;
import net.minecraft.item.ItemStack;

public class CategoryFoundationsOfMagic extends NotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.foundations_of_magic";
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
		List<INotebookEntry> page1 = new ArrayList<INotebookEntry>();
		for (int i = 3; i < 6; i++)
		{
			page1.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page1));
		return pages;
	}


	@Override
	public String getRequiredTag()
	{
		return "unlockedFoundationsOfMagic";
	}
	
	public Pair<String, Integer> getUnlocParchmentInfo()
	{
		return new Pair<String, Integer>("arcanemagic.message.parchment.plants", 2);
	}
	
	@Override
	public String getPrerequisiteTag()
	{
		return NotebookCategories.ARCANE_ANALYSIS.getRequiredTag();
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(ModRegistry.ESSENCE);
	}
}
