package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
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
	public List<INotebookEntry> getEntries()
	{
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		for (int i = 0; i < 2; i++)
		{
			entries.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		ItemStack[][] itemsIn = { { ItemStack.EMPTY, new ItemStack(Items.BLAZE_ROD), ItemStack.EMPTY },
				{ new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.PRISMARINE_CRYSTALS),
						new ItemStack(Items.BLAZE_ROD) },
				{ new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.IRON_BLOCK),
						new ItemStack(Blocks.IRON_BLOCK) } };
		entries.add(new NotebookEntryCraftingRecipe(itemsIn, new ItemStack(ModRegistry.CRYSTALLIZER)));

		return entries;
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
