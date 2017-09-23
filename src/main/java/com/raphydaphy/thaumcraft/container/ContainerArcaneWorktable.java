package com.raphydaphy.thaumcraft.container;

import javax.annotation.Nullable;

import com.raphydaphy.thaumcraft.init.ModItems;
import com.raphydaphy.thaumcraft.tileentity.TileEntityArcaneWorktable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerArcaneWorktable extends Container 
{

    private TileEntityArcaneWorktable te;

    public ContainerArcaneWorktable(IInventory playerInventory, TileEntityArcaneWorktable te) 
    {
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
    }

    private void addOwnSlots()
    {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int x = 35;
        int y = 37;

        int slotIndex = 0;
        for (int row = 0; row < 3; row++) 
        {
        	for (int col = 0; col < 3; col++)
        	{
	            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
	            slotIndex++;
	            
	            x += 24;
        	}
        	x = 35;
        	y += 24;
        }
        
        //addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, 100, 62));
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) 
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) 
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileEntityArcaneWorktable.SIZE) 
            {
                if (!this.mergeItemStack(itemstack1, TileEntityArcaneWorktable.SIZE, this.inventorySlots.size(), true)) 
                {
                    return ItemStack.EMPTY;
                }
            } 
            else if (itemstack1.getItem().equals(ModItems.wand))
            {
            	System.out.println("put it in!");
            }
            /*else if (!this.mergeItemStack(itemstack1, 0, TileEntityArcaneWorktable.SIZE, false)) 
            {
                return null;
            }*/

            if (itemstack1.isEmpty()) 
            {
                slot.putStack(ItemStack.EMPTY);
            } 
            else 
            {
                //slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) 
    {
        return te.canInteractWith(playerIn);
    }
}