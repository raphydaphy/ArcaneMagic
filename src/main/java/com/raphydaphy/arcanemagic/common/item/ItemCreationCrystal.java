package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.IEssenceCrystal;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ItemCreationCrystal extends ItemBase implements IEssenceCrystal
{

	public ItemCreationCrystal()
	{
		super("essence_creation", TextFormatting.GOLD);
	}

	@Override
	public Essence getEssence(ItemStack stack)
	{
		return Essence.CREATION;
	}

}
