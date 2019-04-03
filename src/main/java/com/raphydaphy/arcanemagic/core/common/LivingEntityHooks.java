package com.raphydaphy.arcanemagic.core.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityHooks
{
	@Invoker("playHurtSound")
	void playHurtSound(DamageSource source);
}
