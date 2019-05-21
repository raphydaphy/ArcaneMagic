package com.raphydaphy.arcanemagic.integration;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.recipe.ShapedTransfigurationRecipe;
import me.shedaniel.rei.api.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;

public class ArcaneMagicREIPlugin implements REIPluginEntry {
    static final Identifier TRANSFIGURATION = new Identifier(ArcaneMagic.DOMAIN, "plugins/transfiguration");

    @Override
    public Identifier getPluginIdentifier() {
        return new Identifier(ArcaneMagic.DOMAIN, "rei_plugin");
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new TransfigurationCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        for (Recipe recipe : recipeHelper.getRecipeManager().values()) {
            if (recipe instanceof ShapedTransfigurationRecipe) {
                recipeHelper.registerDisplay(TRANSFIGURATION, new TransfigurationDisplay((ShapedTransfigurationRecipe) recipe));
            }
        }
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipeVisibilityHandler((recipeCategory, recipeDisplay) ->
        {
            MinecraftClient client = MinecraftClient.getInstance();
            if (recipeDisplay.getRecipe().isPresent()) {
                Recipe<?> recipe = (Recipe<?>) recipeDisplay.getRecipe().get();
                if (recipe.getId().getNamespace().equals(ArcaneMagic.DOMAIN)) {
                    if (recipe instanceof CraftingRecipe) {
                        return (!recipe.isIgnoredInRecipeBook() && !client.player.getRecipeBook().contains(recipe)) ? DisplayVisibility.NEVER_VISIBLE : DisplayVisibility.ALWAYS_VISIBLE;
                    }
                    return DisplayVisibility.NEVER_VISIBLE;
                }
            }
            return DisplayVisibility.PASS;
        });
    }
}
