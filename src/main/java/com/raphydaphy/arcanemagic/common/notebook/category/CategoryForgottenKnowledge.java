package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryCraftingRecipe;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CategoryForgottenKnowledge extends NotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.forgotten_knowledge";
	}

	
	@Override
	public List<NotebookPage> getPages()
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		entries.add(new NotebookEntryText(getUnlocalizedName() + "." + 0, 0x000000));
		ItemStack[][] itemsIn = {
				{ ItemStack.EMPTY, new ItemStack(Items.ENDER_PEARL), ItemStack.EMPTY },
				{ new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.GLASS), new ItemStack(Blocks.PLANKS) },
				{ new ItemStack(Blocks.PLANKS), ItemStack.EMPTY, new ItemStack(Blocks.PLANKS) } };
		entries.add(new NotebookEntryCraftingRecipe(itemsIn, new ItemStack(ModRegistry.ANALYZER)));
		entries.add(new NotebookEntryText(getUnlocalizedName() + "." + 1, 0x000000));
		pages.add(new NotebookPage(entries));
		return pages;
	}

	@Override
	public String getRequiredTag()
	{
		return "unlockedForgottenKnowledge";
	}

	@Override
	public String getPrerequisiteTag()
	{
		return NotebookCategories.ANCIENT_RELICS.getRequiredTag();
	}

	public ItemStack getIcon()
	{
		return new ItemStack(ModRegistry.NOTEBOOK);
	}
}
