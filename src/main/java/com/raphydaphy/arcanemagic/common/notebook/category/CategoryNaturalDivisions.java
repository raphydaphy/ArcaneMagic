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

public class CategoryNaturalDivisions extends NotebookCategory
{
	public CategoryNaturalDivisions()
	{
		setUnlocalizedName("arcanemagic.notebook.category.natural_divisions");
		setUnlocParchmentInfo(new Pair<String, Integer>("arcanemagic.message.parchment.natural_divisions", 2));
		setRequiredTag("unlockedNaturalDisvisions");
		setPrerequisiteTag(NotebookCategories.FOUNDATIONS_OF_MAGIC.getRequiredTag());
		setIcon(new ItemStack(ModRegistry.ESSENCE, 1, 1));
	}

	@Override
	public String getUnlocalizedTitle(NotebookInfo info, int page)
	{
		int fakePage = page;
		for (int potPage = 0; potPage < 7; potPage++)
		{
			if (potPage > fakePage)
			{
				break;
			}
			// this page is not visible yet to the player!
			if (potPage > 0
					&& !info.isUnlocked(NotebookCategories.NATURAL_DIVISION_PAGES[potPage - 1].getRequiredTag()))
			{
				fakePage++;
			}
		}
		return page == 0 ? getUnlocalizedName() : getUnlocalizedName() + "." + fakePage;
	}

	@Override
	public List<NotebookPage> getPages(NotebookInfo info)
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		for (int curPage = 0; curPage < 7; curPage++)
		{
			if (curPage < 1)
			{
				List<INotebookEntry> page = new ArrayList<INotebookEntry>();
				for (int i = 0; i < 3; i++)
				{
					page.add(new NotebookEntryText(getUnlocalizedName() + "." + curPage + "." + i, 0x000000));
				}
				pages.add(new NotebookPage(page));
			} else if (info.isUnlocked(NotebookCategories.NATURAL_DIVISION_PAGES[curPage - 1].getRequiredTag()))
			{
				pages.addAll(NotebookCategories.NATURAL_DIVISION_PAGES[curPage - 1].getPages(info));
			}
		}
		return pages;
	}
}
