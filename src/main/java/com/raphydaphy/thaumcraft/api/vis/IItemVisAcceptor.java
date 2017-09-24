package com.raphydaphy.thaumcraft.api.vis;

import java.util.Map;

import net.minecraft.item.ItemStack;

public interface IItemVisAcceptor
{
	/*
	 * Returns a map of each vis type to the amount currently stored
	 * Can support more or less vis types in a wand by changing the
	 * size of the map.
	 */
	public Map<Vis, Integer> getVisStored(ItemStack stack);
	
	/*
	 * Returns the maximum amount of vis that can be stored for each
	 * type.
	 */
	public Map<Vis, Integer> getVisCapacity(ItemStack stack);
	
	/*
	 * Returns a map of each vis type to the discount that should be
	 * applied for usage. Iron/Wood Wands return 1.1f for all
	 * types and Thaumium/Silverwood returns 0.9f
	 */
	public Map<Vis, Float> getVisDiscount(ItemStack stack);
}
