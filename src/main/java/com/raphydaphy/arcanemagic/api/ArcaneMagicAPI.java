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
import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.recipe.IArcaneTransfigurationItem;
import com.raphydaphy.arcanemagic.api.recipe.IArcaneTransfigurationRecipe;

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

	public static final List<IArcaneTransfigurationRecipe> ARCANE_TRANSFIGURATION_RECIPES = new ArrayList<>();

	// relates a oredict ID to a anima type that should be used to smelt the ore
	// in an infernal furnace
	public static Map<Integer, Anima> ANIMA_ORE_REGISTRY = new HashMap<Integer, Anima>();

	public static void registerCategory(NotebookCategory category)
	{
		NotebookCategory.REGISTRY.register(category);
	}

	public static void registerSubCategories(NotebookCategory... sub)
	{
		NotebookCategory.SUB_REGISTRY.registerAll(sub);
	}

	public static void registerOre(String oreDict, Anima anima)
	{
		ANIMA_ORE_REGISTRY.put(OreDictionary.getOreID(oreDict), anima);
	}

	public static Anima getAnimaFromStack(ItemStack stack)
	{
		for (int id : OreDictionary.getOreIDs(stack))
		{
			if (ANIMA_ORE_REGISTRY.containsKey(id))
			{
				return ANIMA_ORE_REGISTRY.get(id);
			}
		}
		return null;
	}

	public static void registerAnima(Anima e)
	{
		Anima.REGISTRY.register(e);
	}

	public static void registerAllCategories(NotebookCategory... category)
	{
		NotebookCategory.REGISTRY.registerAll(category);
	}

	public static void registerAllAnimus(Anima... e)
	{
		Anima.REGISTRY.registerAll(e);
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

	public static IArcaneTransfigurationRecipe getArcaneTransfigurationRecipe(EntityPlayer player, ItemStack wand,
			NonNullList<ItemStack> inputs, World world)
	{
		Preconditions.checkArgument(inputs.size() == 9,
				"[Arcane Magic]: Attempting to retrieve an arcane transfiguration recipe with an invalid input list size!");
		Preconditions.checkArgument(wand.getItem() instanceof IArcaneTransfigurationItem,
				"[Arcane Magic]: Attempting to retrieve an arcane transfiguration recipe with an invalid wand stack! (Must be IElementalCraftingItem)");

		for (IArcaneTransfigurationRecipe curRecipe : ARCANE_TRANSFIGURATION_RECIPES)
			if (curRecipe.matches(player, wand, inputs, world))
				return curRecipe;
		return null;
	}

	public static AnalysisManager getAnalyzer()
	{
		return MANAGER;
	}
}
