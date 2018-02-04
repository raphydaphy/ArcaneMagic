package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.IAnimaCrystal;
import com.raphydaphy.arcanemagic.common.data.EnumBasicAnimus;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ItemAnimaCrystal extends ItemEnum<EnumBasicAnimus> implements IAnimaCrystal {
	public ItemAnimaCrystal() {
		super("anima", EnumBasicAnimus.values(), TextFormatting.DARK_AQUA);
	}

	@Override
	public Anima getAnima(ItemStack stack) {
		return EnumBasicAnimus.values()[stack.getMetadata()].getAnima();
	}

}
