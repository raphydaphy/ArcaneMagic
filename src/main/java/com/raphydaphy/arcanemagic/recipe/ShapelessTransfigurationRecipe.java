package com.raphydaphy.arcanemagic.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ShapelessTransfigurationRecipe implements TransfigurationRecipe
{
	public static RecipeSerializer<ShapelessTransfigurationRecipe> SERIALIZER;

	private final Identifier id;
	final ItemStack output;
	final DefaultedList<Ingredient> inputs;
	final int soul;

	public ShapelessTransfigurationRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> inputs, int soul)
	{
		this.id = id;
		this.output = output;
		this.inputs = inputs;
		this.soul = soul;
	}

	@Override
	public boolean matches(Inventory inv, World world)
	{
		List<ItemStack> toCheck = new ArrayList<>();
		for (int slot = 0; slot < inv.getInvSize(); slot++)
		{
			toCheck.add(inv.getInvStack(slot));
		}
		for (Ingredient i : this.inputs)
		{
			for (int ix = 0; ix < toCheck.size(); ix++)
			{
				if (i == Ingredient.EMPTY && (toCheck.get(ix).isEmpty() || toCheck.get(ix).getItem() == Items.GLASS_PANE))
				{
					toCheck.remove(ix);
				} else if (i.method_8093(toCheck.get(ix))) // apply
				{
					toCheck.remove(ix);
					break;
				}
			}
		}
		return toCheck.isEmpty();
	}

	@Override
	public ItemStack getOutput()
	{
		return output;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients()
	{
		return inputs;
	}

	@Override
	public int getSoul()
	{
		return soul;
	}

	@Override
	public boolean fits(int width, int height)
	{
		return width * height >= inputs.size();
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public RecipeSerializer<ShapelessTransfigurationRecipe> getSerializer()
	{
		return SERIALIZER;
	}
}
