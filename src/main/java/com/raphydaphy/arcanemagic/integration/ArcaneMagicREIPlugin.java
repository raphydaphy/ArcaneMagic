package com.raphydaphy.arcanemagic.integration;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.recipe.ShapedTransfigurationRecipe;
import me.shedaniel.rei.api.*;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.plugin.crafting.DefaultCraftingDisplay;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ArcaneMagicREIPlugin implements REIPluginV0 {
    static final Identifier TRANSFIGURATION = new Identifier(ArcaneMagic.DOMAIN, "plugins/transfiguration");

    @Override
    public Identifier getPluginIdentifier() {
        return new Identifier(ArcaneMagic.DOMAIN, "rei_plugin");
    }

    @Override
    public SemanticVersion getMinimumVersion() throws VersionParsingException {
        return SemanticVersion.parse("3.0-pre");
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
        MinecraftClient client = MinecraftClient.getInstance();
        recipeHelper.registerRecipeVisibilityHandler((recipeCategory, recipeDisplay) ->
        {
            if (recipeDisplay instanceof DefaultCraftingDisplay) {
                Optional<Recipe<?>> optionalRecipe = ((DefaultCraftingDisplay)recipeDisplay).getOptionalRecipe();
                if (optionalRecipe.isPresent()) {
                    Recipe<?> recipe = optionalRecipe.get();
                    if (recipe.getId().getNamespace().equals(ArcaneMagic.DOMAIN)) {
                        return (!recipe.isIgnoredInRecipeBook() && !client.player.getRecipeBook().contains(recipe)) ? ActionResult.FAIL : ActionResult.SUCCESS;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
