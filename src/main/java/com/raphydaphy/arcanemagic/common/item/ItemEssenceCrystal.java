package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.IEssenceCrystal;
import com.raphydaphy.arcanemagic.common.data.EnumBasicEssence;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ItemEssenceCrystal extends ItemEnum<EnumBasicEssence> implements IEssenceCrystal
{
	public ItemEssenceCrystal()
	{
		super("essence", EnumBasicEssence.values(), TextFormatting.DARK_AQUA);
	}

	@Override
	public Essence getEssence(ItemStack stack)
	{
		return EnumBasicEssence.values()[stack.getMetadata()].getEssence();
	}

}
