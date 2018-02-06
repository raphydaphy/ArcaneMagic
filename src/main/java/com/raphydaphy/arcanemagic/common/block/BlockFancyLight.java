package com.raphydaphy.arcanemagic.common.block;

import javax.annotation.Nullable;

import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFancyLight extends BlockBase
{
	public BlockFancyLight()
	{
		super("fancy_light", Material.AIR, 0.0f, SoundType.SNOW);
		setTickRandomly(false);
		setLightLevel(1.0F);
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

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return true;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		return;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public void onLanded(World worldIn, Entity entityIn)
	{
		return;
	}

	@Override
	public void init()
	{
		ModRegistry.BLOCKS.add(this);
	}
}
