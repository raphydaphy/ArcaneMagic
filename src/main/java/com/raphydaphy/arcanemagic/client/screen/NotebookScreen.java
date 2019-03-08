package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.NotebookUpdatePacket;
import com.raphydaphy.arcanemagic.notebook.ContentsNotebookSection;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InputListener;
import net.minecraft.client.gui.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NotebookScreen extends Screen
{
	private INotebookSection section;
	private int leftPage = 0;
	private int scaledMouseX = 0;
	private int scaledMouseY = 0;

	private List<INotebookElement> leftElements = new ArrayList<>();
	private List<INotebookElement> rightElements = new ArrayList<>();

	public NotebookScreen(ItemStack stack)
	{
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.containsKey(ArcaneMagicConstants.NOTEBOOK_SECTION_KEY))
		{
			INotebookSection section = NotebookSectionRegistry.get(Identifier.create(tag.getString(ArcaneMagicConstants.NOTEBOOK_SECTION_KEY)));
			int page = tag.getInt(ArcaneMagicConstants.NOTEBOOK_PAGE_KEY);

			if (section != null)
			{
				setSection(section, false);
				if (page <= section.getPageCount())
				{
					this.leftPage = page;
					pageChanged(false);
				}
				return;
			}
		}

		ArcaneMagic.getLogger().warn("Tried to open a notebook with invalid NBT !");
		setSection(NotebookSectionRegistry.CONTENTS, false);
	}

	private void setSection(INotebookSection section, boolean sync)
	{
		this.leftPage = 0;
		this.section = section;
		this.leftElements.clear();
		this.rightElements.clear();

		this.leftElements = this.section.getElements(0);
		this.rightElements = this.section.getElements(1);

		if (sync)
		{
			ArcaneMagicPacketHandler.sendToServer(new NotebookUpdatePacket(this.section.getID().toString(), leftPage));
		}
	}

	private void pageChanged(boolean sync)
	{
		this.leftElements.clear();
		this.rightElements.clear();

		this.leftElements = this.section.getElements(this.leftPage);
		this.rightElements = this.section.getElements(this.leftPage + 1);

		if (sync)
		{
			ArcaneMagicPacketHandler.sendToServer(new NotebookUpdatePacket(section.getID().toString(), leftPage));
		}
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
					if ((leftPage + 1 < section.getPageCount() && overRightArrow()) || (leftPage > 0 && overLeftArrow()) || (!(section instanceof ContentsNotebookSection) && overBackArrow()))
					{
						client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
						return true;
					} else if (isMouseOverAny(leftElements) || isMouseOverAny(rightElements))
					{
						client.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.5f, 1);
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
					if (leftPage + 1 < section.getPageCount() && overRightArrow())
					{
						leftPage += 2;
						pageChanged(true);
						return true;
					} else if (leftPage > 0 && overLeftArrow())
					{
						leftPage -= 2;
						if (leftPage < 0)
						{
							leftPage = 0;
						}
						pageChanged(true);
						return true;
					} else if (!(section instanceof ContentsNotebookSection) && overBackArrow())
					{
						setSection(NotebookSectionRegistry.CONTENTS, true);
						return true;
					}
					return handleClickOn(leftElements) || handleClickOn(rightElements);
				}
				return false;
			}
		});
	}

	private boolean isMouseOverAny(List<INotebookElement> elements)
	{
		for (INotebookElement element : elements)
		{
			if (element.mouseOver(scaledMouseX, scaledMouseY))
			{
				return true;
			}
		}
		return false;
	}

	private boolean handleClickOn(List<INotebookElement> elements)
	{
		for (INotebookElement element : elements)
		{
			INotebookSection s = element.handleClick(scaledMouseX, scaledMouseY);
			if (s != null)
			{
				setSection(s, true);
				return true;
			}
		}
		return false;
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

	private boolean overBackArrow()
	{
		//right + 85, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21
		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int right = xTop + 142;
		return scaledMouseX >= right - 15 && scaledMouseY >= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21 && scaledMouseX <= right && scaledMouseY <= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 10;
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

		if (section instanceof ContentsNotebookSection)
		{
			DrawableHelper.drawTexturedRect(xTop + 133, yTop + 156, 136, 180, 5, 11, 5, 11, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}

		// Intro page
		int pointer = yTop + 15;
		for (INotebookElement element : this.leftElements)
		{
			GlStateManager.pushMatrix();
			pointer += element.draw(this, left, pointer, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		pointer = yTop + 15;
		for (INotebookElement element : this.rightElements)
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

		if (!(section instanceof ContentsNotebookSection))
		{
			RenderUtils.drawTexturedRect(right - 15, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21, overBackArrow() ? 66 : 46, 193, 15, 11, 15, 11, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
