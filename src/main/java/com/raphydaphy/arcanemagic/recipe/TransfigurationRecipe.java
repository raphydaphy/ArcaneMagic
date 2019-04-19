package com.raphydaphy.arcanemagic.recipe;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;

public interface TransfigurationRecipe extends Recipe<Inventory> {
    RecipeType<TransfigurationRecipe> TYPE = RecipeType.register(ArcaneMagic.PREFIX + "transfiguration");

    /**
     * @return A list of the ingredients needed for the recipe
     */
    DefaultedList<Ingredient> getIngredients();

    @Override
    default DefaultedList<Ingredient> getPreviewInputs() {
        return getIngredients();
    }

    /**
     * @return The amount of soul needed to craft the item
     */
    int getSoul();

    @Override
    default boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    default RecipeType<?> getType() {
        return TYPE;
    }

    /**
     * Transfiguration Recipes are not crafted using this method since they require soul
     */
    @Override
    default ItemStack craft(Inventory var1) {
        return ItemStack.EMPTY;
    }
}
