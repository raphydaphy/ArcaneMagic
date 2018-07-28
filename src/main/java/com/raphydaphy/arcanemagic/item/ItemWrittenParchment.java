package com.raphydaphy.arcanemagic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemWrittenParchment extends Item
{
    private final boolean ancient;

    public ItemWrittenParchment(boolean ancient)
    {
        super(new Item.Builder().group(ItemGroup.MISC));
        this.ancient = ancient;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return ancient;
    }
}
