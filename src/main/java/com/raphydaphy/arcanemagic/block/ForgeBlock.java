package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.OrientableBlockBase;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

// Unfinished
public class ForgeBlock extends OrientableBlockBase
{
	public static final EnumProperty<ForgeBlockPart> PART;

	static
	{
		PART = EnumProperty.create("part", ForgeBlockPart.class);
	}

	protected ForgeBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState((this.getDefaultState()).with(PART, ForgeBlockPart.FRONT_LOWER));
	}

	@Override
	public void onBlockRemoved(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean boolean_1)
	{
		if (ArcaneMagicUtils.handleTileEntityBroken(this, oldState, world, pos, newState))
		{
			super.onBlockRemoved(oldState, world, pos, newState, boolean_1);
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState otherState, IWorld world, BlockPos pos, BlockPos otherPos)
	{
		ForgeBlockPart otherHalf = state.get(PART);
		if (dir.getAxis() == Direction.Axis.Y && otherHalf == ForgeBlockPart.FRONT_LOWER == (dir == Direction.UP))
		{
			return otherState.getBlock() == this && otherState.get(PART) != otherHalf ? state.with(FACING, otherState.get(FACING)) : Blocks.AIR.getDefaultState();
		} else
		{
			return otherHalf == ForgeBlockPart.FRONT_LOWER && dir == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, dir, otherState, world, pos, otherPos);
		}
	}

	@Override
	public void afterBreak(World world_1, PlayerEntity playerEntity_1, BlockPos blockPos_1, BlockState blockState_1, BlockEntity blockEntity_1, ItemStack itemStack_1)
	{
		super.afterBreak(world_1, playerEntity_1, blockPos_1, Blocks.AIR.getDefaultState(), blockEntity_1, itemStack_1);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity breaker)
	{
		ForgeBlockPart half = state.get(PART);
		BlockPos otherPartPos = half == ForgeBlockPart.FRONT_LOWER ? pos.up() : pos.down();
		BlockState otherPartState = world.getBlockState(otherPartPos);
		if (otherPartState.getBlock() == this && otherPartState.get(PART) != half)
		{
			world.setBlockState(otherPartPos, Blocks.AIR.getDefaultState(), 35);
			ItemStack itemStack_1 = breaker.getMainHandStack();
			if (!world.isClient && !breaker.isCreative())
			{
				Block.dropStacks(state, world, pos, null, breaker, itemStack_1);
				Block.dropStacks(otherPartState, world, otherPartPos, null, breaker, itemStack_1);
			}
		}
		super.onBreak(world, pos, state, breaker);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockPos above = ctx.getBlockPos();
		if (above.getY() < 255 && ctx.getWorld().getBlockState(above.up()).method_11587(ctx))
		{
			return this.getDefaultState().with(FACING, ctx.getPlayerHorizontalFacing().getOpposite()).with(PART, ForgeBlockPart.FRONT_LOWER);
		} else
		{
			return null;
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		Direction facing = state.get(FACING);
		BlockPos back = pos.east(facing.getOffsetX());
		if (facing.getAxis() == Direction.Axis.Z)
		{
			back = pos.south(facing.getOffsetZ());
		}
		world.setBlockState(back, state.with(PART, ForgeBlockPart.BACK_LOWER));
		world.setBlockState(back.up(), state.with(PART, ForgeBlockPart.BACK_UPPER));
		world.setBlockState(pos.up(), state.with(PART, ForgeBlockPart.FRONT_UPPER), 3);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState blockState_1)
	{
		return PistonBehavior.DESTROY;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public long getRenderingSeed(BlockState state, BlockPos pos)
	{
		return MathHelper.hashCode(pos.north(state.get(PART).isFront() ? 0 : 1).getX(), pos.down(state.get(PART).isLowerHalf() ? 0 : 1).getY(), pos.getZ());
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> map)
	{
		super.appendProperties(map);
		map.with(PART);
	}

	public enum ForgeBlockPart implements StringRepresentable
	{
		FRONT_UPPER,
		BACK_UPPER,
		FRONT_LOWER,
		BACK_LOWER;

		public String toString()
		{
			return this.asString();
		}

		public String asString()
		{
			switch (this)
			{
				case FRONT_UPPER:
					return "front_upper";
				case BACK_UPPER:
					return "back_upper";
				case FRONT_LOWER:
					return "front_lower";
				case BACK_LOWER:
					return "back_lower";
			}
			return "front_lower";
		}

		public boolean isLowerHalf()
		{
			return this == FRONT_LOWER || this == BACK_LOWER;
		}

		public boolean isFront()
		{
			return this == FRONT_LOWER || this == FRONT_UPPER;
		}
	}
}
