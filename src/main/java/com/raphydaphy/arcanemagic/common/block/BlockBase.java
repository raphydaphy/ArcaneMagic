package com.raphydaphy.arcanemagic.common.block;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IBaseBlock
{
	public BlockBase(String name, Material material, float hardness)
	{
		super(material);
		setUnlocalizedName(ArcaneMagic.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(ArcaneMagic.creativeTab);
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
