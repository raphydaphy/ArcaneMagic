package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.WaterloggableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.AltarBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AltarBlock extends WaterloggableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	static
	{
		VoxelShape bottom = Block.createCuboidShape(2, 0, 2, 14, 4, 14);
		VoxelShape middle = Block.createCuboidShape(4, 4, 4, 12, 6, 12);
		VoxelShape top = Block.createCuboidShape(0, 6, 0, 16, 12, 16);

		shape = VoxelShapes.union(VoxelShapes.union(bottom, middle), top);
	}

	public AltarBlock()
	{
		super(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f).build());
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof AltarBlockEntity))
		{
			return false;
		}

		return ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, 0);
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
		return shape;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1)
	{
		return new AltarBlockEntity();
	}
}
