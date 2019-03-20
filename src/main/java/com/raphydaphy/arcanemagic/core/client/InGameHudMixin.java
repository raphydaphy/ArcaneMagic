package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.render.HudRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
	@Inject(at = @At(value = "TAIL"), method = "draw")
	private void worldRenderTick(float partialTicks, CallbackInfo info)
	{
		HudRenderer.render(partialTicks);
	}
}
