package com.raphydaphy.arcanemagic.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

import java.util.Map;
import java.util.Set;

public class ShapedTransfigurationRecipeSerializer implements RecipeSerializer<ShapedTransfigurationRecipe> {
    private static DefaultedList<Ingredient> patternToList(String[] pattern, Map<String, Ingredient> key, int width, int height) {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
        Set<String> ingredientNames = Sets.newHashSet(key.keySet());
        ingredientNames.remove(" ");

        for (int row = 0; row < pattern.length; ++row) {
            for (int col = 0; col < pattern[row].length(); ++col) {
                String symbol = pattern[row].substring(col, col + 1);
                Ingredient ingredient_1 = key.get(symbol);
                if (ingredient_1 == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + symbol + "' but it's not defined in the key");
                }

                ingredientNames.remove(symbol);
                ingredients.set(col + width * row, ingredient_1);
            }
        }

        if (!ingredientNames.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + ingredientNames);
        } else {
            return ingredients;
        }
    }

    private static String[] makePatternList(String... strings_1) {
        int int_1 = Integer.MAX_VALUE;
        int int_2 = 0;
        int int_3 = 0;
        int int_4 = 0;

        for (int int_5 = 0; int_5 < strings_1.length; ++int_5) {
            String string_1 = strings_1[int_5];
            int_1 = Math.min(int_1, findNonEmpty(string_1));
            int int_6 = findNonEmptyReverse(string_1);
            int_2 = Math.max(int_2, int_6);
            if (int_6 < 0) {
                if (int_3 == int_5) {
                    ++int_3;
                }

                ++int_4;
            } else {
                int_4 = 0;
            }
        }

        if (strings_1.length == int_4) {
            return new String[0];
        } else {
            String[] strings_2 = new String[strings_1.length - int_4 - int_3];

            for (int int_7 = 0; int_7 < strings_2.length; ++int_7) {
                strings_2[int_7] = strings_1[int_7 + int_3].substring(int_1, int_2 + 1);
            }

            return strings_2;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static int findNonEmpty(String in) {
        int position;
        for (position = 0; position < in.length() && in.charAt(position) == ' '; ++position) {
        }
        return position;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static int findNonEmptyReverse(String in) {
        int position;
        for (position = in.length() - 1; position >= 0 && in.charAt(position) == ' '; --position) {
        }
        return position;
    }

    private static Map<String, Ingredient> deserializeComponents(JsonObject object) {
        Map<String, Ingredient> ingredients = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if ((entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            ingredients.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        ingredients.put(" ", Ingredient.EMPTY);
        return ingredients;
    }

    private static String[] deserializePattern(JsonArray array) {
        String[] strings_1 = new String[array.size()];
        if (strings_1.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (strings_1.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int int_1 = 0; int_1 < strings_1.length; ++int_1) {
                String string_1 = JsonHelper.asString(array.get(int_1), "pattern[" + int_1 + "]");
                if (string_1.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (int_1 > 0 && strings_1[0].length() != string_1.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings_1[int_1] = string_1;
            }

            return strings_1;
        }
    }

    @Override
    public ShapedTransfigurationRecipe read(Identifier id, JsonObject object) {
        Map<String, Ingredient> ingredients = deserializeComponents(JsonHelper.getObject(object, "key"));
        String[] strings_1 = makePatternList(deserializePattern(JsonHelper.getArray(object, "pattern")));
        int width = strings_1[0].length();
        int height = strings_1.length;
        DefaultedList<Ingredient> inputs = patternToList(strings_1, ingredients, width, height);
        ItemStack output = ArcaneMagicUtils.deserializeItemStack(JsonHelper.getObject(object, "result"));
        int soul = JsonHelper.getInt(object, "soul");
        return new ShapedTransfigurationRecipe(id, output, inputs, soul, width, height);
    }

    @Override
    public ShapedTransfigurationRecipe read(Identifier id, PacketByteBuf buf) {
        int soul = buf.readVarInt();

        int width = buf.readVarInt();
        int height = buf.readVarInt();

        DefaultedList<Ingredient> inputs = DefaultedList.ofSize(width * height, Ingredient.EMPTY);

        for (int int_3 = 0; int_3 < inputs.size(); ++int_3) {
            inputs.set(int_3, Ingredient.fromPacket(buf));
        }

        ItemStack output = buf.readItemStack();

        return new ShapedTransfigurationRecipe(id, output, inputs, soul, width, height);
    }

    @Override
    public void write(PacketByteBuf buf, ShapedTransfigurationRecipe recipe) {
        buf.writeVarInt(recipe.soul);

        buf.writeVarInt(recipe.width);
        buf.writeVarInt(recipe.height);

        for (Ingredient ingredient : recipe.inputs) {
            ingredient.write(buf);
        }

        buf.writeItemStack(recipe.output);
    }
}