package com.raphydaphy.arcanemagic.api.anima;

import java.util.Map;

import net.minecraft.item.ItemStack;

public interface IItemAnimaAcceptor
{
	/*
	 * Returns a map of each anima type to the amount currently stored Can
	 * support more or less animus types in a wand by changing the size of the
	 * map.
	 */
	public Map<Anima, Integer> getAnimaStored(ItemStack stack);

	/* Returns the maximum amount of anima that can be stored for each type. */
	public Map<Anima, Integer> getAnimaCapacity(ItemStack stack);
}
