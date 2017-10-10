package com.raphydaphy.arcanemagic.client.toast;

import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CategoryUnlockedToast implements IToast
{
	private final NotebookCategory unlocked;
	private boolean hasPlayedSound = false;

	public CategoryUnlockedToast(NotebookCategory unlocked)
	{
		this.unlocked = unlocked;
	}

	public IToast.Visibility draw(GuiToast toastGui, long delta)
	{
		toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

		int i = 16746751;

		toastGui.getMinecraft().fontRenderer.drawString(I18n.format("toast.arcanemagic.page_unlocked"), 30, 7, i | -16777216);
		toastGui.getMinecraft().fontRenderer.drawString(I18n.format(unlocked.getUnlocalizedName()), 30, 18, -1);

		if (!this.hasPlayedSound && delta > 0L)
		{
			this.hasPlayedSound = true;

			toastGui.getMinecraft().getSoundHandler()
					.playSound(PositionedSoundRecord.getRecord(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));

		}

		RenderHelper.enableGUIStandardItemLighting();
		toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase) null,
				new ItemStack(ModRegistry.WRITING_DESK), 8, 8);
		return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
	}
}