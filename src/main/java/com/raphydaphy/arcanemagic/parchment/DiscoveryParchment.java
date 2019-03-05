package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.api.parchment.IParchment;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscoveryParchment implements IParchment
{
	public static final String NAME = "progression.arcanemagic.discovery";

	private int drownedKills = 0;

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getText()
	{
		String base = "parchment.arcanemagic.discovery";
		if (drownedKills < 5)
		{
			return base + ".kill_quest";
		}
		return base + ".gather_new";
	}

	@Override
	public void init(ItemStack stack)
	{
		drownedKills = ((DataHolder)MinecraftClient.getInstance().player).getAdditionalData().getInt(ArcaneMagicConstants.DROWNED_KILLS_KEY);;
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
		return drownedKills < 5;
	}

	@Override
	public double getProgressPercent()
	{
		return (drownedKills - 1) / 4d;
	}

	public List<Ingredient> getRequiredItems()
	{
		return drownedKills < 5 ? Collections.emptyList() : Arrays.asList(Ingredient.ofStacks(new ItemStack(Items.NAUTILUS_SHELL)), Ingredient.ofStacks(new ItemStack(Items.CROSSBOW)), Ingredient.ofStacks(new ItemStack(Items.BUBBLE_CORAL), new ItemStack(Items.BRAIN_CORAL), new ItemStack(Items.FIRE_CORAL), new ItemStack(Items.TUBE_CORAL), new ItemStack(Items.HORN_CORAL)));
	}
}
