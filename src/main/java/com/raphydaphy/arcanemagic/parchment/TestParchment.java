package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.parchment.IParchment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestParchment implements IParchment
{
	public static final String NAME = "progression.arcanemagic.test_parchment";

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getText()
	{
		return "parchment.arcanemagic.test";
	}

	@Override
	public Recipe<? extends Inventory> getRecipe()
	{
		RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
		//return manager.get(new Identifier(ArcaneMagic.DOMAIN, "notebook")).orElse(null);
		return null;
	}


	@Override
	public boolean showProgressBar()
	{
		return false;
	}

	@Override
	public double getProgressPercent()
	{
		return 0;
	}

	public List<Ingredient> getRequiredItems()
	{
		return Arrays.asList(Ingredient.ofStacks(new ItemStack(Items.NAUTILUS_SHELL)), Ingredient.ofStacks(new ItemStack(Items.CROSSBOW)), Ingredient.ofStacks(new ItemStack(Items.BUBBLE_CORAL), new ItemStack(Items.BRAIN_CORAL), new ItemStack(Items.FIRE_CORAL), new ItemStack(Items.TUBE_CORAL), new ItemStack(Items.HORN_CORAL)));
	}
}
