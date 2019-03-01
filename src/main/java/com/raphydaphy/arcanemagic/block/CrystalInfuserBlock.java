package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.WaterloggableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CrystalInfuserBlock extends WaterloggableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	static
	{
		VoxelShape cubes_one = VoxelShapes.union(
				VoxelShapes.union(Block.createCuboidShape(12, 0, 2, 14, 2, 4), Block.createCuboidShape(12, 0, 12, 14, 2, 14)),
				VoxelShapes.union(Block.createCuboidShape(2, 0, 12, 4, 2, 14), Block.createCuboidShape(2, 0, 2, 4, 2, 4)));
		VoxelShape cubes_two = VoxelShapes.union(
				VoxelShapes.union(Block.createCuboidShape(9, 1, 5, 11, 3, 7), Block.createCuboidShape(9, 1, 9, 11, 3, 11)),
				VoxelShapes.union(Block.createCuboidShape(5, 1, 9, 7, 3, 11), Block.createCuboidShape(5, 1, 5, 7, 3, 7)));
		VoxelShape cubes_three = VoxelShapes.union(
				VoxelShapes.union(Block.createCuboidShape(4, 4, 10, 6, 6, 12), Block.createCuboidShape(4, 4, 4, 6, 6, 6)),
				VoxelShapes.union(Block.createCuboidShape(10, 4, 4, 12, 6, 6), Block.createCuboidShape(10, 4, 10, 12, 6, 12)));
		VoxelShape cubes_four = VoxelShapes.union(Block.createCuboidShape(2, 6, 2, 14, 8, 14), Block.createCuboidShape(4, 8, 4, 12, 10, 12));

		shape = VoxelShapes.union(VoxelShapes.union(cubes_one, cubes_two), VoxelShapes.union(cubes_three, cubes_four));
	}

	public CrystalInfuserBlock()
	{
		super(FabricBlockSettings.of(Material.WOOD).strength(2f, 3f).sounds(BlockSoundGroup.WOOD).build());
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof CrystalInfuserBlockEntity))
		{
			return false;
		}

		if (!((CrystalInfuserBlockEntity) blockEntity).isActive())
		{
			if (player.isSneaking())
			{
				// Try to extract from any slot
				for (int i = 2; i >= 0; i--)
				{
					boolean success = ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, i);
					if (success)
					{
						return true;
					}
				}
			} else
			{
				// Try to insert to the right slot
				int slot = ((CrystalInfuserBlockEntity) blockEntity).getSlotForItem(player.getStackInHand(hand));
				if (slot != -1)
				{
					return ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, slot);
				}
			}
		}
		return false;
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
		return new CrystalInfuserBlockEntity();
	}
}
