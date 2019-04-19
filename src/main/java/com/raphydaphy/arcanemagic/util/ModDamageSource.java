package com.raphydaphy.arcanemagic.util;

import net.minecraft.entity.damage.DamageSource;

public class ModDamageSource extends DamageSource {
    public ModDamageSource(String name) {
        super(name);
    }

    @Override
    public ModDamageSource setUnblockable() {
        super.setUnblockable();
        return this;
    }

    @Override
    public ModDamageSource setUsesMagic() {
        super.setUsesMagic();
        return this;
    }

    @Override
    public ModDamageSource setBypassesArmor() {
        super.setBypassesArmor();
        return this;
    }
}
