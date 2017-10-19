package com.raphydaphy.arcanemagic.client.toast;

import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CategoryUnlockedToast implements IToast
{
	private final NotebookCategory unlocked;
	private final boolean expanded;
	private boolean hasPlayedSound = false;

	public CategoryUnlockedToast(NotebookCategory unlocked, boolean expanded)
	{
		this.unlocked = unlocked;
		this.expanded = expanded;
	}

	public IToast.Visibility draw(GuiToast toastGui, long delta)
	{
		toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

		toastGui.getMinecraft().fontRenderer.drawString(I18n.format(expanded ? "toast.arcanemagic.page_expanded" : "toast.arcanemagic.page_unlocked"), 30, 7,
				0x5bc14d);
		toastGui.getMinecraft().fontRenderer.drawString(I18n.format(unlocked.getUnlocalizedName()), 30, 18, 0x65a595);

		if (!this.hasPlayedSound && delta > 0L)
		{
			this.hasPlayedSound = true;

			toastGui.getMinecraft().getSoundHandler()
					.playSound(PositionedSoundRecord.getRecord(ArcaneMagicSoundHandler.learn, 1.0F, 1.0F));

		}

		RenderHelper.enableGUIStandardItemLighting();
		toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase) null, unlocked.getIcon(),
				8, 8);
		return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
	}
}