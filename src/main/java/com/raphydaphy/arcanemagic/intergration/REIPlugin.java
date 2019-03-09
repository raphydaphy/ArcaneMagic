package com.raphydaphy.arcanemagic.intergration;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.recipe.ShapedTransfigurationRecipe;
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import me.shedaniel.rei.api.IRecipePlugin;
import me.shedaniel.rei.api.ItemRegisterer;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

public class REIPlugin implements IRecipePlugin
{
	static final Identifier TRANSFIGURATION = new Identifier(ArcaneMagic.DOMAIN, "plugins/transfiguration");

	@Override
	public void registerItems(ItemRegisterer itemRegisterer)
	{

	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper)
	{
		recipeHelper.registerCategory(new TransfigurationCategory());
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper)
	{
		for (Recipe recipe : recipeHelper.getRecipeManager().values())
		{
			if (recipe instanceof ShapedTransfigurationRecipe)
			{
				recipeHelper.registerDisplay(TRANSFIGURATION, new TransfigurationDisplay((ShapedTransfigurationRecipe)recipe));
			}
		}
	}

	@Override
	public void registerSpeedCraft(RecipeHelper recipeHelper)
	{
		recipeHelper.registerSpeedCraftButtonArea(TRANSFIGURATION, null);
	}
}
