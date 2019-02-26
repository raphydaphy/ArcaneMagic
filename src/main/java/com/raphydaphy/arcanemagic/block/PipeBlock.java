package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.entity.PipeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class PipeBlock extends WaterloggableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	public PipeBlock()
	{
		super(Settings.copy(Blocks.STONE));
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition vep)
	{
		return shape;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1)
	{
		return new PipeBlockEntity();
	}

	static
	{
		shape = Block.createCuboidShape(5, 5, 5, 11, 11, 11);;
	}
}
