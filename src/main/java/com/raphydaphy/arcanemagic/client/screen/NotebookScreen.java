package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.notebook.ContentsNotebookSection;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InputListener;
import net.minecraft.client.gui.Screen;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

@Environment(EnvType.CLIENT)
public class NotebookScreen extends Screen
{
	private INotebookSection section;
	private int leftPage = 0;
	private int scaledMouseX = 0;
	private int scaledMouseY = 0;

	public NotebookScreen(ItemStack stack)
	{
		section = ContentsNotebookSection.INSTANCE;
	}

	@Override
	protected void onInitialized()
	{
		super.onInitialized();
		this.listeners.add(new InputListener()
		{
			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button)
			{
				if (button == 0)
				{
					if (overRightArrow() && leftPage + 1 < section.getPageCount())
					{
						client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
						return true;
					} else if (overLeftArrow() && leftPage > 0)
					{
						client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean mouseReleased(double mouseX, double mouseY, int button)
			{
				if (button == 0)
				{
					if (overRightArrow() && leftPage + 1 < section.getPageCount())
					{
						leftPage += 2;
						return true;
					} else if (overLeftArrow() && leftPage > 0)
					{
						leftPage -= 2;
						if (leftPage < 0)
						{
							leftPage = 0;
						}
						return true;
					}
				}
				return false;
			}
		});
	}

	private boolean overRightArrow()
	{
		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int right = xTop + 142;

		return scaledMouseX >= right + 85 && scaledMouseY >= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21 && scaledMouseX <= right + 103 && scaledMouseY <= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 11;
	}

	private boolean overLeftArrow()
	{
		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int left = xTop + 17;

		return scaledMouseX >= left + 10 && scaledMouseY >= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21 && scaledMouseX <= left + 28 && scaledMouseY <= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 11;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks)
	{
		this.drawBackground();
		super.draw(mouseX, mouseY, partialTicks);

		this.scaledMouseX = mouseX;
		this.scaledMouseY = mouseY;

		GlStateManager.pushMatrix();

		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int left = xTop + 17;
		int right = xTop + 142;

		client.getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
		DrawableHelper.drawTexturedRect(xTop, yTop, 0, 0, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_HEIGHT, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_HEIGHT, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);

		// Intro page
		int pointer = yTop + 15;
		for (INotebookElement element : section.getElements(leftPage))
		{
			GlStateManager.pushMatrix();
			pointer += element.draw(this, left, pointer, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		pointer = yTop + 15;
		for (INotebookElement element : section.getElements(leftPage + 1))
		{
			GlStateManager.pushMatrix();
			pointer += element.draw(this, right, pointer, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		client.getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);

		if (leftPage + 1 < section.getPageCount())
		{
			RenderUtils.drawTexturedRect(right + 85, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21, overRightArrow() ? 23 : 0, 180, 18, 10, 18, 10, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}

		if (leftPage > 0)
		{
			RenderUtils.drawTexturedRect(left + 10, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21, overLeftArrow() ? 23 : 0, 193, 18, 10, 18, 10, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
