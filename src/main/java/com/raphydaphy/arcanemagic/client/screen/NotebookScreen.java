package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.NotebookElement;
import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.network.NotebookSectionReadPacket;
import com.raphydaphy.arcanemagic.network.NotebookUpdatePacket;
import com.raphydaphy.arcanemagic.notebook.ContentsNotebookSection;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import com.raphydaphy.crochet.data.DataHolder;
import com.raphydaphy.crochet.network.PacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NotebookScreen extends Screen
{
	private NotebookSection section;
	private int leftPage = 0;
	private int contentsPage = 0;
	private int scaledMouseX = 0;
	private int scaledMouseY = 0;

	private List<NotebookElement> leftElements = new ArrayList<>();
	private List<NotebookElement> rightElements = new ArrayList<>();

	public NotebookScreen(ItemStack stack)
	{
		super(new TranslatableTextComponent("item.arcanemagic.notebook"));
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.containsKey(ArcaneMagicConstants.NOTEBOOK_SECTION_KEY))
		{
			NotebookSection section = NotebookSectionRegistry.get(Identifier.create(tag.getString(ArcaneMagicConstants.NOTEBOOK_SECTION_KEY)));
			int page = tag.getInt(ArcaneMagicConstants.NOTEBOOK_PAGE_KEY);
			int contentsPage = tag.getInt(ArcaneMagicConstants.NOTEBOOK_CONTENTS_PAGE_KEY);

			this.section = section;

			if (section != null)
			{
				this.leftPage = page;
				this.contentsPage = contentsPage;
			}
		}
	}

	private void setSection(NotebookSection section)
	{
		MinecraftClient client = MinecraftClient.getInstance();
		this.leftPage = 0;
		if (!section.isVisibleTo((DataHolder) client.player))
		{
			this.section = NotebookSectionRegistry.CONTENTS;
		} else
		{
			this.section = section;
		}

		if (this.section == NotebookSectionRegistry.CONTENTS)
		{
			this.leftPage = contentsPage;
		}

		if (this.section.hasNewInfo((DataHolder) client.player))
		{
			PacketHandler.sendToServer(new NotebookSectionReadPacket(this.section));
		}

		pageChanged();
	}

	private void pageChanged()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		if (leftPage > section.getPageCount((DataHolder) client.player))
		{
			leftPage -= 2;
			pageChanged();
		}
		if (this.section == NotebookSectionRegistry.CONTENTS)
		{
			this.contentsPage = leftPage;
		}
		this.leftElements.clear();
		this.rightElements.clear();

		this.leftElements = this.section.getElements((DataHolder) client.player, this.leftPage);
		this.rightElements = this.section.getElements((DataHolder) client.player, this.leftPage + 1);
	}

	@Override
	protected void init()
	{
		super.init();
		MinecraftClient client = MinecraftClient.getInstance();
		if (section != null)
		{
			int page = leftPage;
			setSection(section);
			if (page != leftPage)
			{
				this.leftPage = page;
				pageChanged();
			}
		} else
		{
			ArcaneMagic.getLogger().warn("Tried to open a notebook with invalid NBT !");
			setSection(NotebookSectionRegistry.CONTENTS);
		}
		this.children.add(new Element()
		{
			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button)
			{
				if (button == 0)
				{
					if (leftPage + 1 < section.getPageCount((DataHolder) client.player) && overRightArrow())
					{
						leftPage += 2;
						pageChanged();
						client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
						return true;
					} else if (leftPage > 0 && overLeftArrow())
					{
						leftPage -= 2;
						if (leftPage < 0)
						{
							leftPage = 0;
						}
						pageChanged();
						client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
						return true;
					} else if (!(section instanceof ContentsNotebookSection) && overBackArrow())
					{
						setSection(NotebookSectionRegistry.CONTENTS);
						client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
						return true;
					} else if (handleClickOn(leftElements) || handleClickOn(rightElements))
					{
						client.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.5f, 1);
						return true;
					}
				} else if (button == 1)
				{
					if (section != NotebookSectionRegistry.CONTENTS)
					{
						setSection(NotebookSectionRegistry.CONTENTS);
						client.player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
						return true;
					}
				}
				return false;
			}
		});
	}

	private boolean isMouseOverAny(List<NotebookElement> elements)
	{
		for (NotebookElement element : elements)
		{
			if (element.mouseOver(scaledMouseX, scaledMouseY))
			{
				return true;
			}
		}
		return false;
	}

	private boolean handleClickOn(List<NotebookElement> elements)
	{
		for (NotebookElement element : elements)
		{
			NotebookSection s = element.handleClick(scaledMouseX, scaledMouseY);
			if (s != null)
			{
				setSection(s);
				return true;
			}
		}
		return false;
	}

	private boolean overRightArrow()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int right = xTop + 142;

		return scaledMouseX >= right + 85 && scaledMouseY >= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21 && scaledMouseX <= right + 103 && scaledMouseY <= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 11;
	}

	private boolean overLeftArrow()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int left = xTop + 17;

		return scaledMouseX >= left + 10 && scaledMouseY >= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21 && scaledMouseX <= left + 28 && scaledMouseY <= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 11;
	}

	private boolean overBackArrow()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		//right + 85, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21
		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int right = xTop + 142;
		return scaledMouseX >= right - 15 && scaledMouseY >= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21 && scaledMouseX <= right && scaledMouseY <= yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 10;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		MinecraftClient client = MinecraftClient.getInstance();
		super.render(mouseX, mouseY, partialTicks);

		this.scaledMouseX = mouseX;
		this.scaledMouseY = mouseY;

		GlStateManager.pushMatrix();

		int xTop = (client.window.getScaledWidth() / 2) - (ArcaneMagicConstants.NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (ArcaneMagicConstants.NOTEBOOK_HEIGHT / 2);

		int left = xTop + 17;
		int right = xTop + 142;

		client.getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
		RenderUtils.texRect(xTop, yTop, 0, 0, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_HEIGHT, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_HEIGHT, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);

		if (section instanceof ContentsNotebookSection)
		{
			RenderUtils.texRect(xTop + 133, yTop + 156, 136, 180, 5, 11, 5, 11, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}

		// Intro page
		int pointer = yTop + 15;
		for (NotebookElement element : this.leftElements)
		{
			GlStateManager.pushMatrix();
			pointer += element.draw(this, left, pointer, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		pointer = yTop + 15;
		for (NotebookElement element : this.rightElements)
		{
			GlStateManager.pushMatrix();
			pointer += element.draw(this, right, pointer, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		client.getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);

		if (leftPage + 1 < section.getPageCount((DataHolder) client.player))
		{
			RenderUtils.texRect(right + 85, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21, overRightArrow() ? 23 : 0, 180, 18, 10, 18, 10, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}

		if (leftPage > 0)
		{
			RenderUtils.texRect(left + 10, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21, overLeftArrow() ? 23 : 0, 193, 18, 10, 18, 10, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}

		if (!(section instanceof ContentsNotebookSection))
		{
			RenderUtils.texRect(right - 15, yTop + ArcaneMagicConstants.NOTEBOOK_HEIGHT - 21, overBackArrow() ? 66 : 46, 193, 15, 11, 15, 11, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
		}

		for (NotebookElement element : this.leftElements)
		{
			GlStateManager.pushMatrix();
			element.drawOverlay(this, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		for (NotebookElement element : this.rightElements)
		{
			GlStateManager.pushMatrix();
			element.drawOverlay(this, mouseX, mouseY, xTop, yTop);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void removed()
	{
		if (this.section != null)
		{
			PacketHandler.sendToServer(new NotebookUpdatePacket(this.section.getID().toString(), this.leftPage, this.contentsPage));
		}
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
