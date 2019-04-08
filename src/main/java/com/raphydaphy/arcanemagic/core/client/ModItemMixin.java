package com.raphydaphy.arcanemagic.core.client;

import io.github.prospector.modmenu.gui.ModItem;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModItem.class)
public abstract class ModItemMixin
{
	@Shadow public int badgeX;

	@Shadow public int badgeY;

	@Shadow @Final private MinecraftClient client;

	@Shadow public abstract void drawBadge(String text, int outlineColor, int fillColor);

	@Shadow public ModMetadata metadata;

	@Shadow public ModContainer container;

	@Inject(at = @At(value="INVOKE_ASSIGN", target="Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), method="render")
	private void render(int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float delta, CallbackInfo info)
	{
		if (container.getMetadata().getId().equals("arcanemagic"))
		{
			this.badgeX = x + 32 + 3 + this.client.textRenderer.getStringWidth(this.metadata.getName()) + 2;
			this.badgeY = y;
			this.drawBadge("Magic", 0xFF6847cc, 0xFF4416ce);
		}
	}
}
