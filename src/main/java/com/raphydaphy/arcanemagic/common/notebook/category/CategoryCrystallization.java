package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryCraftingRecipe;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CategoryCrystallization extends NotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.crystallization";
	}

	@Override
	public List<NotebookPage> getPages(NotebookInfo info)
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> page0 = new ArrayList<INotebookEntry>();
		for (int i = 0; i < 2; i++)
		{
			page0.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page0));
		List<INotebookEntry> page1 = new ArrayList<INotebookEntry>();
		ItemStack[][] itemsIn = { { ItemStack.EMPTY, new ItemStack(Items.BLAZE_ROD), ItemStack.EMPTY },
				{ new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.PRISMARINE_CRYSTALS),
						new ItemStack(Items.BLAZE_ROD) },
				{ new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.IRON_BLOCK),
						new ItemStack(Blocks.IRON_BLOCK) } };
		page1.add(new NotebookEntryText(getUnlocalizedName() + ".2", 0x000000));
		page1.add(new NotebookEntryCraftingRecipe(itemsIn, new ItemStack(ModRegistry.CRYSTALLIZER)));
		pages.add(new NotebookPage(page1));
		return pages;
	}

	@Override
	public String getRequiredTag()
	{
		return "unlockedCrystallization";
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(ModRegistry.CRYSTALLIZER);
	}
}
