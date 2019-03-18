package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.OrientableBlockBase;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

// Unfinished
public class ForgeBlock extends OrientableBlockBase
{
	public static final EnumProperty<ForgeBlockPart> PART;

	private static final Map<ForgeBlockPart, Map<Direction, VoxelShape>> shapes = new HashMap<>();

	static
	{
		PART = EnumProperty.create("part", ForgeBlockPart.class);

		Map<Direction, VoxelShape> front_bottom = new HashMap<>();
		front_bottom.put(Direction.NORTH, VoxelShapes.union(Block.createCuboidShape(12, 8, 16, 4, 12, 10), Block.createCuboidShape(14, 12, 16, 2, 16, 8),
				Block.createCuboidShape(14, 0, 16, 2, 8, 8)));
		front_bottom.put(Direction.EAST, VoxelShapes.union(Block.createCuboidShape(0, 8, 4, 6, 12, 12), Block.createCuboidShape(0, 12, 2, 8, 16, 14),
				Block.createCuboidShape(0, 0, 2, 8, 8, 14)));
		front_bottom.put(Direction.SOUTH, VoxelShapes.union(Block.createCuboidShape(4, 8, 0, 12, 12, 6), Block.createCuboidShape(2, 12, 0, 14, 16, 8),
				Block.createCuboidShape(2, 0, 0, 14, 8, 8)));
		front_bottom.put(Direction.WEST, VoxelShapes.union(Block.createCuboidShape(16, 8, 12, 10, 12, 4), Block.createCuboidShape(16, 12, 14, 8, 16, 2),
				Block.createCuboidShape(64, 0, 14, 8, 8, 2)));
		shapes.put(ForgeBlockPart.FRONT_LOWER, front_bottom);
	}

	public ForgeBlock()
	{
		super(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f).build());
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
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition vep)
	{
		return shapes.containsKey(state.get(PART)) ? shapes.get(state.get(PART)).get(state.get(FACING)) : VoxelShapes.empty();
	}

	// TODO: this ??
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
		Direction facing = state.get(FACING);
		ForgeBlockPart part = state.get(PART);
		BlockPos otherVerticalHalfPos = part.isLowerHalf() ? pos.up() : pos.down();
		BlockState otherVerticalHalfState = world.getBlockState(otherVerticalHalfPos);

		BlockPos otherHorizontalHalfPos = part.isFront() ? pos.add(facing.getOpposite().getVector()) : pos.add(facing.getVector());
		BlockState otherHorizontalHalfState = world.getBlockState(otherHorizontalHalfPos);

		BlockPos cornerPos = part.isLowerHalf() ? otherHorizontalHalfPos.up() : otherHorizontalHalfPos.down();
		BlockState cornerState = world.getBlockState(cornerPos);

		if (otherVerticalHalfState.getBlock() == this && otherVerticalHalfState.get(PART) != part
				&& otherHorizontalHalfState.getBlock() == this && otherHorizontalHalfState.get(PART) != part
				&& cornerState.getBlock() == this && cornerState.get(PART) != part)
		{
			world.setBlockState(otherVerticalHalfPos, Blocks.AIR.getDefaultState(), 35);
			world.setBlockState(otherHorizontalHalfPos, Blocks.AIR.getDefaultState(), 35);
			world.setBlockState(cornerPos, Blocks.AIR.getDefaultState(), 35);

			world.playEvent(breaker, 2001, otherVerticalHalfPos, Block.getRawIdFromState(otherVerticalHalfState));
			world.playEvent(breaker, 2001, otherHorizontalHalfPos, Block.getRawIdFromState(otherHorizontalHalfState));
			world.playEvent(breaker, 2001, cornerPos, Block.getRawIdFromState(cornerState));

			ItemStack itemStack_1 = breaker.getMainHandStack();
			if (!world.isClient && !breaker.isCreative())
			{
				Block.dropStacks(state, world, pos, null, breaker, itemStack_1);
				Block.dropStacks(otherVerticalHalfState, world, otherVerticalHalfPos, null, breaker, itemStack_1);
				Block.dropStacks(otherHorizontalHalfState, world, otherHorizontalHalfPos, null, breaker, itemStack_1);
				Block.dropStacks(cornerState, world, cornerPos, null, breaker, itemStack_1);
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
		Direction facing = state.get(FACING).getOpposite();
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
