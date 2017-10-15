package com.raphydaphy.arcanemagic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";

	private static ImmutableList<NotebookCategory> sorted_categories;

	// i hope shadows approves
	private static Map<ItemStack, NotebookCategory> ANALYSIS_ITEMS = new HashMap<ItemStack, NotebookCategory>();
	private static final Map<ItemStack, List<NotebookCategory>> ANALYZED_ITEMS = new HashMap<ItemStack, List<NotebookCategory>>();

	public static void registerCategory(NotebookCategory category)
	{
		NotebookCategory.REGISTRY.register(category);
	}

	public static void registerForAnalysis(ItemStack item, NotebookCategory category)
	{
		ANALYSIS_ITEMS.put(item, category);
	}

	@Nullable
	public static List<NotebookCategory> getFromAnalysis(ItemStack analyzed, List<ItemStack> ignore)
	{
		for (ItemStack cached : ANALYZED_ITEMS.keySet())
		{
			if (ItemStack.areItemsEqualIgnoreDurability(cached, analyzed))
			{
				System.out.println(cached.toString() + " was already cached as " + ANALYZED_ITEMS.get(cached));
				return ANALYZED_ITEMS.get(cached);
			}
		}

		List<NotebookCategory> ret = new ArrayList<NotebookCategory>();

		for (ItemStack analysisItem : ANALYSIS_ITEMS.keySet())
		{
			if (OreDictionary.itemMatches(analysisItem, analyzed, false))
			{
				System.out.println("BOOM we found something for " + analysisItem.toString());
				ret.add(ANALYSIS_ITEMS.get(analysisItem));
				break;
			}
			else
			{
				System.out.println(analyzed.toString() + " is not " + analysisItem.toString());
			}
		}

		// might kill ur computer
		for (IRecipe recipe : ForgeRegistries.RECIPES.getValues())
		{
			if (ItemStack.areItemsEqualIgnoreDurability(recipe.getRecipeOutput(), analyzed))
			{
				for (Ingredient i : recipe.getIngredients())
				{
					for (ItemStack ingredientStack : i.getMatchingStacks())
					{
						boolean shouldIgnore = false;

						for (ItemStack ignored : ignore)
						{
							if (ItemStack.areItemsEqualIgnoreDurability(ingredientStack, ignored))
							{
								shouldIgnore = true;
								break;
							}
						}

						if (!shouldIgnore)
						{
							boolean didAdd = false;
							for (ItemStack cached : ANALYZED_ITEMS.keySet())
							{
								System.out.println("comparing cached stack " + cached.toString() + "with "
										+ ingredientStack.toString());
								if (OreDictionary.itemMatches(cached,ingredientStack, false))
								{
									ret.addAll(ANALYZED_ITEMS.get(cached));
									didAdd = true;
								}
							}

							if (!didAdd)
							{
								System.out.println("adding everytihing for " + ingredientStack.toString());
								ignore.add(analyzed);
								ret.addAll(getFromAnalysis(ingredientStack, ignore));
							}
						}
					}
				}
			}
		}
		for (ItemStack in : FurnaceRecipes.instance().getSmeltingList().keySet())
		{
			ItemStack out = FurnaceRecipes.instance().getSmeltingResult(in);
			// this recipe produces the thing you are analyzing
			if (ItemStack.areItemsEqualIgnoreDurability(out, analyzed))
			{
				System.out.println("ah i see");

				for (ItemStack i : ANALYSIS_ITEMS.keySet())
				{
					if (OreDictionary.itemMatches(in, i, false))
					{
						System.out.println("oh look " + i.toString() + " is literally right there on the list");
						ret.add(ANALYSIS_ITEMS.get(i));
					}
				}

				boolean alreadyAnalyzed = false;
				for (ItemStack i : ANALYZED_ITEMS.keySet())
				{
					if (OreDictionary.itemMatches(in, i, false))
					{
						alreadyAnalyzed = true;
						System.out.println("oh look " + i.toString() + " has already been analyzed");
						ret.addAll(ANALYZED_ITEMS.get(i));
					}
				}

				if (!alreadyAnalyzed)
				{
					System.out.println("ah we will have to analyze it again");
					ret.addAll(getFromAnalysis(in, ignore));
				}
			}
		}
		// cache the thing!
		ANALYZED_ITEMS.put(analyzed, ret);

		System.out.println("CACHED NEW ENTRY: " + analyzed.toString() + " as " + ret);
		return ret;

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

	public static IElementalRecipe getElementalCraftingRecipe(ItemStack wand, NonNullList<ItemStack> inputs,
			World world)
	{
		Preconditions.checkArgument(inputs.size() == 9);
		Preconditions.checkArgument(wand.hasTagCompound() && wand.hasCapability(IEssenceStorage.CAP, null));
		for (IElementalRecipe curRecipe : RecipeHelper.ELEMENTAL_RECIPES)
			if (curRecipe.matches(wand, inputs, world))
				return curRecipe;
		return null;
	}
}
