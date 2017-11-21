package com.raphydaphy.arcanemagic.api.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public interface IElementalCraftingItem
{

	/**
	 * Use this to decide if you actually can craft this recipe.  If you return false, the recipe will not match. Return true, and the recipe will continue to its own check.
	 * @param recipe The current recipe
	 * @param player The player
	 * @param wand The wand itemstack
	 * @param stacks The current list of stacks in the elemental crafter
	 * @param world The world
	 * @return If this item can craft this recipe
	 */
	default public boolean matches(IElementalRecipe recipe, EntityPlayer player, ItemStack wand,
			NonNullList<ItemStack> stacks, World world)
	{
		return true;
	}

	/**
	 * This method must return false if you do not have the IAnimaStorage capability.
	 * @return If this item has animus inside.
	 */
	public boolean containsAnimus();

}
