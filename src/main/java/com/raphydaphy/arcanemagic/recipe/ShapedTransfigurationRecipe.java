package com.raphydaphy.arcanemagic.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ShapedTransfigurationRecipe implements TransfigurationRecipe
{
	public static RecipeSerializer<ShapedTransfigurationRecipe> SERIALIZER;

	private final Identifier id;
	final ItemStack output;
	final DefaultedList<Ingredient> inputs;
	final int soul;
	final int width;
	final int height;

	public ShapedTransfigurationRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> inputs, int soul, int width, int height)
	{
		this.id = id;
		this.output = output;
		this.inputs = inputs;
		this.soul = soul;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean matches(Inventory inventory, World world)
	{
		for (int int_1 = 0; int_1 <= 3 - this.width; ++int_1)
		{
			for (int int_2 = 0; int_2 <= 3 - this.height; ++int_2)
			{
				if (this.matchesSmall(inventory, int_1, int_2, true))
				{
					return true;
				}

				if (this.matchesSmall(inventory, int_1, int_2, false))
				{
					return true;
				}
			}
		}

		return false;
	}

	private boolean matchesSmall(Inventory inv, int offsetX, int offsetY, boolean something)
	{
		for (int row = 0; row < 3; ++row)
		{
			for (int col = 0; col < 3; ++col)
			{
				int posX = row - offsetX;
				int posY = col - offsetY;
				Ingredient ingredient = Ingredient.EMPTY;
				if (posX >= 0 && posY >= 0 && posX < this.width && posY < this.height)
				{
					if (something)
					{
						ingredient = this.inputs.get(this.width - posX - 1 + posY * this.width);
					} else
					{
						ingredient = this.inputs.get(posX + posY * this.width);
					}
				}

				ItemStack invStack = inv.getInvStack(row + col * 3);

				if (!(ingredient.isEmpty() && (invStack.isEmpty() || invStack.getItem() == Items.GLASS_PANE)) && !ingredient.method_8093(invStack)) // apply
				{
					return false;
				}
			}
		}

		return true;
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
		return width >= this.width && height >= this.height;
	}

	@Override
	public ItemStack getOutput()
	{
		return output;
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public RecipeSerializer<ShapedTransfigurationRecipe> getSerializer()
	{
		return SERIALIZER;
	}
}
