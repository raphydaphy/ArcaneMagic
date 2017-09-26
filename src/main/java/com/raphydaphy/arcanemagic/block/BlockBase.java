package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IBaseBlock
{
	public BlockBase(String name, Material material, float hardness)
	{
		super(material);
		setUnlocalizedName(Thaumcraft.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(Thaumcraft.creativeTab);
		setHardness(hardness);
		init();
	}

	@Override
	public void init()
	{
		ModRegistry.BLOCKS.add(this);
		ModRegistry.ITEMS.add(createItemBlock());
	}

	@Override
	public ItemBlock createItemBlock()
	{
		return (ItemBlock) new ItemBlock(this).setRegistryName(getRegistryName());
	}
}
