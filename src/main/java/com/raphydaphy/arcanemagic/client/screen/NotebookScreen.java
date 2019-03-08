package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.notebook.CategoriesNotebookPage;
import com.raphydaphy.arcanemagic.notebook.TitleNotebookPage;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class NotebookScreen extends Screen
{

	public NotebookScreen(ItemStack stack)
	{
	}

	@Override
	protected void onInitialized()
	{
		super.onInitialized();
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks)
	{
		this.drawBackground();
		super.draw(mouseX, mouseY, partialTicks);

		GlStateManager.pushMatrix();

		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int left = xTop + 17;
		int right = xTop + 142;

		client.getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
		DrawableHelper.drawTexturedRect(xTop, yTop, 0, 0, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_HEIGHT, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_HEIGHT, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);

		// Intro page
		int pointer = yTop + 15;
		for (INotebookElement element : TitleNotebookPage.INSTANCE.getElements())
		{
			GlStateManager.pushMatrix();
			pointer += element.draw(this, left, pointer, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		pointer = yTop + 15;
		for (INotebookElement element : CategoriesNotebookPage.INSTANCE.getElements())
		{
			GlStateManager.pushMatrix();
			pointer += element.draw(this, right, pointer, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		client.getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
		boolean overRightArrow = mouseX >= right + 85 && mouseY >= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21 && mouseX <= right + 103 && mouseY <= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 11;
		RenderUtils.drawTexturedRect(right + 85, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21, overRightArrow ? 23 : 0, 180, 18, 10, 18, 10, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);

		GlStateManager.popMatrix();
	}

	private int drawHeader(int xTop, int yTop, String unlocalized, Object... keys)
	{
		client.getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
		int height = RenderUtils.drawTexturedRect(xTop - 8, yTop + 11, 0, 206, 140, 31, 140, 30, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT) - 3;
		RenderUtils.drawCustomSizedSplitString(xTop + 17 - 8, yTop + 18, 1, 119, 0x218e15, false, false, unlocalized, keys);
		return height;
	}

	private int drawItemBoxWithText(int x, int y, int mouseX, int mouseY, ItemProvider item, String unlocalizedTitle, String unlocalizedDesc)
	{
		int largest = 0;
		int tmp = RenderUtils.drawItemInBox(this, new ItemStack(item), null, x, y, mouseX, mouseY);
		largest = tmp > largest ? tmp : largest;
		tmp = RenderUtils.drawCustomSizedSplitString(x + 29, y + 2, 0.9, 80, 0, false, false, unlocalizedTitle) + 3;
		tmp += RenderUtils.drawCustomSizedSplitString(x + 29, y + 2 + tmp, 0.7, 80, 0, false, false, unlocalizedDesc);
		return tmp > largest ? tmp : largest;
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
