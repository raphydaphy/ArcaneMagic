package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.WaterloggableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.PipeBlockEntity;
import io.github.prospector.silk.fluid.FluidContainer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PipeBlock extends WaterloggableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape CENTER_SHAPE;
	private static final VoxelShape UP_SHAPE;
	private static final VoxelShape DOWN_SHAPE;
	private static final VoxelShape NORTH_SHAPE;
	private static final VoxelShape EAST_SHAPE;
	private static final VoxelShape SOUTH_SHAPE;
	private static final VoxelShape WEST_SHAPE;

	public static final BooleanProperty UP;
	public static final BooleanProperty DOWN;
	public static final BooleanProperty NORTH;
	public static final BooleanProperty EAST;
	public static final BooleanProperty SOUTH;
	public static final BooleanProperty WEST;

	static
	{
		CENTER_SHAPE = Block.createCuboidShape(6, 6, 6, 10, 10, 10);
		UP_SHAPE = Block.createCuboidShape(6, 10, 6, 10, 16, 10);
		DOWN_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 6, 10);
		NORTH_SHAPE = Block.createCuboidShape(6, 6, 0, 10, 10, 6);
		EAST_SHAPE = Block.createCuboidShape(10, 6, 6, 16, 10, 10);
		SOUTH_SHAPE = Block.createCuboidShape(6, 6, 10, 10, 10, 16);
		WEST_SHAPE = Block.createCuboidShape(0, 6, 6, 6, 10, 10);

		UP = BooleanProperty.create("connection_up");
		DOWN = BooleanProperty.create("connection_down");
		NORTH = BooleanProperty.create("connection_north");
		EAST = BooleanProperty.create("connection_east");
		SOUTH = BooleanProperty.create("connection_south");
		WEST = BooleanProperty.create("connection_west");
	}

	public PipeBlock()
	{
		super(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6f).build());
		this.setDefaultState(this.getDefaultState().with(UP, false).with(DOWN, false).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult info)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!player.isSneaking() && blockEntity instanceof PipeBlockEntity)
		{
			if (!world.isClient)
			{
				PipeBlockEntity pipe = (PipeBlockEntity) blockEntity;
				player.addChatMessage(new StringTextComponent("Storing " + pipe.getFluids(null)[0].getAmount() + " Droplets recieved from " + pipe.getFrom().toString()), true);
			}
			return true;
		}
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition vep)
	{
		VoxelShape shape = CENTER_SHAPE;
		if (state.get(UP)) shape = VoxelShapes.union(shape, UP_SHAPE);
		if (state.get(DOWN)) shape = VoxelShapes.union(shape, DOWN_SHAPE);
		if (state.get(NORTH)) shape = VoxelShapes.union(shape, NORTH_SHAPE);
		if (state.get(EAST)) shape = VoxelShapes.union(shape, EAST_SHAPE);
		if (state.get(SOUTH)) shape = VoxelShapes.union(shape, SOUTH_SHAPE);
		if (state.get(WEST)) shape = VoxelShapes.union(shape, WEST_SHAPE);
		return shape;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1)
	{
		return new PipeBlockEntity();
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> map)
	{
		super.appendProperties(map);
		map.with(UP, DOWN, NORTH, EAST, SOUTH, WEST);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return updateState(ctx.getWorld(), super.getPlacementState(ctx), ctx.getBlockPos());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState otherState, IWorld world, BlockPos pos, BlockPos otherPos)
	{
		return updateState(world, state, pos);
	}

	private BlockState updateState(IWorld world, BlockState base, BlockPos pos)
	{
		if (canConnect(world, pos.add(0, 1, 0))) base = base.with(UP, true);
		else base = base.with(UP, false);

		if (canConnect(world, pos.add(0, -1, 0))) base = base.with(DOWN, true);
		else base = base.with(DOWN, false);

		if (canConnect(world, pos.add(0, 0, -1))) base = base.with(NORTH, true);
		else base = base.with(NORTH, false);

		if (canConnect(world, pos.add(1, 0, 0))) base = base.with(EAST, true);
		else base = base.with(EAST, false);

		if (canConnect(world, pos.add(0, 0, 1))) base = base.with(SOUTH, true);
		else base = base.with(SOUTH, false);

		if (canConnect(world, pos.add(-1, 0, 0))) base = base.with(WEST, true);
		else base = base.with(WEST, false);

		return base;
	}

	private boolean canConnect(IWorld world, BlockPos pos)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof FluidContainer;
	}

	public static BooleanProperty getProp(Direction dir)
	{
		switch (dir)
		{
			case UP:
				return UP;
			case DOWN:
				return DOWN;
			case NORTH:
				return NORTH;
			case EAST:
				return EAST;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			default:
				return UP;
		}
	}
}
