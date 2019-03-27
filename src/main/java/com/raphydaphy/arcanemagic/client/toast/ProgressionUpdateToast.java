package com.raphydaphy.arcanemagic.client.toast;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.ItemStack;

public class ProgressionUpdateToast implements Toast
{
	private boolean notebook = false;

	public ProgressionUpdateToast(boolean notebook)
	{
		this.notebook = notebook;
	}

	@Override
	public Visibility draw(ToastManager manager, long time)
	{
		manager.getGame().getTextureManager().bindTexture(TOASTS_TEX);
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		manager.blit(0, 0, 0, 0, 160, 32);
		manager.getGame().textRenderer.draw(I18n.translate(notebook ? "toast.arcanemagic.notebook_update" : "toast.arcanemagic.parchment_update"), 30.0F, 12.0F, 0x978b52);

		GuiLighting.enableForItems();
		manager.getGame().getItemRenderer().renderGuiItem(null, new ItemStack(notebook ? ModRegistry.NOTEBOOK : ModRegistry.WRITTEN_PARCHMENT), 8, 8);
		return time >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;

	}
}
