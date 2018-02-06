package com.raphydaphy.arcanemagic.common.container;

import com.raphydaphy.arcanemagic.common.container.slot.SlotLimited;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAltar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerInfusionAltar extends Container
{
	private TileEntityAltar te;

	public ContainerInfusionAltar(InventoryPlayer playerInventory, TileEntityAltar te)
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
				int y = row * 18 + 118;
				this.addSlotToContainer(new Slot(playerInventory, slot++, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row)
		{
			int x = 8 + row * 18;
			int y = 176;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots()
	{
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		addSlotToContainer(
				new SlotLimited(itemHandler, 0, 15, 41, instance -> instance.getItem().equals(ModRegistry.NOTEBOOK)));
		addSlotToContainer(new SlotLimited(itemHandler, 1, 15, 63, instance -> instance.getItem().equals(Items.PAPER)));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack prev = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack cur = slot.getStack();
			prev = cur.copy();

			// From altar -> player
			if (index < TileEntityAltar.SIZE)
			{
				// TODO: not hardcode the max inv size to support mods that
				// altar the player inventory
				if (!this.mergeItemStack(cur, TileEntityAltar.SIZE, 38, true))
				{
					return ItemStack.EMPTY;
				}
			}
			// From player -> altar
			else
			{
				if (!this.mergeItemStack(cur, 0, 2, true))
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