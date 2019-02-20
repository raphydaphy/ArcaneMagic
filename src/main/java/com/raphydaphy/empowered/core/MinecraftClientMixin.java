package com.raphydaphy.empowered.core;

import com.raphydaphy.empowered.Empowered;
import com.raphydaphy.empowered.client.ClientEvents;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;profiler:Lnet/minecraft/util/profiler/DisableableProfiler;"), method = "render")
	private void worldRenderTick(boolean renderWorldIn, CallbackInfo info)
	{
		ClientEvents.onRenderTick();
	}
}
