package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.render.MixerRenderer;
import com.raphydaphy.arcanemagic.util.TremorTracker;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private Camera camera;

    @Inject(at = @At(value = "INVOKE_STRING", args = "ldc=hand", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"), method = "renderCenter")
    private void renderAfterWorld(float partialTicks, long finishTimeNano, CallbackInfo info) {
        ParticleRenderer.INSTANCE.render(partialTicks, camera);
        MixerRenderer.renderMixers();
    }

    @Inject(at = @At(value = "INVOKE_STRING", args = "ldc=prepareterrain", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"), method = "renderCenter")
    private void renderBeforeWorld(float partialTicks, long finishTimeNano, CallbackInfo info) {
        TremorTracker.renderTremors();
    }
}
