package com.raphydaphy.arcanemagic.container;

import com.raphydaphy.arcanemagic.container.slot.SlotLimited;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.tileentity.TileEntityWritingDesk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerWritingDesk extends Container
{
	private TileEntityWritingDesk te;

	public ContainerWritingDesk(InventoryPlayer playerInventory, TileEntityWritingDesk te)
	{
		super();
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory)
	{
		// Slots for the main inventory
		for (int row = 0; row < 3; ++row)
		{
			for (int col = 0; col < 9; ++col)
			{
				int x = 8 + col * 18;
				int y = row * 18 + 110;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row)
		{
			int x = 8 + row * 18;
			int y = 168;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots()
	{
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		addSlotToContainer(
				new SlotLimited(itemHandler, 0, 15, 42, instance -> instance.getItem().equals(ModRegistry.NOTEBOOK)));
		addSlotToContainer(new SlotLimited(itemHandler, 1, 15, 64, instance -> instance.getItem().equals(Items.PAPER)));
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

			// From writing desk -> player
			if (index < TileEntityWritingDesk.SIZE)
			{
				// TODO: not hardcode the max inv size to support mods that altar the player inventory
				if (!this.mergeItemStack(cur, TileEntityWritingDesk.SIZE, 38, true))
				{
					return ItemStack.EMPTY;
				}
			}
			// TODO: From player to writing desk - should transfer only notebook or paper 
			else
			{
				if (!this.mergeItemStack(cur, 0, TileEntityWritingDesk.SIZE, true))
				{
					return ItemStack.EMPTY;
				}
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