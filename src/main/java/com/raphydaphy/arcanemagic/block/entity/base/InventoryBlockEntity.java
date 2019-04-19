package com.raphydaphy.arcanemagic.block.entity.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

public class InventoryBlockEntity extends BlockEntity implements Inventory {
    private final int size;
    protected DefaultedList<ItemStack> contents;

    public InventoryBlockEntity(BlockEntityType<?> type, int size) {
        super(type);

        this.contents = DefaultedList.create(size, ItemStack.EMPTY);
        this.size = size;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        contents = DefaultedList.create(size, ItemStack.EMPTY);
        Inventories.fromTag(tag, contents);
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        writeContents(tag);
        return tag;
    }

    public void writeContents(CompoundTag tag) {
        Inventories.toTag(tag, contents);
    }

    @Override
    public int getInvSize() {
        return size;
    }

    @Override
    public boolean isInvEmpty() {
        for (ItemStack stack : contents) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return contents.get(slot);
    }

    @Override
    public ItemStack takeInvStack(int slot, int count) {
        ItemStack stack = Inventories.splitStack(contents, slot, count);
        markDirty();

        return stack;
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        ItemStack ret = Inventories.removeStack(contents, slot);
        markDirty();
        return ret;
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        contents.set(slot, stack);
        markDirty();
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity var1) {
        return false;
    }

    @Override
    public void clear() {
        contents.clear();
        markDirty();
    }
}
