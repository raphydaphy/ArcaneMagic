package com.raphydaphy.arcanemagic.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.InventoryUtil;

/**
 * This manages most things required to have a double block with a shared entity
 * `bottom` must be set to true if this entity belongs to the bottom block
 * `setBottom` should be set to true once the bottom block is identified
 */

public abstract class DoubleBlockEntity extends InventoryBlockEntity implements SidedInventory
{
	public boolean bottom;
	boolean setBottom = false;

	DoubleBlockEntity(BlockEntityType<?> type, int size)
	{
		super(type, size);
	}

	DoubleBlockEntity getBottom()
	{
		if (bottom)
		{
			return this;
		} else
		{
			BlockEntity below = world.getBlockEntity(pos.add(0, -1, 0));
			if (below instanceof DoubleBlockEntity)
			{
				return (DoubleBlockEntity)below;
			}
		}
		return null;
	}

	/**
	 * This is called only on the bottom block when isValidInvStack is called on either block
	 */
	public boolean isValidInvStackBottom(int slot, ItemStack stack)
	{
		return super.isValidInvStack(slot, stack);
	}

	@Override
	public boolean isValidInvStack(int slot, ItemStack stack)
	{
		if (bottom)
		{
			return isValidInvStackBottom(slot, stack);
		} else
		{
			DoubleBlockEntity bottomBlockEntity = getBottom();
			if (bottomBlockEntity != null)
			{
				return bottomBlockEntity.isValidInvStackBottom(slot, stack);
			}
		}
		return false;
	}

	@Override
	public void writeContents(CompoundTag tag)
	{
		if (bottom)
		{
			InventoryUtil.serialize(tag, contents);
		}
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		if (bottom)
		{
			contents = DefaultedList.create(getInvSize(), ItemStack.EMPTY);
			InventoryUtil.deserialize(tag, contents);
		}
	}

	@Override
	public boolean isInvEmpty()
	{
		if (bottom)
		{
			return super.isInvEmpty();
		} else
		{
			DoubleBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.isInvEmpty();
			}
		}
		return true;
	}

	@Override
	public ItemStack getInvStack(int slot)
	{
		if (bottom)
		{
			return super.getInvStack(slot);
		} else
		{
			DoubleBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.getInvStack(slot);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack takeInvStack(int slot, int count)
	{
		if (bottom)
		{
			return super.takeInvStack(slot, count);
		} else
		{
			DoubleBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.takeInvStack(slot, count);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeInvStack(int slot)
	{
		if (bottom)
		{
			return super.removeInvStack(slot);
		} else
		{
			DoubleBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.removeInvStack(slot);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInvStack(int slot, ItemStack stack)
	{
		if (bottom)
		{
			super.setInvStack(slot, stack);
		} else
		{
			DoubleBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				blockEntityBottom.setInvStack(slot, stack);
			}
		}
	}

	@Override
	public void clear()
	{
		if (bottom)
		{
			super.clear();
		} else
		{
			DoubleBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				blockEntityBottom.clear();
			}
		}
	}
}
