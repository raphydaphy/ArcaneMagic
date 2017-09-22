package com.raphydaphy.thaumcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockOre extends BlockBase
{
	public BlockOre(String name, float hardness) 
	{
		super(name, Material.ROCK, hardness);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) 
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) 
	{
		return false;
	}
}
