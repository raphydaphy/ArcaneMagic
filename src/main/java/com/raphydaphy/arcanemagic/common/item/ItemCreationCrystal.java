package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.IAnimaCrystal;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ItemCreationCrystal extends ItemBase implements IAnimaCrystal {

	public ItemCreationCrystal() {
		super("anima_creation", TextFormatting.GOLD);
	}

	@Override
	public Anima getAnima(ItemStack stack) {
		return Anima.CREATION;
	}

}
