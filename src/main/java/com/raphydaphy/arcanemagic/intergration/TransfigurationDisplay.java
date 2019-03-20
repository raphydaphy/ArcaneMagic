package com.raphydaphy.arcanemagic.intergration;
/*
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.*;

public class TransfigurationDisplay implements RecipeDisplay
{
	private TransfigurationRecipe recipe;
	private List<List<ItemStack>> input;
	private List<ItemStack> output;
	private int soul;

	TransfigurationDisplay(TransfigurationRecipe recipe)
	{
		this.recipe = recipe;
		input = new ArrayList<>();
		for (Ingredient ingredient : recipe.getIngredients())
		{
			input.add(Arrays.asList(ingredient.getStackArray()));
		}
		this.output = Collections.singletonList(recipe.getOutput());
		this.soul = recipe.getSoul();
	}

	int getSoul()
	{
		return soul;
	}

	@Override
	public Optional getRecipe()
	{
		return Optional.of(recipe);
	}

	@Override
	public List<List<ItemStack>> getInput()
	{
		return input;
	}

	@Override
	public List<ItemStack> getOutput()
	{
		return output;
	}

	@Override
	public Identifier getRecipeCategory()
	{
		return ArcaneMagicREIPlugin.TRANSFIGURATION;
	}
}
*/