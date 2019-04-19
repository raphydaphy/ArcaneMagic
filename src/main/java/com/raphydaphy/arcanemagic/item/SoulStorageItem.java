package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SoulStorageItem extends Item {
    public SoulStorageItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void onCrafted(ItemStack stack, World world, PlayerEntity player) {
        stack.getOrCreateTag().putInt(ArcaneMagicConstants.SOUL_KEY, 0);
    }
}
