package com.raphydaphy.arcanemagic.core;

import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnEggItem.class)
public interface SpawnEggItemHooks
{
	@Accessor("field_8916")
	int getColor();
}
