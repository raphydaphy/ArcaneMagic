package com.raphydaphy.arcanemagic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.raphydaphy.arcanemagic.api.analysis.AnalysisManager;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.recipe.IElementalCraftingItem;
import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";

	private static final AnalysisManager MANAGER = new AnalysisManager();

	private static ImmutableList<NotebookCategory> sorted_categories;

	private static final Logger LOGGER = LogManager.getLogger("ArcaneMagicAPI");

	public static final List<IElementalRecipe> ELEMENTAL_RECIPES = new ArrayList<>();

	// relates a oredict ID to a essence type that should be used to smelt the ore in an infernal furnace
	public static Map<Integer, Essence> ESSENCE_ORE_REGISTRY = new HashMap<Integer, Essence>();

	public static void registerCategory(NotebookCategory category)
	{
		NotebookCategory.REGISTRY.register(category);
	}

	public static void registerSubCategories(NotebookCategory... sub)
	{
		NotebookCategory.SUB_REGISTRY.registerAll(sub);
	}

	public static void registerOre(String oreDict, Essence essence)
	{
		ESSENCE_ORE_REGISTRY.put(OreDictionary.getOreID(oreDict), essence);
	}

	public static Essence getEssenceFromStack(ItemStack stack)
	{
		for (int id : OreDictionary.getOreIDs(stack))
		{
			if (ESSENCE_ORE_REGISTRY.containsKey(id))
			{
				return ESSENCE_ORE_REGISTRY.get(id);
			}
		}
		return null;
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

		LOGGER.info("Setting sorted category list - being called from " + Thread.currentThread().getStackTrace()[1]);
	}

	public static IElementalRecipe getElementalCraftingRecipe(EntityPlayer player, ItemStack wand,
			NonNullList<ItemStack> inputs, World world)
	{
		Preconditions.checkArgument(inputs.size() == 9,
				"[Arcane Magic]: Attempting to retrieve an elemental recipe with an invalid input list size!");
		Preconditions.checkArgument(wand.getItem() instanceof IElementalCraftingItem,
				"[Arcane Magic]: Attempting to retrieve an elemental recipe with an invalid wand stack! (Must be IElementalCraftingItem)");

		for (IElementalRecipe curRecipe : ELEMENTAL_RECIPES)
			if (curRecipe.matches(player, wand, inputs, world))
				return curRecipe;
		return null;
	}

	public static AnalysisManager getAnalyzer()
	{
		return MANAGER;
	}
}
