package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.parchment.IParchment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

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
		return manager.get(new Identifier(ArcaneMagic.DOMAIN, "golden_scepter")).orElse(null);
	}
}
