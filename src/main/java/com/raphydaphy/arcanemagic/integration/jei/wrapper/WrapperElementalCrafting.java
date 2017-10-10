package com.raphydaphy.arcanemagic.integration.jei.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class WrapperElementalCrafting implements ICustomCraftingRecipeWrapper {

	private final IElementalRecipe recipe;

	public WrapperElementalCrafting(IElementalRecipe rec) {
		recipe = rec;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		List<List<ItemStack>> inputs = new ArrayList<>();
		for (Ingredient i : recipe.getIngredients())
			inputs.add(Arrays.asList(i.getMatchingStacks()));
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IIngredients input) {
		if (recipe.isShapeless()) layout.setShapeless();
	}

	@Override
	public void drawInfo(Minecraft mc, int width, int height, int mouseX, int mouseY) {
		//TODO draw essence stack in here or something
	}

}