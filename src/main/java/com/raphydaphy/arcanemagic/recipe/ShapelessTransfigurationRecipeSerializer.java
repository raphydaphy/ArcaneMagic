package com.raphydaphy.arcanemagic.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

public class ShapelessTransfigurationRecipeSerializer implements RecipeSerializer<ShapelessTransfigurationRecipe> {

    private static DefaultedList<Ingredient> parseIngredientArray(JsonArray array) {
        DefaultedList<Ingredient> ingredients = DefaultedList.create();

        for (int int_1 = 0; int_1 < array.size(); ++int_1) {
            Ingredient ingredient_1 = Ingredient.fromJson(array.get(int_1));
            if (!ingredient_1.isEmpty()) {
                ingredients.add(ingredient_1);
            }
        }

        return ingredients;
    }

    @Override
    public ShapelessTransfigurationRecipe read(Identifier id, JsonObject object) {
        DefaultedList<Ingredient> ingredients = parseIngredientArray(JsonHelper.getArray(object, "ingredients"));
        if (ingredients.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (ingredients.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        } else {
            ItemStack output = ArcaneMagicUtils.deserializeItemStack(JsonHelper.getObject(object, "result"));
            int soul = JsonHelper.getInt(object, "soul");
            return new ShapelessTransfigurationRecipe(id, output, ingredients, soul);
        }
    }

    @Override
    public ShapelessTransfigurationRecipe read(Identifier id, PacketByteBuf buf) {
        int soul = buf.readVarInt();
        int size = buf.readVarInt();

        DefaultedList<Ingredient> inputs = DefaultedList.create(size, Ingredient.EMPTY);

        for (int input = 0; input < inputs.size(); ++input) {
            inputs.set(input, Ingredient.fromPacket(buf));
        }

        ItemStack output = buf.readItemStack();
        return new ShapelessTransfigurationRecipe(id, output, inputs, soul);
    }

    @Override
    public void write(PacketByteBuf buf, ShapelessTransfigurationRecipe recipe) {
        buf.writeVarInt(recipe.soul);
        buf.writeVarInt(recipe.inputs.size());
        for (Ingredient ingredient : recipe.inputs) {
            ingredient.write(buf);
        }
        buf.writeItemStack(recipe.output);
    }
}
