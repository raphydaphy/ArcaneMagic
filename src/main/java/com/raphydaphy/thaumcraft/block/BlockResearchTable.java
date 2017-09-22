package com.raphydaphy.thaumcraft.block;

import com.raphydaphy.thaumcraft.init.ModBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockResearchTable extends BlockTable
{
	public static final PropertyInteger SIDE = PropertyInteger.create("side", 0, 1);
	public BlockResearchTable() 
	{
		super("research_table", Material.WOOD, 2.5f, "axe", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SIDE, 0));
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        BlockPos north = pos.north(); // 0, 0, -1
        BlockPos east = pos.east(); // 1, 0, 0
        BlockPos south = pos.south(); // 0, 0, 1
        BlockPos west = pos.west(); // -1, 0, 0

        if (state.getValue(SIDE) == 0)
        {
        	if (worldIn.getBlockState(north).getBlock() == this)
        	{
        		worldIn.setBlockState(north, ModBlocks.table.getDefaultState().withProperty(BlockTable.FACING, EnumFacing.WEST));
        	}
        	if (worldIn.getBlockState(east).getBlock() == this)
        	{
        		worldIn.setBlockState(east, ModBlocks.table.getDefaultState().withProperty(BlockTable.FACING, EnumFacing.SOUTH));
        	}
        }
        else
        {
        	if (worldIn.getBlockState(south).getBlock() == this)
        	{
        		worldIn.setBlockState(south, ModBlocks.table.getDefaultState().withProperty(BlockTable.FACING, EnumFacing.EAST));
        	}
        	if (worldIn.getBlockState(west).getBlock() == this)
        	{
        		worldIn.setBlockState(west, ModBlocks.table.getDefaultState().withProperty(BlockTable.FACING, EnumFacing.NORTH));
        	}
        }
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
