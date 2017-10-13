package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
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
	public List<INotebookEntry> getEntries()
	{
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		entries.add(new NotebookEntryText(getUnlocalizedName() + "." + 0, 0x000000));
		ItemStack[][] itemsIn = {
				{ new ItemStack(Items.PAPER), new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.PAPER) },
				{ new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.PLANKS) },
				{ new ItemStack(Blocks.PLANKS), ItemStack.EMPTY, new ItemStack(Blocks.PLANKS) } };
		entries.add(new NotebookEntryCraftingRecipe(itemsIn, new ItemStack(ModRegistry.WRITING_DESK)));
		entries.add(new NotebookEntryText(getUnlocalizedName() + "." + 1, 0x000000));
		return entries;
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
