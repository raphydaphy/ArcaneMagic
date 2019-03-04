package com.raphydaphy.arcanemagic.core;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
	@Inject(at=@At("TAIL"), method="method_14203")
	private void method_14203(ServerPlayerEntity playerEntity, boolean keepEverything, CallbackInfo info)
	{

	}
}
