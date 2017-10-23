package com.raphydaphy.arcanemagic.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.raphydaphy.arcanemagic.api.analysis.AnalysisManager;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.recipe.IElementalCraftingItem;
import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";
	
	private static final AnalysisManager MANAGER = new AnalysisManager();

	private static ImmutableList<NotebookCategory> sorted_categories;

	public static void registerCategory(NotebookCategory category)
	{
		NotebookCategory.REGISTRY.register(category);
	}

	public static void registerSubCategories(NotebookCategory... sub)
	{
		NotebookCategory.SUB_REGISTRY.registerAll(sub);
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

	public static IElementalRecipe getElementalCraftingRecipe(EntityPlayer player, ItemStack wand, NonNullList<ItemStack> inputs, World world)
	{
		Preconditions.checkArgument(inputs.size() == 9, "[Arcane Magic]: Attempting to retrieve an elemental recipe with an invalid input list size!");
		Preconditions.checkArgument(wand.getItem() instanceof IElementalCraftingItem, "[Arcane Magic]: Attempting to retrieve an elemental recipe with an invalid wand stack! (Must be IElementalCraftingItem)");
		Preconditions.checkArgument(wand.hasTagCompound(), "[Arcane Magic]: Attempting to retrieve an elemental recipe with an invalid wand stack! (Needs NBT)");
		
		for (IElementalRecipe curRecipe : RecipeHelper.ELEMENTAL_RECIPES)
			if (curRecipe.matches(player, wand, inputs, world))
				return curRecipe;
		return null;
	}
	
	public static AnalysisManager getAnalyzer() {
		return MANAGER;
	}
}
