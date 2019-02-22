package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.entity.InventoryBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
			if (held.getTag() != null)
			{
				int soul = held.getTag().getInt(ScepterItem.SOUL_KEY);

				if (soul >= 10)
				{
					if (!world.isClient)
					{
						held.getTag().putInt(ScepterItem.SOUL_KEY, soul - 10);
						((Inventory)blockEntity).clear();
						ItemEntity result = new ItemEntity(world,pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(Blocks.STONE));
						result.setVelocity(0, 0, 0);
						world.spawnEntity(result);
					}
					else
					{
						player.swingHand(hand);
					}

					return true;
				}
			}
			return false;
		}

		Vec3d hit = new Vec3d(hitResult.getPos().x - pos.getX(), hitResult.getPos().y - pos.getY(), hitResult.getPos().z - pos.getZ());



		if (hit.x >= 0.203 && hit.x <= 0.801 && hit.y >= 0.5625 && hit.z >= 0.203 && hit.z <= 0.801)
		{
			double divX = (hit.x - 0.203f);
			double divZ = (hit.z - 0.203f);

			int slot = (divX <= 0.2152 ? 2 : divX <= 0.4084 ? 1 : 0) + (divZ <= 0.2152 ? 6 : divZ <= 0.4084 ? 3 : 0);

			ItemStack stackInTable = ((Inventory)blockEntity).getInvStack(slot);

			// Try to insert stack
			if (!player.isSneaking())
			{
				if (held.isEmpty())
				{
					return false;
				}
				if (stackInTable.isEmpty())
				{
					ItemStack insertStack = held.copy();

					if (held.getAmount() > 1)
					{
						if (!world.isClient)
						{
							if (!player.isCreative())
							{
								held.subtractAmount(1);
							}
							insertStack.setAmount(1);
						}
					} else if (!world.isClient && !player.isCreative())
					{
						player.setStackInHand(hand, ItemStack.EMPTY);
					}

					// insertStack = 1 item to insert
					// held = remaining items

					if (!world.isClient)
					{
						((Inventory) blockEntity).setInvStack(slot, insertStack);
					} else
					{
						world.playSound(player, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCK, 1, 1);
					}

					return true;
				}
			} else
			{
				if (!stackInTable.isEmpty())
				{
					if (!world.isClient)
					{
						if (!player.method_7270(stackInTable.copy())) // addItemStackToInventory
						{
							ItemEntity result = new ItemEntity(world,pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stackInTable.copy());
							result.setVelocity(0, 0, 0);
							world.spawnEntity(result);
						}
						((Inventory)blockEntity).setInvStack(slot, ItemStack.EMPTY);
					} else
					{
						world.playSound(player, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCK, 1, 1);
					}
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onBlockRemoved(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean boolean_1)
	{
		if (oldState.getBlock() != newState.getBlock())
		{
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory)
			{
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
				world.updateHorizontalAdjacent(pos, this);
			}

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
		return InventoryBlockEntity.getComparatorOutput(world, pos);
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
