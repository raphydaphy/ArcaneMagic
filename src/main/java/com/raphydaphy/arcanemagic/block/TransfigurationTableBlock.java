package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TransfigurationTableBlock extends WaterloggableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	public TransfigurationTableBlock()
	{
		super(Block.Settings.copy(Blocks.OAK_PLANKS));
	}

	public boolean useScepter(World world, BlockEntity blockEntity, ItemStack scepter, @Nullable PlayerEntity player, @Nullable Hand hand)
	{
		if (scepter.getTag() != null)
		{
			int soul = scepter.getTag().getInt(ScepterItem.SOUL_KEY);

			if (soul >= 10)
			{
				if (!world.isClient)
				{
					scepter.getTag().putInt(ScepterItem.SOUL_KEY, soul - 10);
					((Inventory)blockEntity).clear();
					ItemEntity result = new ItemEntity(world,blockEntity.getPos().getX() + 0.5, blockEntity.getPos().getY() + 1, blockEntity.getPos().getZ() + 0.5, new ItemStack(Blocks.STONE));
					result.setVelocity(0, 0, 0);
					world.spawnEntity(result);
				}
				if (player != null)
				{
					player.swingHand(hand);
				}

				world.playSound(player, blockEntity.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCK, 1, 1);

				return true;
			}
		}
		return false;
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof TransfigurationTableBlockEntity))
		{
			return false;
		}

		ItemStack held = player.getStackInHand(hand);

		if (!held.isEmpty() && held.getItem() instanceof ScepterItem)
		{
			return useScepter(world, blockEntity, held, player, hand);
		}

		Vec3d hit = new Vec3d(hitResult.getPos().x - pos.getX(), hitResult.getPos().y - pos.getY(), hitResult.getPos().z - pos.getZ());

		if (hit.x >= 0.203 && hit.x <= 0.801 && hit.y >= 0.5625 && hit.z >= 0.203 && hit.z <= 0.801)
		{
			double divX = (hit.x - 0.203f);
			double divZ = (hit.z - 0.203f);

			int slot = (divX <= 0.2152 ? 2 : divX <= 0.4084 ? 1 : 0) + (divZ <= 0.2152 ? 6 : divZ <= 0.4084 ? 3 : 0);

			return ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, slot);
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

	@Override // Copied from net.minecraft.container.Container#calculateComparatorOutput
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
		return new TransfigurationTableBlockEntity();
	}

	static
	{
		VoxelShape bottom = Block.createCuboidShape(2, 0, 2, 14, 4, 14);
		VoxelShape middle = Block.createCuboidShape(0, 4, 0, 16, 8, 16);
		VoxelShape top = Block.createCuboidShape(3, 8, 3, 13, 10, 13);

		shape = VoxelShapes.union(VoxelShapes.union(bottom, middle), top);
	}
}
