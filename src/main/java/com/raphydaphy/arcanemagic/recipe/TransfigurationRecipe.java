package com.raphydaphy.arcanemagic.recipe;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public class TransfigurationRecipe
{
	private final ItemStack output;
	private final int soul;
	private final ItemStack[] recipe;

	public TransfigurationRecipe(ItemStack output, int soul, ItemStack... recipeIn)
	{
		ItemStack[] safeRecipe = new ItemStack[9];

		for (int i = 0; i < 9; i++)
		{
			if (i < recipeIn.length && recipeIn[i] != null)
			{
				safeRecipe[i] = recipeIn[i];
			} else
			{
				safeRecipe[i] = ItemStack.EMPTY;
			}
		}

		this.output = output == null ? ItemStack.EMPTY : output;
		this.soul = soul;
		this.recipe = safeRecipe;

		if (output != null && !output.isEmpty())
		{
			ModRegistry.TRANSFIGURATION_RECIPES.add(this);
		} else
		{
			ArcaneMagic.getLogger().error("Tried to register transfiguration recipe with empty output!");
		}
	}

	public boolean matches(DefaultedList<ItemStack> table)
	{
		for (int item = 0; item < 9; item++)
		{
			if (!table.get(item).isEqualIgnoreDurability(recipe[item]))
			{
				if (recipe[item].isEmpty() && (table.get(item).isEmpty() || table.get(item).getItem() == Blocks.GLASS_PANE.getItem()))
				{
					continue;
				}
				return false;
			}
		}

		return true;
	}

	public int getSoul()
	{
		return soul;
	}

	public ItemStack getOutput()
	{
		return output.copy();
	}
}
