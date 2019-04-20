package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.ClientEvents;
import com.raphydaphy.arcanemagic.init.ModCutscenes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    public ClientWorld world;

    @Inject(at = @At("RETURN"), method="<init>")
    private void constructor(RunArgs args, CallbackInfo info) {
        ModCutscenes.preInitClient();
    }

    @Inject(at = @At(value = "INVOKE_STRING", args = "ldc=gameRenderer", target = "Lnet/minecraft/util/profiler/DisableableProfiler;swap(Ljava/lang/String;)V"), method = "render")
    private void worldRenderTick(boolean renderWorldIn, CallbackInfo info) {
        if (world != null) {
            ClientEvents.onRenderTick();
        }
    }
}
