package com.raphydaphy.arcanemagic.container;

import com.raphydaphy.arcanemagic.handler.InvCraftingHandler;
import com.raphydaphy.arcanemagic.tileentity.TileEntityArcaneWorktable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

public class ContainerArcaneWorktable extends Container
{

	private TileEntityArcaneWorktable te;
	private final InvCraftingHandler craftMatrix;
	private final InventoryCraftResult craftResult;
	protected int playerInventoryStart = -1;

	public ContainerArcaneWorktable(InventoryPlayer playerInventory, TileEntityArcaneWorktable te)
	{
		this.te = te;
		this.craftResult = new InventoryCraftResult();
		this.craftMatrix = new InvCraftingHandler(this, te, 3, 3);

		this.addSlotToContainer(
				new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 80, 20));

		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory)
	{
		int start = this.inventorySlots.size();

		// Slots for the main inventory
		for (int row = 0; row < 3; ++row)
		{
			for (int col = 0; col < 9; ++col)
			{
				int x = 8 + col * 18;
				int y = row * 18 + 147;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row)
		{
			int x = 8 + row * 18;
			int y = 58 + 147;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}

		playerInventoryStart = start;
	}

	private void addOwnSlots()
	{
		int x = 35;
		int y = 37;

		int slotIndex = 1;
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 3; col++)
			{
				addSlotToContainer(new Slot(craftMatrix, slotIndex, x, y));
				slotIndex++;

				x += 24;
			}
			x = 35;
			y += 24;
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return te.canInteractWith(playerIn);
	}
}