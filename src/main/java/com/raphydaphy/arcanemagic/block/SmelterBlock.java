package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.entity.SmelterBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.hit.BlockHitResult;
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

public class SmelterBlock extends DoubleBlockBase implements BlockEntityProvider
{
	private static final Map<Direction, VoxelShape> bottom = new HashMap<>();
	private static final Map<Direction, VoxelShape> top = new HashMap<>();

	public SmelterBlock()
	{
		super(Settings.copy(Blocks.FURNACE));
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof SmelterBlockEntity))
		{
			return false;
		}

		if (player.isSneaking())
		{
			for (int slot = 2; slot >= 1; slot--)
			{
				boolean extracted = ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, slot);
				if (extracted)
				{
					return true;
				}
			}
			return false;
		}

		return ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, 0);
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasComparatorOutput(BlockState blockState_1)
	{
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return ArcaneMagicUtils.calculateComparatorOutput(world, pos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition vep)
	{
		return state.get(HALF) == DoubleBlockHalf.LOWER ? bottom.get(state.get(FACING)) : top.get(state.get(FACING));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view)
	{
		return new SmelterBlockEntity();
	}

	static
	{
		VoxelShape bottomOneNorth = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(4, 0, 6, 2, 8, 0), Block.createCuboidShape(14, 12, 12, 12, 14, 6)),
				VoxelShapes.union(Block.createCuboidShape(4, 12, 12, 2, 14, 6), Block.createCuboidShape(14, 8, 6, 2, 14, 0)));
		VoxelShape bottomTwoNorth = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(16, 0, 16, 0, 12, 6), Block.createCuboidShape(12, 0, 6, 4, 2, 0)),
				VoxelShapes.union(Block.createCuboidShape(12, 14, 14, 4, 12, 4), Block.createCuboidShape(12, 4, 14, 4, 0, 6)));
		VoxelShape bottomThreeNorth = VoxelShapes.union(Block.createCuboidShape(14, 0, 6, 12, 8, 0), Block.createCuboidShape(12, 6, 4, 4, 12, 2));

		VoxelShape bottomOneEast = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(10, 0, 12, 16, 8, 14), Block.createCuboidShape(4, 12, 2, 10, 14, 4)),
				VoxelShapes.union(Block.createCuboidShape(4, 12, 12, 10, 14, 14), Block.createCuboidShape(10, 8, 2, 16, 14, 14)));
		VoxelShape bottomTwoEast = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 10, 12, 16), Block.createCuboidShape(10, 0, 4, 16, 2, 12)),
				VoxelShapes.union(Block.createCuboidShape(2, 2, 4, 12, 4, 12), Block.createCuboidShape(2, 12, 4, 10, 16, 12)));
		VoxelShape bottomThreeEast = VoxelShapes.union(Block.createCuboidShape(10, 0, 2, 16, 8, 4), Block.createCuboidShape(12, 6, 4, 14, 12, 12));

		VoxelShape bottomOneSouth = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(12, 0, 10, 14, 8, 16), Block.createCuboidShape(2, 12, 4, 4, 14, 10)),
				VoxelShapes.union(Block.createCuboidShape(12, 12, 4, 14, 14, 10), Block.createCuboidShape(2, 8, 10, 14, 14, 16)));
		VoxelShape bottomTwoSouth = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 12, 10), Block.createCuboidShape(4, 0, 10, 12, 2, 16)),
				VoxelShapes.union(Block.createCuboidShape(4, 2, 2, 12, 4, 12), Block.createCuboidShape(4, 12, 2, 12, 16, 10)));
		VoxelShape bottomThreeSouth = VoxelShapes.union(Block.createCuboidShape(2, 0, 10, 4, 8, 16), Block.createCuboidShape(4, 6, 12, 12, 12, 14));

		VoxelShape bottomOneWest = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(6, 0, 4, 0, 8, 2), Block.createCuboidShape(12, 12, 14, 6, 14, 12)),
				VoxelShapes.union(Block.createCuboidShape(12, 12, 4, 6, 14, 2), Block.createCuboidShape(6, 8, 14, 0, 14, 2)));
		VoxelShape bottomTwoWest = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(16, 0, 16, 6, 12, 0), Block.createCuboidShape(6, 0, 12, 0, 2, 4)),
				VoxelShapes.union(Block.createCuboidShape(14, 14, 12, 4, 12, 4), Block.createCuboidShape(14, 4, 12, 6, 0, 4)));
		VoxelShape bottomThreeWest = VoxelShapes.union(Block.createCuboidShape(6, 0, 14, 0, 8, 12), Block.createCuboidShape(4, 6, 12, 2, 12, 4));

		VoxelShape topNorth = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(10, 4, 12, 6, 6, 8), Block.createCuboidShape(12, 0, 14, 4, 4, 6)), Block.createCuboidShape(12, 6, 14, 4, 8, 6));
		VoxelShape topEast = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(4, 4, 6, 8, 6, 10), Block.createCuboidShape(2, 0, 4, 10, 4, 12)), Block.createCuboidShape(2, 6, 4, 10, 8, 12));
		VoxelShape topSouth = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(6, 4, 4, 10, 6, 8), Block.createCuboidShape(4, 0, 2, 12, 4, 10)), Block.createCuboidShape(4, 6, 2, 12, 8, 10));
		VoxelShape topWest = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(12, 4, 10, 8, 6, 6), Block.createCuboidShape(14, 0, 12, 6, 4, 4)), Block.createCuboidShape(14, 6, 12, 6, 8, 4));

		bottom.put(Direction.NORTH, VoxelShapes.union(VoxelShapes.union(bottomOneNorth, bottomTwoNorth), bottomThreeNorth));
		bottom.put(Direction.EAST, VoxelShapes.union(VoxelShapes.union(bottomOneEast, bottomTwoEast), bottomThreeEast));
		bottom.put(Direction.SOUTH, VoxelShapes.union(VoxelShapes.union(bottomOneSouth, bottomTwoSouth), bottomThreeSouth));
		bottom.put(Direction.WEST, VoxelShapes.union(VoxelShapes.union(bottomOneWest, bottomTwoWest), bottomThreeWest));

		top.put(Direction.NORTH, topNorth);
		top.put(Direction.EAST, topEast);
		top.put(Direction.SOUTH, topSouth);
		top.put(Direction.WEST, topWest);
	}
}
