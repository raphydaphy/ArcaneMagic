package com.raphydaphy.arcanemagic.intergration;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.recipe.ShapedTransfigurationRecipe;
import me.shedaniel.rei.api.ItemRegistry;
import me.shedaniel.rei.api.REIPlugin;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

public class ArcaneMagicREIPlugin implements REIPlugin
{
	static final Identifier TRANSFIGURATION = new Identifier(ArcaneMagic.DOMAIN, "plugins/transfiguration");

	@Override
	public void registerItems(ItemRegistry registry)
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
