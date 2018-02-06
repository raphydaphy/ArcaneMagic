package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.item.ItemStack;

public class CategoryAncientRelics extends NotebookCategory
{
	public CategoryAncientRelics()
	{
		setUnlocalizedName("arcanemagic.notebook.category.ancient_relics");
		setRequiredTag("unlockedAncientRelics");
		setIcon(new ItemStack(ModRegistry.ANCIENT_PARCHMENT));
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
		return pages;
	}
}
