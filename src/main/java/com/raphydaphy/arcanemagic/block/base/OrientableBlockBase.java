package com.raphydaphy.arcanemagic.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.Direction;

import java.util.Objects;

public class OrientableBlockBase extends WaterloggableBlockBase
{
	public static final DirectionProperty FACING;

	static
	{
		FACING = HorizontalFacingBlock.field_11177;
	}

	protected OrientableBlockBase(Settings settings)
	{
		super(settings);
		this.setDefaultState((this.getDefaultState()).with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		Direction facing = ctx.getPlayerHorizontalFacing().getOpposite();
		return Objects.requireNonNull(super.getPlacementState(ctx)).with(FACING, facing);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation)
	{
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror)
	{
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> map)
	{
		super.appendProperties(map);
		map.with(FACING);
	}
}
