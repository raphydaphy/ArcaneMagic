package com.raphydaphy.arcanemagic.intergration;

import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import me.shedaniel.rei.api.IRecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.*;

public class TransfigurationDisplay implements IRecipeDisplay
{
	private TransfigurationRecipe recipe;
	private List<List<ItemStack>> input;
	private List<ItemStack> output;
	private int soul;

	public TransfigurationDisplay(TransfigurationRecipe recipe)
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

	public int getSoul()
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
