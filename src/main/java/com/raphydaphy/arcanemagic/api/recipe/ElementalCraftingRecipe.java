package com.raphydaphy.arcanemagic.api.recipe;

import net.minecraft.item.ItemStack;

public class ElementalCraftingRecipe
{
	private ItemStack[][] recipeIn;
	private ItemStack recipeOut;

	public ElementalCraftingRecipe(ItemStack[][] in, ItemStack out)
	{
		this.recipeIn = in;
		this.recipeOut = out;
	}

	public ItemStack getOutput()
	{
		return recipeOut.copy();
	}
	
	public ItemStack[][] getInput()
	{
		return recipeIn.clone();
	}

	public boolean inputMatches(ItemStack[][] input)
	{
		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				ItemStack tableIn = input[x][y];
				ItemStack realIn = recipeIn[x][y];
				if (realIn != null && !realIn.isEmpty())
				{
					if (!realIn.getItem().equals(tableIn.getItem()))
					{
						System.out.println("Item no matchy!");
						return false;
					}
					if (realIn.getItemDamage() != tableIn.getItemDamage())
					{
						System.out.println("Damage no matchy!");
						return false;
					}
					if (realIn.getCount() > tableIn.getCount())
					{
						System.out.println("Count too low!");
						return false;
					}
				}
			}
		}
		return true;
	}
}
