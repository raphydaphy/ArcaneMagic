package com.raphydaphy.arcanemagic.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ShapelessTransfigurationRecipe implements TransfigurationRecipe {
    public static RecipeSerializer<ShapelessTransfigurationRecipe> SERIALIZER;
    final ItemStack output;
    final DefaultedList<Ingredient> inputs;
    final int soul;
    private final Identifier id;

    public ShapelessTransfigurationRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> inputs, int soul) {
        this.id = id;
        this.output = output;
        this.inputs = inputs;
        this.soul = soul;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        RecipeFinder finder = new RecipeFinder();
        int items = 0;

        for (int item = 0; item < inv.getInvSize(); ++item) {
            ItemStack stack = inv.getInvStack(item);
            if (!(stack.isEmpty() || stack.getItem() == Items.GLASS_PANE)) {
                ++items;
                finder.addItem(stack);
            }
        }

        return items == this.inputs.size() && finder.findRecipe(this, null);
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return inputs;
    }

    @Override
    public int getSoul() {
        return soul;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= inputs.size();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<ShapelessTransfigurationRecipe> getSerializer() {
        return SERIALIZER;
    }
}
