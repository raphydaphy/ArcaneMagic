package com.raphydaphy.thaumcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockResearchTable extends BlockTable
{
	public static final PropertyInteger SIDE = PropertyInteger.create("side", 0, 1);
	public BlockResearchTable() 
	{
		super("research_table", Material.WOOD, 2.5f, "axe", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SIDE, 0));
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
		if (meta % 2 == 0)
		{
			return this.getDefaultState().withProperty(SIDE, 0).withProperty(FACING, EnumFacing.getHorizontal((int) (Math.ceil(((meta + 1) / 2) - 1))));
		}
		else
		{
			return this.getDefaultState().withProperty(SIDE, 1).withProperty(FACING, EnumFacing.getHorizontal((int) (Math.ceil(((meta + 1) / 2) - 1))));
		}
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
    	if (state.getValue(SIDE) == 0)
    	{
    		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex() * 2;
    	}
    	else
    	{
    		return (((EnumFacing)state.getValue(FACING)).getHorizontalIndex() * 2) + 1;
    	}
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, SIDE, FACING);
    }
}
