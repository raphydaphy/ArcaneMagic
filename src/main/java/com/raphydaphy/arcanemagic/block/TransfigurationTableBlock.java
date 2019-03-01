package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.OrientableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TransfigurationTableBlock extends OrientableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	static
	{
		VoxelShape bottom = Block.createCuboidShape(2, 0, 2, 14, 4, 14);
		VoxelShape middle = Block.createCuboidShape(0, 4, 0, 16, 8, 16);
		VoxelShape top = Block.createCuboidShape(3, 8, 3, 13, 10, 13);

		shape = VoxelShapes.union(VoxelShapes.union(bottom, middle), top);
	}

	public TransfigurationTableBlock()
	{
		super(FabricBlockSettings.of(Material.WOOD).strength(2f, 3f).sounds(BlockSoundGroup.WOOD).build());
	}

	public boolean useScepter(World world, BlockEntity blockEntity, ItemStack scepter, PlayerEntity player, Hand hand)
	{
		if (scepter.getTag() != null)
		{
			int soul = scepter.getTag().getInt(ArcaneMagicConstants.SOUL_KEY);

			DefaultedList<ItemStack> inv = ((TransfigurationTableBlockEntity) blockEntity).getInventory();
			TransfigurationRecipe match = null;

			for (TransfigurationRecipe recipe : ModRegistry.TRANSFIGURATION_RECIPES)
			{
				if (recipe.matches(inv))
				{
					match = recipe;
					break;
				}
			}

			if (match != null)
			{
				if (ArcaneMagicUtils.useSoul(world, scepter, player, match.getSoul()))
				{
					if (!world.isClient)
					{
						((TransfigurationTableBlockEntity) blockEntity).clearRecipe(world.isReceivingRedstonePower(blockEntity.getPos()));
						ItemEntity result = new ItemEntity(world, blockEntity.getPos().getX() + 0.5, blockEntity.getPos().getY() + 1, blockEntity.getPos().getZ() + 0.5, match.getOutput());
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
			Direction facing = state.get(FACING);

			double divX = (hit.x - 0.203f);
			double divZ = (hit.z - 0.203f);

			int row = (divX <= 0.2152 ? 2 : divX <= 0.4084 ? 1 : 0);
			int col = (divZ <= 0.2152 ? 2 : divZ <= 0.4084 ? 1 : 0);

			int slot = row + col * 3;

			if (facing == Direction.EAST)
			{
				row = 2 - row;
				slot = row * 3 + col;
			} else if (facing == Direction.SOUTH)
			{
				slot = 8 - slot;
			} else if (facing == Direction.WEST)
			{
				col = 2 - col;
				slot = row * 3 + col;
			}

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
}
