package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.item.Item;

public class CrystalItem extends Item {
    public final ArcaneMagicUtils.ForgeCrystal type;

    public CrystalItem(ArcaneMagicUtils.ForgeCrystal type) {
        super(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
        this.type = type;
    }
}
