package com.raphydaphy.arcanemagic.block.entity.base;

import com.raphydaphy.arcanemagic.block.base.DoubleBlockBase;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

/**
 * This manages most things required to have a double block with a shared entity
 * `bottom` must be set to true if this entity belongs to the bottom block
 * `setBottom` should be set to true once the bottom block is identified
 */

public abstract class DoubleBlockEntity extends InventoryBlockEntity implements SidedInventory
{
	private boolean bottom;
	private boolean setBottom = false;

	protected DoubleBlockEntity(BlockEntityType<?> type, int size)
	{
		super(type, size);
	}

	protected DoubleBlockEntity getBottom()
	{
		if (isBottom())
		{
			return this;
		} else if (world != null)
		{
			BlockEntity below = world.getBlockEntity(pos.add(0, -1, 0));
			if (below instanceof DoubleBlockEntity)
			{
				return (DoubleBlockEntity) below;
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
		if (isBottom())
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
		if (isBottom())
		{
			Inventories.toTag(tag, contents);
			tag.putBoolean(ArcaneMagicConstants.IS_BOTTOM_KEY, true);
		}
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		if (tag.getBoolean(ArcaneMagicConstants.IS_BOTTOM_KEY))
		{
			contents = DefaultedList.create(getInvSize(), ItemStack.EMPTY);
			Inventories.fromTag(tag, contents);
		}
	}

	public boolean isBottom()
	{
		if (world != null && !setBottom)
		{
			BlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof DoubleBlockBase)
			{
				if (state.get(DoubleBlockBase.HALF) == DoubleBlockHalf.LOWER)
				{
					bottom = true;
				} else
				{
					bottom = false;
				}
				setBottom = true;
			}
		}
		return bottom;
	}

	@Override
	public boolean isInvEmpty()
	{
		if (isBottom())
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
		if (isBottom())
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
		if (isBottom())
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
		if (isBottom())
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
		if (isBottom())
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
		if (isBottom())
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
