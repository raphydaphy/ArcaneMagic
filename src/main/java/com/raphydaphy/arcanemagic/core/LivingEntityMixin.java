package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.init.ModEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	protected LivingEntityMixin(EntityType<? extends LivingEntity> type, World world)
	{
		super(type, world);
	}

	@Inject(at = @At(value = "HEAD"), method = "onDeath")
	private void onDeath(DamageSource source, CallbackInfo info)
	{
		ModEvents.onLivingEntityDeath(this, source);
	}
}
