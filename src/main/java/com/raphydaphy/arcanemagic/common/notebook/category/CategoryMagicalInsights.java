package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryCraftingRecipe;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import akka.japi.Pair;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CategoryMagicalInsights extends NotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.magical_insights";
	}

	@Override
	public List<NotebookPage> getPages(NotebookInfo info)
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> page0 = new ArrayList<INotebookEntry>();
		page0.add(new NotebookEntryText(getUnlocalizedName() + ".0", 0x000000));
		ItemStack[][] itemsIn = { { ItemStack.EMPTY, new ItemStack(Items.REDSTONE), ItemStack.EMPTY },
				{ new ItemStack(Items.REDSTONE), new ItemStack(Items.COMPASS),
						new ItemStack(Items.REDSTONE) },
				{ ItemStack.EMPTY, new ItemStack(Items.REDSTONE),
							ItemStack.EMPTY } };
		page0.add(new NotebookEntryCraftingRecipe(itemsIn, new ItemStack(ModRegistry.MYSTICAL_ILLUMINATOR)));
		page0.add(new NotebookEntryText(getUnlocalizedName() + ".1", 0x000000));
		pages.add(new NotebookPage(page0));
		List<INotebookEntry> page1 = new ArrayList<INotebookEntry>();
		for (int i = 2; i < 3; i++)
		{
			page1.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page1));
		return pages;
	}


	@Override
	public String getRequiredTag()
	{
		return "unlockedMagicalInsights";
	}
	
	public Pair<String, Integer> getUnlocParchmentInfo()
	{
		return new Pair<String, Integer>("arcanemagic.message.parchment.redstone", 2);
	}

	@Override
	public String getPrerequisiteTag()
	{
		return NotebookCategories.ARCANE_ANALYSIS.getRequiredTag();
	}
	
	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(ModRegistry.MYSTICAL_ILLUMINATOR);
	}
}
