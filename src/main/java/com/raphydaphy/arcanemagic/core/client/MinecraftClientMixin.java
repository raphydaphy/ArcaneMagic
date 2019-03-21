package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.ClientEvents;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.CutsceneManager;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@Shadow public ClientPlayerEntity player;

	@Inject(at = @At(value = "INVOKE_STRING", args = "ldc=gameRenderer", target = "Lnet/minecraft/util/profiler/DisableableProfiler;swap(Ljava/lang/String;)V"), method = "render")
	private void worldRenderTick(boolean renderWorldIn, CallbackInfo info)
	{
		ClientEvents.onRenderTick();
	}

	@Inject(at = @At(value = "INVOKE_STRING", args = "ldc=render", target = "Lnet/minecraft/util/profiler/DisableableProfiler;push(Ljava/lang/String;)V"), method = "render", cancellable = true)
	private void render(boolean something, CallbackInfo info)
	{
		if (player != null)
		{
			DataHolder dataPlayer = (DataHolder) player;

			if (dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.WATCHING_CUTSCENE_KEY))
			{
				CutsceneManager.render();
				info.cancel();
			}
		}
	}
}
