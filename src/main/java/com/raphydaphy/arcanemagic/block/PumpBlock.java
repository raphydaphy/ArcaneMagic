package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.entity.PumpBlockEntity;
import io.github.prospector.silk.fluid.FluidContainer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class PumpBlock extends WaterloggableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	public PumpBlock()
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
		return new PumpBlockEntity();
	}

	static
	{
		shape = VoxelShapes.fullCube();
	}
}
