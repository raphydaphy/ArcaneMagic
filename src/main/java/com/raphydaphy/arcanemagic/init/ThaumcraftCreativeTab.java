package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.Thaumcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ThaumcraftCreativeTab extends CreativeTabs
{
	public ThaumcraftCreativeTab()
	{
		super(Thaumcraft.MODID);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModRegistry.THAUMONOMICON);
	}
}