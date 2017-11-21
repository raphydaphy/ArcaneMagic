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

public class CategoryElementalCrafting extends NotebookCategory
{
	public CategoryElementalCrafting()
	{
		setUnlocalizedName("arcanemagic.notebook.category.elemental_crafting");
		setRequiredTag("unlockedElementalCrafting");
		setPrerequisiteTag(NotebookCategories.ESSENCE_MANIPULATION.getRequiredTag());
		setIcon(new ItemStack(ModRegistry.ELEMENTAL_CRAFTING_TABLE));
	}

	@Override
	public List<NotebookPage> getPages(INotebookInfo info)
	{
		List<NotebookPage> pages = new ArrayList<NotebookPage>();
		List<INotebookEntry> page0 = new ArrayList<INotebookEntry>();
		for (int i = 0; i < 2; i++)
		{
			page0.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000));
		}
		pages.add(new NotebookPage(page0));
		List<INotebookEntry> page1 = new ArrayList<INotebookEntry>();

		page1.add(new NotebookEntryText(getUnlocalizedName() + ".2", 0x000000));
		ItemStack[][] crystallizerItemsIn = { { ItemStack.EMPTY, new ItemStack(Items.BLAZE_ROD), ItemStack.EMPTY },
				{ new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.PRISMARINE_CRYSTALS),
						new ItemStack(Items.BLAZE_ROD) },
				{ new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.IRON_BLOCK),
						new ItemStack(Blocks.IRON_BLOCK) } };
		page1.add(new NotebookEntryCraftingRecipe(crystallizerItemsIn, new ItemStack(ModRegistry.ANIMUS_MATERIALIZER)));
		page1.add(new NotebookEntryText(getUnlocalizedName() + ".3", 0x000000));
		ItemStack[][] concentratorItemsIn = { { ItemStack.EMPTY, new ItemStack(Items.REDSTONE), ItemStack.EMPTY },
				{ new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Blocks.IRON_BLOCK),
						new ItemStack(Items.GLOWSTONE_DUST) },
				{ new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.IRON_BLOCK),
						new ItemStack(Blocks.IRON_BLOCK) } };
		page1.add(
				new NotebookEntryCraftingRecipe(concentratorItemsIn, new ItemStack(ModRegistry.ANIMA_CONJURER)));
		pages.add(new NotebookPage(page1));
		return pages;
	}
}
