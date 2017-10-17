package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.item.ItemStack;

public class CategoryNaturalHarmony extends NotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.natural_harmony";
	}

	@Override
	public List<NotebookPage> getPages()
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


	@Override
	public String getRequiredTag()
	{
		return "unlockedNaturalHarmony";
	}

	@Override
	public ItemStack getIcon()
	{
		return Essence.CREATION.getItemForm();
	}
}
