package com.raphydaphy.arcanemagic.intergration;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import me.shedaniel.rei.api.IRecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.*;

public class InfusionDisplay implements IRecipeDisplay
{
	private static final List<ItemStack> scepters = Collections.singletonList(new ItemStack(ModRegistry.GOLDEN_SCEPTER));
	private List<List<ItemStack>> input;
	private List<ItemStack> output;
	private int soul;

	public InfusionDisplay(ItemStack catalyst, ItemStack output, int soul)
	{
		this.input = new ArrayList<>();
		input.add(scepters);
		input.add(Collections.singletonList(catalyst));
		this.output = Collections.singletonList(output);
		this.soul = soul;
	}

	public int getSoul()
	{
		return soul;
	}

	@Override
	public Optional getRecipe()
	{
		return Optional.empty();
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
		return REIPlugin.INFUSION;
	}
}
