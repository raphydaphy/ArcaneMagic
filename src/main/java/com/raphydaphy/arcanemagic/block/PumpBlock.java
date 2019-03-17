package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.DoubleBlockBase;
import com.raphydaphy.arcanemagic.block.entity.PumpBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class PumpBlock extends DoubleBlockBase implements BlockEntityProvider
{
	private static final VoxelShape bottomX;
	private static final VoxelShape bottomZ;
	private static final VoxelShape top;

	static
	{
		bottomZ = VoxelShapes.union(Block.createCuboidShape(4, 0, 4, 8, 2, 12), Block.createCuboidShape(0, 4, 4, 2, 12, 12), Block.createCuboidShape(2, 2, 2, 14, 14, 14),
				Block.createCuboidShape(4, 14, 4, 12, 16, 12), Block.createCuboidShape(14, 4, 4, 16, 12, 12), Block.createCuboidShape(8, 0, 4, 12, 2, 12));
		bottomX = VoxelShapes.union(Block.createCuboidShape(4, 0, 4, 12, 2, 8), Block.createCuboidShape(4, 4, 0, 12, 12, 2), Block.createCuboidShape(2, 2, 2, 14, 14, 14),
				Block.createCuboidShape(4, 14, 4, 12, 16, 12), Block.createCuboidShape(4, 4, 14, 12, 12, 16), Block.createCuboidShape(4, 0, 8, 12, 2, 12));
		top = VoxelShapes.union(Block.createCuboidShape(4, 4, 4, 12, 8, 12), Block.createCuboidShape(6, 0, 6, 10, 4, 10));
	}

	public PumpBlock()
	{
		super(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6f).build());
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition vep)
	{
		return state.get(HALF) == DoubleBlockHalf.LOWER ? (state.get(FACING).getAxis() == Direction.Axis.Z ? bottomZ : bottomX) : top;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1)
	{
		return new PumpBlockEntity();
	}
}
