package com.raphydaphy.arcanemagic.api.essence;

import java.util.Map;

import net.minecraft.item.ItemStack;

public interface IItemEssenceAcceptor
{
	/* Returns a map of each essence type to the amount currently stored Can support
	 * more or less essence types in a wand by changing the size of the map. */
	public Map<Essence, Integer> getEssenceStored(ItemStack stack);

	/* Returns the maximum amount of essence that can be stored for each type. */
	public Map<Essence, Integer> getEssenceCapacity(ItemStack stack);
}
