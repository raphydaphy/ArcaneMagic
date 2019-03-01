package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Inject(at = @At(value = "INVOKE_STRING", args = "ldc=hand", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"), method = "renderCenter")
	private void renderCenter(float partialTicks, long finishTimeNano, CallbackInfo info)
	{
		ParticleRenderer.INSTANCE.render(partialTicks);
	}
}
