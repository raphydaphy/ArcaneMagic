package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class TileEntityAltar extends TileEntity implements ITickable, IInventory
{
    private ItemStack contents = ItemStack.EMPTY;

    public TileEntityAltar()
    {
        super(ArcaneMagic.ALTAR_TE);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.contents = ItemStack.EMPTY;
        if (tag.hasKey("item"))
        {
            this.contents = ItemStack.func_199557_a(tag.getCompoundTag("item"));
        }
        this.contents = ItemStack.func_199557_a(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        NBTTagCompound itemTag = new NBTTagCompound();
        contents.writeToNBT(itemTag);
        tag.setTag("item", itemTag);
        return tag;
    }

    @Override
    public void update()
    {

    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return contents.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        if (index == 0)
        {
            return contents;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        if (index == 0)
        {
            ItemStack out = contents.copy();
            if (contents.getCount() >= count)
            {
                out.setCount(count);
                contents.shrink(count);
                return out;
            }
            else
            {
                contents.setCount(0);
                return out;
            }
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (index == 0)
        {
            ItemStack out = contents.copy();
            contents.setCount(0);
            return out;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index == 0)
        {
            contents = stack.copy();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index == 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public ITextComponent getName()
    {
        return null;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName()
    {
        return null;
    }
}