package com.raphydaphy.arcanemagic.integration;

import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.integration.jei.category.CategoryElementalCrafting;
import com.raphydaphy.arcanemagic.integration.jei.wrapper.WrapperElementalCrafting;
import com.raphydaphy.arcanemagic.util.RecipeHelper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class ArcaneMagicJEIPlugin implements IModPlugin
{
	public static final String ELEMENTAL_CRAFTING_UID = "arcanemagic.elementalcrafting";

	public void register(IModRegistry registry)
	{
		registry.addRecipes(RecipeHelper.ELEMENTAL_RECIPES, ELEMENTAL_CRAFTING_UID);
		registry.handleRecipes(IElementalRecipe.class, WrapperElementalCrafting::new, ELEMENTAL_CRAFTING_UID);
		registry.addRecipeCatalyst(new ItemStack(ModRegistry.ELEMENTAL_CRAFTING_TABLE), ELEMENTAL_CRAFTING_UID);
		registry.addIngredientInfo(new ItemStack(ModRegistry.ANCIENT_PARCHMENT), ItemStack.class,
				"desc.arcanemagic.ancient_parchment");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new CategoryElementalCrafting(guiHelper));
	}

}
