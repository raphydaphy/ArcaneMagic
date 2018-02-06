package com.raphydaphy.arcanemagic.common.integration;

import com.raphydaphy.arcanemagic.api.recipe.IArcaneTransfigurationRecipe;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.integration.jei.category.CategoryArcaneTransfiguration;
import com.raphydaphy.arcanemagic.common.integration.jei.wrapper.WrapperArcaneTransfiguration;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class ArcaneMagicJEIPlugin implements IModPlugin
{
	public static final String ARCANE_TRANSFIGURATION_UID = "arcanemagic.arcane_transfiguration";

	public void register(IModRegistry registry)
	{
		registry.addRecipes(RecipeHelper.ELEMENTAL_RECIPES, ARCANE_TRANSFIGURATION_UID);
		registry.handleRecipes(IArcaneTransfigurationRecipe.class, WrapperArcaneTransfiguration::new,
				ARCANE_TRANSFIGURATION_UID);
		registry.addRecipeCatalyst(new ItemStack(ModRegistry.ARCANE_TRANSFIGURATION_TABLE), ARCANE_TRANSFIGURATION_UID);
		registry.addIngredientInfo(new ItemStack(ModRegistry.ANCIENT_PARCHMENT), ItemStack.class,
				"desc.arcanemagic.ancient_parchment");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new CategoryArcaneTransfiguration(guiHelper));
	}

}
