package com.raphydaphy.arcanemagic.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;
import com.raphydaphy.arcanemagic.util.RecipeHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";

	private static ImmutableList<NotebookCategory> sorted_categories;
	
	public static void registerCategory(NotebookCategory category)
	{
		NotebookCategory.REGISTRY.register(category);
	}

	public static void registerEssence(Essence e)
	{
		Essence.REGISTRY.register(e);
	}

	public static void registerAllCategories(NotebookCategory... category)
	{
		NotebookCategory.REGISTRY.registerAll(category);
	}

	public static void registerAllEssences(Essence... e)
	{
		Essence.REGISTRY.registerAll(e);
	}

	public static int getCategoryCount()
	{
		if (sorted_categories == null)
			throw new UnsupportedOperationException("Categories not yet sorted!");
		return sorted_categories.size();
	}

	public static ImmutableList<NotebookCategory> getNotebookCategories()
	{
		if (sorted_categories == null)
			throw new UnsupportedOperationException("Categories not yet sorted!");
		return sorted_categories;
	}

	public static void setCategoryList(ImmutableList<NotebookCategory> list)
	{
		if (sorted_categories == null)
			sorted_categories = list;
		else
			throw new UnsupportedOperationException("Pls stop");
		ArcaneMagic.LOGGER
				.info("Setting sorted category list - being called from " + Thread.currentThread().getStackTrace()[1]);
	}
	
	public static IElementalRecipe getElementalCraftingRecipe(ItemStack wand, NonNullList<ItemStack> inputs, World world)
	{
		Preconditions.checkArgument(inputs.size() == 9);
		Preconditions.checkArgument(wand.hasTagCompound() && wand.hasCapability(IEssenceStorage.CAP, null));
		for (IElementalRecipe curRecipe : RecipeHelper.ELEMENTAL_RECIPES)
			if(curRecipe.matches(wand, inputs, world)) return curRecipe;
		return null;
	}
}
