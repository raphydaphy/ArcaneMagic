package com.raphydaphy.thaumcraft.init;

import com.raphydaphy.thaumcraft.Thaumcraft;

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
		return new ItemStack(ModItems.thaumonomicon);
	}
}