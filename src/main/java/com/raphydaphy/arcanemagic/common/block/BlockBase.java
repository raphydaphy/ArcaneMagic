package com.raphydaphy.arcanemagic.common.block;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IBaseBlock
{
	public BlockBase(String name, Material material, float hardness, float resist, SoundType sound)
	{
		super(material);
		setup(name, hardness, resist, sound);
		init();
	}

	public BlockBase(String name, Material material, float hardness, SoundType sound)
	{
		this(name, material, hardness, hardness * (5 / 3), sound);
	}

	@Override
	public void setup(String name, float hardness, float resist, SoundType sound)
	{
		setUnlocalizedName(ArcaneMagic.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(ArcaneMagic.creativeTab);
		setHardness(hardness);
		setResistance(resist);
		setSoundType(sound);
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
