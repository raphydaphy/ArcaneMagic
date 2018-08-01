package com.raphydaphy.arcanemagic.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Iterator;

public class TileEntityInventory extends TileEntity implements IInventory
{
    private NonNullList<ItemStack> contents;

    private final String name;
    private final int size;

    public TileEntityInventory(TileEntityType type, String name, int size)
    {
        super(type);

        this.contents = NonNullList.withSize(size, ItemStack.EMPTY);

        this.name = name;
        this.size = size;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tag, contents);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        writeContents(tag);
        return tag;
    }

    NBTTagCompound writeContents(NBTTagCompound tag)
    {
        ItemStackHelper.saveAllItems(tag, contents);
        return tag;
    }

    public void contentsChanged()
    {
        markDirty();
    }

    @Override
    public boolean isEmpty()
    {
        Iterator var1 = contents.iterator();

        ItemStack curStack;
        do
        {
            if (!var1.hasNext())
            {
                return true;
            }

            curStack = (ItemStack)var1.next();
        }
        while(curStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return contents.get(slot);
    }

    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack stack = ItemStackHelper.getAndSplit(contents, index, count);

        if (!stack.isEmpty())
        {
            contentsChanged();
        }

        return stack;
    }

    public ItemStack removeStackFromSlot(int p_removeStackFromSlot_1_)
    {
        return ItemStackHelper.getAndRemove(contents, p_removeStackFromSlot_1_);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        contents.set(slot, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        contentsChanged();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
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
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return size;
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
        contents.clear();
    }

    @Override
    public ITextComponent getName()
    {
        return new TextComponentTranslation("name");
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
