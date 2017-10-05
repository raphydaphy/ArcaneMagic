package com.raphydaphy.arcanemagic.handler;

import javax.annotation.Nonnull;

import com.raphydaphy.arcanemagic.network.PacketElementalCraftingSync;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class InvCraftingHandler extends InventoryCrafting
{
	private final int length;
	private final Container eventHandler;
	private final IItemHandler itemHandler;
	private final TileEntity te;

	public InvCraftingHandler(Container eventHandler, TileEntity te, int width, int height)
	{
		super(eventHandler, width, height);
		int k = width * height;
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		assert (k == itemHandler.getSlots());
		this.te = te;
		this.itemHandler = itemHandler;
		this.length = k;
		this.eventHandler = eventHandler;
	}

	@Override
	public int getSizeInventory()
	{
		return this.length;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= this.getSizeInventory() ? ItemStack.EMPTY : this.itemHandler.getStackInSlot(index);
	}

	public String getCommandSenderName()
	{
		return "container.arcane_worktable";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Nonnull
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (!this.getStackInSlot(index).isEmpty())
		{
			ItemStack itemstack;

			if (this.getStackInSlot(index).getCount() <= count)
			{
				itemstack = this.getStackInSlot(index);
				this.setInventorySlotContents(index, ItemStack.EMPTY);
				this.eventHandler.onCraftMatrixChanged(this);
				return itemstack;
			} else
			{
				itemstack = this.getStackInSlot(index).splitStack(count);

				if (this.getStackInSlot(index).getCount() == 0)
				{
					this.setInventorySlotContents(index, ItemStack.EMPTY);
				}

				this.eventHandler.onCraftMatrixChanged(this);
				return itemstack;
			}
		} else
		{
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
	{
		this.itemHandler.insertItem(index, stack, false);
		this.eventHandler.onCraftMatrixChanged(this);
	}

	@Override
	public void markDirty()
	{
		this.te.markDirty();
		this.eventHandler.onCraftMatrixChanged(this);

		ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketElementalCraftingSync());
	}

	@Override
	public void clear()
	{
		// do exactly nothing
	}
}