package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import akka.japi.Pair;
import net.minecraft.item.ItemStack;

public class CategoryNaturalDivisionsSub extends NotebookCategory
{
	public final Essence categoryEssence;
	public final int indexPage;
	public final int pageCount;

	public CategoryNaturalDivisionsSub(Essence categoryEssence, int indexPage, int pageCount)
	{
		this.categoryEssence = categoryEssence;
		this.indexPage = indexPage;
		this.pageCount = pageCount;
		
		this.setRegistryName(ArcaneMagic.MODID, "natural_divisions_" + indexPage);
	}

	@Override
	public String getRequiredTag()
	{
		return NotebookCategories.NATURAL_DIVISIONS.getRequiredTag() + "_" + categoryEssence.getIndexName();
	}

	@Override
	public Pair<String, Integer> getUnlocParchmentInfo()
	{
		return new Pair<String, Integer>(
				"arcanemagic.message.parchment.natural_divisions." + categoryEssence.getIndexName(), 2);
	}

	@Override
	public String getPrerequisiteTag()
	{
		return NotebookCategories.NATURAL_DIVISIONS.getRequiredTag();
	}

	@Override
	public String getUnlocalizedName()
	{
		return NotebookCategories.NATURAL_DIVISIONS.getUnlocalizedName() + "." + indexPage;
	}

	@Override
	public List<NotebookPage> getPages(NotebookInfo info)
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> page = new ArrayList<INotebookEntry>();
		for (int i = 0; i < pageCount; i++)
		{
			page.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page));
		return pages;
	}

	@Override
	public ItemStack getIcon()
	{
		return categoryEssence.getItemForm();
	}

}