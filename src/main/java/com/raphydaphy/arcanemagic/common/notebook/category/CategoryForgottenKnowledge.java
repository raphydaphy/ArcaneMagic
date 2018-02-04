package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.NotebookPage;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryCraftingRecipe;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CategoryForgottenKnowledge extends NotebookCategory {
	public CategoryForgottenKnowledge() {
		setUnlocalizedName("arcanemagic.notebook.category.forgotten_knowledge");
		setRequiredTag("unlockedForgottenKnowledge");
		setPrerequisiteTag(NotebookCategories.ANCIENT_RELICS.getRequiredTag());
		setIcon(new ItemStack(ModRegistry.NOTEBOOK));
	}

	@Override
	public List<NotebookPage> getPages(INotebookInfo info) {
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		entries.add(new NotebookEntryText(getUnlocalizedName() + "." + 0, 0x000000));
		ItemStack[][] itemsIn = { { ItemStack.EMPTY, new ItemStack(Items.ENDER_PEARL), ItemStack.EMPTY },
				{ new ItemStack(Blocks.PLANKS), new ItemStack(Blocks.GLASS), new ItemStack(Blocks.PLANKS) },
				{ new ItemStack(Blocks.PLANKS), ItemStack.EMPTY, new ItemStack(Blocks.PLANKS) } };
		entries.add(new NotebookEntryCraftingRecipe(itemsIn, new ItemStack(ModRegistry.ANALYZER)));
		entries.add(new NotebookEntryText(getUnlocalizedName() + "." + 1, 0x000000));
		pages.add(new NotebookPage(entries));
		return pages;
	}
}
