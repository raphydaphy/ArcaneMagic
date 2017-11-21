package com.raphydaphy.arcanemagic.common.container;

import com.raphydaphy.arcanemagic.common.container.slot.SlotOutput;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimusMaterializer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerAnimusMaterializer extends Container
{

	private TileEntityAnimusMaterializer te;

	public ContainerAnimusMaterializer(InventoryPlayer playerInventory, TileEntityAnimusMaterializer te)
	{
		super();
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory)
	{
		// Slots for the main inventory
		int slot = 9;
		for (int row = 0; row < 3; ++row)
		{
			for (int col = 0; col < 9; ++col)
			{
				int x = 8 + col * 18;
				int y = row * 18 + 99;
				this.addSlotToContainer(new Slot(playerInventory, slot, x, y));
				slot++;
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row)
		{
			int x = 8 + row * 18;
			int y = 157;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots()
	{
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		int slot = 0;
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 2; x++)
			{

				addSlotToContainer(new SlotOutput(itemHandler, slot, (x * 20) + 15, (y * 20) + 21));
				slot++;
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack prev = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack cur = slot.getStack();
			prev = cur.copy();

			// From crystlaizer -> player
			if (index < TileEntityAnimusMaterializer.SIZE)
			{
				// TODO: not hardcode the max inv size to support mods that altar the player inventory
				if (!this.mergeItemStack(cur, TileEntityAnimusMaterializer.SIZE, 42, true))
				{
					return ItemStack.EMPTY;
				}
			}
			// From player to crystallizer - we dont want this!
			else
			{
				return ItemStack.EMPTY;
			}

			if (cur.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}

			if (cur.getCount() == prev.getCount())
			{
				return ItemStack.EMPTY;
			}

		}
		return prev;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return te.canInteractWith(playerIn);
	}
}