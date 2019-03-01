package com.raphydaphy.arcanemagic.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Set;

public class ShapedTransfigurationRecipeSerializer implements RecipeSerializer<ShapedTransfigurationRecipe>
{
	public ShapedTransfigurationRecipeSerializer()
	{

	}

	@Override
	public ShapedTransfigurationRecipe read(Identifier identifier_1, JsonObject jsonObject_1)
	{
		Map<String, Ingredient> map_1 = deserializeComponents(JsonHelper.getObject(jsonObject_1, "key"));
		String[] strings_1 = method_8146(deserializePattern(JsonHelper.getArray(jsonObject_1, "pattern")));
		int width = strings_1[0].length();
		int height = strings_1.length;
		DefaultedList<Ingredient> inputs = method_8148(strings_1, map_1, width, height);
		ItemStack output = deserializeItemStack(JsonHelper.getObject(jsonObject_1, "result"));
		return new ShapedTransfigurationRecipe(identifier_1, output, inputs, 20, width, height);
	}

	@Override
	public ShapedTransfigurationRecipe read(Identifier id, PacketByteBuf packetByteBuf_1)
	{
		int width = packetByteBuf_1.readVarInt();
		int height = packetByteBuf_1.readVarInt();
		DefaultedList<Ingredient> inputs = DefaultedList.create(width * height, Ingredient.EMPTY);

		for (int int_3 = 0; int_3 < inputs.size(); ++int_3)
		{
			inputs.set(int_3, Ingredient.fromPacket(packetByteBuf_1));
		}

		ItemStack output = packetByteBuf_1.readItemStack();
		return new ShapedTransfigurationRecipe(id, output, inputs, 20, width, height);
	}

	@Override
	public void write(PacketByteBuf buf, ShapedTransfigurationRecipe shapedRecipe_1)
	{
		buf.writeVarInt(shapedRecipe_1.width);
		buf.writeVarInt(shapedRecipe_1.height);

		for (Ingredient ingredient : shapedRecipe_1.inputs)
		{
			ingredient.write(buf);
		}

		buf.writeItemStack(shapedRecipe_1.output);
	}
	private static DefaultedList<Ingredient> method_8148(String[] strings_1, Map<String, Ingredient> map_1, int int_1, int int_2)
	{
		DefaultedList<Ingredient> defaultedList_1 = DefaultedList.create(int_1 * int_2, Ingredient.EMPTY);
		Set<String> set_1 = Sets.newHashSet(map_1.keySet());
		set_1.remove(" ");

		for (int int_3 = 0; int_3 < strings_1.length; ++int_3)
		{
			for (int int_4 = 0; int_4 < strings_1[int_3].length(); ++int_4)
			{
				String string_1 = strings_1[int_3].substring(int_4, int_4 + 1);
				Ingredient ingredient_1 = (Ingredient) map_1.get(string_1);
				if (ingredient_1 == null)
				{
					throw new JsonSyntaxException("Pattern references symbol '" + string_1 + "' but it's not defined in the key");
				}

				set_1.remove(string_1);
				defaultedList_1.set(int_4 + int_1 * int_3, ingredient_1);
			}
		}

		if (!set_1.isEmpty())
		{
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set_1);
		} else
		{
			return defaultedList_1;
		}
	}

	@VisibleForTesting
	static String[] method_8146(String... strings_1)
	{
		int int_1 = Integer.MAX_VALUE;
		int int_2 = 0;
		int int_3 = 0;
		int int_4 = 0;

		for (int int_5 = 0; int_5 < strings_1.length; ++int_5)
		{
			String string_1 = strings_1[int_5];
			int_1 = Math.min(int_1, findNonEmpty(string_1));
			int int_6 = findNonEmptyReverse(string_1);
			int_2 = Math.max(int_2, int_6);
			if (int_6 < 0)
			{
				if (int_3 == int_5)
				{
					++int_3;
				}

				++int_4;
			} else
			{
				int_4 = 0;
			}
		}

		if (strings_1.length == int_4)
		{
			return new String[0];
		} else
		{
			String[] strings_2 = new String[strings_1.length - int_4 - int_3];

			for (int int_7 = 0; int_7 < strings_2.length; ++int_7)
			{
				strings_2[int_7] = strings_1[int_7 + int_3].substring(int_1, int_2 + 1);
			}

			return strings_2;
		}
	}

	private static int findNonEmpty(String in)
	{
		int position;
		for (position = 0; position < in.length() && in.charAt(position) == ' '; ++position)
		{
		}

		return position;
	}

	private static int findNonEmptyReverse(String in)
	{
		int position;
		for (position = in.length() - 1; position >= 0 && in.charAt(position) == ' '; --position)
		{
		}

		return position;
	}

	private static String[] deserializePattern(JsonArray array)
	{
		String[] strings_1 = new String[array.size()];
		if (strings_1.length > 3)
		{
			throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
		} else if (strings_1.length == 0)
		{
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		} else
		{
			for (int int_1 = 0; int_1 < strings_1.length; ++int_1)
			{
				String string_1 = JsonHelper.asString(array.get(int_1), "pattern[" + int_1 + "]");
				if (string_1.length() > 3)
				{
					throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
				}

				if (int_1 > 0 && strings_1[0].length() != string_1.length())
				{
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}

				strings_1[int_1] = string_1;
			}

			return strings_1;
		}
	}

	private static Map<String, Ingredient> deserializeComponents(JsonObject object)
	{
		Map<String, Ingredient> ingredients = Maps.newHashMap();

		for (Map.Entry<String, JsonElement> entry : object.entrySet())
		{
			if ((entry.getKey()).length() != 1)
			{
				throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey()))
			{
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}

			ingredients.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
		}

		ingredients.put(" ", Ingredient.EMPTY);
		return ingredients;
	}

	private static ItemStack deserializeItemStack(JsonObject object)
	{
		String id = JsonHelper.getString(object, "item");
		Item item = Registry.ITEM.getOrEmpty(new Identifier(id)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + id + "'"));
		if (object.has("data"))
		{
			throw new JsonParseException("Disallowed data tag found");
		} else
		{
			int int_1 = JsonHelper.getInt(object, "count", 1);
			return new ItemStack(item, int_1);
		}
	}
}