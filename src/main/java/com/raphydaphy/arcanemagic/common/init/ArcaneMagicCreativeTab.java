package com.raphydaphy.arcanemagic.common.init;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ArcaneMagicCreativeTab extends CreativeTabs {
	public ArcaneMagicCreativeTab() {
		super(ArcaneMagic.MODID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModRegistry.NOTEBOOK);
	}
}