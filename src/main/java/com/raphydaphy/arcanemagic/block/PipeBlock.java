package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.WaterloggableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.PipeBlockEntity;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Hand;
import net.minecraft.util.StringRepresentable;
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
	public static final EnumProperty<PipeConnection> UP;
	public static final EnumProperty<PipeConnection> DOWN;
	public static final EnumProperty<PipeConnection> NORTH;
	public static final EnumProperty<PipeConnection> EAST;
	public static final EnumProperty<PipeConnection> SOUTH;
	public static final EnumProperty<PipeConnection> WEST;
	private static final VoxelShape CENTER_SHAPE;
	private static final VoxelShape UP_PIPE_SHAPE;
	private static final VoxelShape UP_BLOCK_SHAPE;
	private static final VoxelShape DOWN_PIPE_SHAPE;
	private static final VoxelShape DOWN_BLOCK_SHAPE;
	private static final VoxelShape NORTH_PIPE_SHAPE;
	private static final VoxelShape NORTH_BLOCK_SHAPE;
	private static final VoxelShape EAST_PIPE_SHAPE;
	private static final VoxelShape EAST_BLOCK_SHAPE;
	private static final VoxelShape SOUTH_PIPE_SHAPE;
	private static final VoxelShape SOUTH_BLOCK_SHAPE;
	private static final VoxelShape WEST_PIPE_SHAPE;
	private static final VoxelShape WEST_BLOCK_SHAPE;

	static
	{
		CENTER_SHAPE = Block.createCuboidShape(6, 6, 6, 10, 10, 10);
		UP_PIPE_SHAPE = Block.createCuboidShape(6, 10, 6, 10, 16, 10);
		UP_BLOCK_SHAPE = VoxelShapes.union(Block.createCuboidShape(6, 10, 6, 10, 14, 10), Block.createCuboidShape(4, 14, 4, 12, 16, 12));
		DOWN_PIPE_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 6, 10);
		DOWN_BLOCK_SHAPE = VoxelShapes.union(Block.createCuboidShape(6, 2, 6, 10, 6, 10), Block.createCuboidShape(4, 0, 4, 12, 2, 12));

		NORTH_PIPE_SHAPE = Block.createCuboidShape(6, 6, 0, 10, 10, 6);
		NORTH_BLOCK_SHAPE = VoxelShapes.union(Block.createCuboidShape(6, 6, 2, 10, 10, 6), Block.createCuboidShape(4, 4, 0, 12, 12, 2));

		SOUTH_PIPE_SHAPE = Block.createCuboidShape(6, 6, 10, 10, 10, 16);
		SOUTH_BLOCK_SHAPE = VoxelShapes.union(Block.createCuboidShape(6, 6, 10, 10, 10, 14), Block.createCuboidShape(4, 4, 14, 12, 12, 16));

		EAST_PIPE_SHAPE = Block.createCuboidShape(10, 6, 6, 16, 10, 10);
		EAST_BLOCK_SHAPE = VoxelShapes.union(Block.createCuboidShape(10, 6, 6, 14, 10, 10), Block.createCuboidShape(14, 4, 4, 16, 12, 12));

		WEST_PIPE_SHAPE = Block.createCuboidShape(0, 6, 6, 6, 10, 10);
		WEST_BLOCK_SHAPE = VoxelShapes.union(Block.createCuboidShape(2, 6, 6, 6, 10, 10), Block.createCuboidShape(0, 4, 4, 2, 12, 12));

		UP = EnumProperty.create("connection_up", PipeConnection.class);
		DOWN = EnumProperty.create("connection_down", PipeConnection.class);
		NORTH = EnumProperty.create("connection_north", PipeConnection.class);
		EAST = EnumProperty.create("connection_east", PipeConnection.class);
		SOUTH = EnumProperty.create("connection_south", PipeConnection.class);
		WEST = EnumProperty.create("connection_west", PipeConnection.class);
	}

	public PipeBlock()
	{
		super(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6f).build());
		this.setDefaultState(this.getDefaultState().with(UP, PipeConnection.NONE).with(DOWN, PipeConnection.NONE).with(NORTH, PipeConnection.NONE).with(EAST, PipeConnection.NONE).with(SOUTH, PipeConnection.NONE).with(WEST, PipeConnection.NONE));
	}

	public static EnumProperty<PipeConnection> getProp(Direction dir)
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

		PipeConnection connection = state.get(UP);
		if (connection == PipeConnection.PIPE) shape = VoxelShapes.union(shape, UP_PIPE_SHAPE);
		else if (connection == PipeConnection.BLOCK) shape = VoxelShapes.union(shape, UP_BLOCK_SHAPE);

		connection = state.get(DOWN);
		if (connection == PipeConnection.PIPE) shape = VoxelShapes.union(shape, DOWN_PIPE_SHAPE);
		else if (connection == PipeConnection.BLOCK) shape = VoxelShapes.union(shape, DOWN_BLOCK_SHAPE);

		connection = state.get(NORTH);
		if (connection == PipeConnection.PIPE) shape = VoxelShapes.union(shape, NORTH_PIPE_SHAPE);
		else if (connection == PipeConnection.BLOCK) shape = VoxelShapes.union(shape, NORTH_BLOCK_SHAPE);

		connection = state.get(EAST);
		if (connection == PipeConnection.PIPE) shape = VoxelShapes.union(shape, EAST_PIPE_SHAPE);
		else if (connection == PipeConnection.BLOCK) shape = VoxelShapes.union(shape, EAST_BLOCK_SHAPE);

		connection = state.get(SOUTH);
		if (connection == PipeConnection.PIPE) shape = VoxelShapes.union(shape, SOUTH_PIPE_SHAPE);
		else if (connection == PipeConnection.BLOCK) shape = VoxelShapes.union(shape, SOUTH_BLOCK_SHAPE);

		connection = state.get(WEST);
		if (connection == PipeConnection.PIPE) shape = VoxelShapes.union(shape, WEST_PIPE_SHAPE);
		else if (connection == PipeConnection.BLOCK) shape = VoxelShapes.union(shape, WEST_BLOCK_SHAPE);

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
		base = base.with(UP, getConnectionFor(world, Direction.UP, pos.add(0, 1, 0)))
				.with(DOWN, getConnectionFor(world, Direction.DOWN, pos.add(0, -1, 0)))
				.with(NORTH, getConnectionFor(world, Direction.NORTH, pos.add(0, 0, -1)))
				.with(EAST, getConnectionFor(world, Direction.EAST, pos.add(1, 0, 0)))
				.with(SOUTH, getConnectionFor(world, Direction.SOUTH, pos.add(0, 0, 1)))
				.with(WEST, getConnectionFor(world, Direction.WEST, pos.add(-1, 0, 0)));
		return base;
	}

	private PipeConnection getConnectionFor(IWorld world, Direction offset, BlockPos pos)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PipeBlockEntity)
		{
			return PipeConnection.PIPE;
		}
		if (blockEntity instanceof FluidContainer)
		{
			FluidInstance[] fluids = ((FluidContainer) blockEntity).getFluids(offset.getOpposite());
			return fluids.length > 0 ? PipeConnection.BLOCK : PipeConnection.NONE;
		}
		return PipeConnection.NONE;
	}

	public enum PipeConnection implements StringRepresentable
	{
		NONE,
		PIPE,
		BLOCK;

		public String toString()
		{
			return this.asString();
		}

		public String asString()
		{
			return this == BLOCK ? "block" : this == PIPE ? "pipe" : "lower";
		}

		public boolean hasConnection()
		{
			return this == PIPE || this == BLOCK;
		}

		public boolean isPipe()
		{
			return this == PIPE;
		}

		public boolean isBlock()
		{
			return this == BLOCK;
		}
	}

}
