package com.raphydaphy.arcanemagic.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ArcaneMagicUtils
{
	public static boolean pedestalInteraction(World world, PlayerEntity player, BlockEntity container, Hand hand, int slot)
	{
		ItemStack held = player.getStackInHand(hand);
		ItemStack stackInTable = ((Inventory)container).getInvStack(slot);

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
					((Inventory) container).setInvStack(slot, insertStack);
				} else
				{
					world.playSound(player, container.getPos(), SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCK, 1, 1);
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
						ItemEntity result = new ItemEntity(world,container.getPos().getX() + 0.5, container.getPos().getY() + 1, container.getPos().getZ() + 0.5, stackInTable.copy());
						result.setVelocity(0, 0, 0);
						world.spawnEntity(result);
					}
					((Inventory)container).setInvStack(slot, ItemStack.EMPTY);
				} else
				{
					world.playSound(player, container.getPos(), SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCK, 1, 1);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleTileEntityBroken(Block block, BlockState oldState, World world, BlockPos pos, BlockState newState)
	{
		if (oldState.getBlock() != newState.getBlock())
		{
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory)
			{
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
				world.updateHorizontalAdjacent(pos, block);
			}
			return true;
		}
		return false;
	}

	// Copied from net.minecraft.container.Container#calculateComparatorOutput
	public static int calculateComparatorOutput(World world, BlockPos pos)
	{
		BlockEntity entity = world.getBlockEntity(pos);

		if (entity instanceof Inventory)
		{
			Inventory inventory = (Inventory) entity;
			int count = 0;
			float percent = 0.0F;

			for (int slot = 0; slot < inventory.getInvSize(); ++slot)
			{
				ItemStack stack = inventory.getInvStack(slot);
				if (!stack.isEmpty())
				{
					percent += (float) stack.getAmount() / (float) Math.min(inventory.getInvMaxStackAmount(), stack.getMaxAmount());
					++count;
				}
			}

			percent /= (float) inventory.getInvSize();
			return MathHelper.floor(percent * 14.0F) + (count > 0 ? 1 : 0);
		}
		return 0;
	}

	public static float lerp(float a, float b, float t)
	{
		return (1 - t) * a + t * b;
	}
}
