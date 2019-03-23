package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.render.HudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(at = @At(value = "TAIL"), method = "draw")
	private void renderHud(float partialTicks, CallbackInfo info)
	{
		HudRenderer.render(partialTicks);
	}
}
