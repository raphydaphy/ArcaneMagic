package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.init.ModEvents;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextTypes;
import org.lwjgl.system.CallbackI;
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

	@Inject(at = @At(value = "HEAD"), method = "method_16077", cancellable = true) // dropLoot
	private void method_16077(DamageSource source, boolean killedByPlayer, CallbackInfo info)
	{
		if (source == ModRegistry.DRAINED_DAMAGE)
		{
			info.cancel();
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "dropEquipment", cancellable = true)
	private void dropEquipment(DamageSource source, int looting, boolean killedByPlayer, CallbackInfo info)
	{
		if (source == ModRegistry.DRAINED_DAMAGE)
		{
			info.cancel();
		}
	}
}
