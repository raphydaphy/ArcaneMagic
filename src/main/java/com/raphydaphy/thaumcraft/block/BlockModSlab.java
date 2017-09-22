/*
 * Slab Code by @Shadows_Of_Fire
 * Reproduced with permission
 */

package com.raphydaphy.thaumcraft.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockModSlab extends BlockBase {

	public static final PropertyEnum<SlabVariant> VARIANT = PropertyEnum.<SlabVariant>create("slab_variant",
			SlabVariant.class);
	
	public BlockModSlab(String name, Material material) {
		super(name, material, 1.8f);
		useNeighborBrightness = true;
		setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, SlabVariant.LOWER));;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return (state.getValue(VARIANT) == SlabVariant.DOUBLE);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return (state.getValue(VARIANT) == SlabVariant.DOUBLE) ? 2 : 1;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(VARIANT).getAABB();
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) 
	{
		return blockState.getBoundingBox(worldIn, pos);
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		
		SlabVariant variant = SlabVariant.LOWER;
		if (meta == 1)
		{
			variant = SlabVariant.UPPER;
		}
		else if (meta == 2)
		{
			variant = SlabVariant.DOUBLE;
		}
		return this.getDefaultState().withProperty(VARIANT, variant);
	}

	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return state.getValue(VARIANT).getMeta();
	}

	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

	public IBlockState getUpper()
	{
		return this.getDefaultState().withProperty(VARIANT, SlabVariant.UPPER);
	}

	public IBlockState getLower() 
	{
		return this.getDefaultState();
	}

	public IBlockState getDouble() 
	{
		return this.getDefaultState().withProperty(VARIANT, SlabVariant.DOUBLE);
	}

	public IBlockState getOpposite(IBlockState state) 
	{
		if (state == getUpper())
			return getLower();
		return getUpper();
	}

	public static enum SlabVariant implements IStringSerializable 
	{
		LOWER("lower", new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), 0), UPPER("upper",
				new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), 1), DOUBLE("double", FULL_BLOCK_AABB, 2);

		private String name;
		private AxisAlignedBB aabb;
		private int meta;

		private SlabVariant(String name_, AxisAlignedBB AABB_, int meta_) 
		{
			name = name_;
			aabb = AABB_;
			meta = meta_;
		}

		@Override
		public String getName() 
		{
			return name;
		}

		public AxisAlignedBB getAABB() 
		{
			return aabb;
		}

		public int getMeta() 
		{
			return meta;
		}
	}

}