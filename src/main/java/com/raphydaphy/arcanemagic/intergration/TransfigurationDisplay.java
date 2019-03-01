package com.raphydaphy.arcanemagic.intergration;

import me.shedaniel.rei.api.IRecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TransfigurationDisplay implements IRecipeDisplay
{
	private TransfigurationRecipe recipe;
	private List<List<ItemStack>> input;
	private List<ItemStack> output;
	private int soul;

	TransfigurationDisplay(TransfigurationRecipe recipe)
	{
		this.recipe = recipe;
		input = new ArrayList<>();
		for (ItemStack stack : recipe.getInputs())
		{
			input.add(Collections.singletonList(stack));
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
		return REIPlugin.TRANSFIGURATION;
	}
}
