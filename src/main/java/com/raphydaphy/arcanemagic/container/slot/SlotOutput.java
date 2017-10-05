package com.raphydaphy.arcanemagic.container.slot;

import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOutput extends SlotItemHandler
{
	public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public void putStack(ItemStack stack)
    {
		if (ModRegistry.ITEMS.contains(stack.getItem()))
		{
			this.inventory.setInventorySlotContents(super.slotNumber, stack);
			this.onSlotChanged();
		}
    }

}
