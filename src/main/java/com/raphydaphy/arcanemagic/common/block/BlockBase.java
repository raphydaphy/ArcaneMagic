package com.raphydaphy.arcanemagic.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBase extends Block implements IBaseBlock
{
	private static AxisAlignedBB RENDER_AABB = FULL_BLOCK_AABB;
	private static List<AxisAlignedBB> COLLISION_AABB_LIST = new ArrayList<>();

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

	public BlockBase setRenderedAABB(AxisAlignedBB aabb)
	{
		RENDER_AABB = aabb;
		return this;
	}

	public BlockBase setCollisionAABBList(List<AxisAlignedBB> aabbList)
	{
		COLLISION_AABB_LIST = aabbList;
		return this;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return RENDER_AABB;
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos,
			@Nonnull Vec3d start, @Nonnull Vec3d end)
	{
		if (COLLISION_AABB_LIST.isEmpty())
		{
			return super.collisionRayTrace(blockState, worldIn, pos, start, end);
		}
		return COLLISION_AABB_LIST.stream().map(bb -> rayTrace(pos, start, end, bb)).anyMatch(Objects::nonNull)
				? super.collisionRayTrace(blockState, worldIn, pos, start, end)
				: null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		if (!isActualState)
		{
			state = this.getActualState(state, worldIn, pos);
		}

		for (AxisAlignedBB aabb : COLLISION_AABB_LIST)
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb);
		}
	}

	public static AxisAlignedBB makeAABB(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return new AxisAlignedBB(x1 * (1d / 16d), y1 * (1d / 16d), z1 * (1d / 16d), x2 * (1d / 16d), y2 * (1d / 16d),
				z2 * (1d / 16d));
	}

}
