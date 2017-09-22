package com.raphydaphy.thaumcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockArcaneWorktable extends BlockBase
{
	public BlockArcaneWorktable()
	{
		super("arcane_worktable", Material.WOOD, 2.5f);
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
