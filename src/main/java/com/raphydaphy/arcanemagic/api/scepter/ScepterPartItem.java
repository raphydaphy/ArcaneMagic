package com.raphydaphy.arcanemagic.api.scepter;

import net.minecraft.item.ItemStack;

/**
 * Created by Xander V on 29/09/2017.
 */
public interface ScepterPartItem {
    ScepterPart getPartFromItemStack(ItemStack in);
}
