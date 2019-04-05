package com.raphydaphy.arcanemagic.notebook;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.NotebookElement;
import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

class BasicNotebookElements
{
	static int textPages(String content, int headingSize)
	{
		double scale = textScale();
		int lines = linesPerPage();
		return (int) Math.ceil(((MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(I18n.translate(content), (int) (116 / scale)).size() - (lines - headingSize))) / (float) lines);
	}

	private static int linesPerPage()
	{
		int guiScale = (int) MinecraftClient.getInstance().window.getScaleFactor();
		;
		switch (guiScale)
		{
			case 3:
				return 18;
			case 4:
				return 21;
			default:
				return 15;
		}
	}

	private static double textScale()
	{
		int guiScale = (int) MinecraftClient.getInstance().window.getScaleFactor();
		;
		switch (guiScale)
		{
			case 4:
				return 0.7f;
			case 3:
				return 0.85f;
			default:
				return 1;
		}
	}

	static List<NotebookElement> wrapText(String text, int headingSize, int startPage, int curPage)
	{
		List<NotebookElement> elements = new ArrayList<>();
		if (curPage >= startPage)
		{
			double scale = textScale();
			int width = (int) (116 / scale);
			List<String> wrapped = RenderUtils.wrapText(text, width);

			int page = curPage - startPage;
			int lines = linesPerPage();

			int start = page == 0 ? 0 : page * lines - headingSize;

			StringBuilder builder = new StringBuilder();
			int size = 0;
			for (int i = start; i < start + (page == 0 ? (lines - headingSize) : lines); i++)
			{
				if (i < wrapped.size())
				{
					String line = wrapped.get(i);
					if (line.isEmpty())
					{
						String paragraph = builder.toString();
						if (!paragraph.isEmpty() || size > 0)
						{
							elements.add(new BasicNotebookElements.Paragraph(false, scale, 116, paragraph).withPadding(MinecraftClient.getInstance().textRenderer.fontHeight));
							builder = new StringBuilder();
							size++;
						}
					} else
					{
						builder.append(line);
						builder.append(" ");
					}
				} else
				{
					break;
				}
			}

			String last = builder.toString();
			if (!last.isEmpty())
			{
				elements.add(new BasicNotebookElements.Paragraph(false, scale, 116, last));
			}
		}
		return elements;
	}

	public static class BigHeading extends Writable
	{
		BigHeading(String unlocalized, Object... keys)
		{
			super(unlocalized, keys);
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			MinecraftClient.getInstance().getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
			RenderUtils.texRect(xTop - 8, yTop + 11, 0, 206, 140, 31, 140, 30, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
			RenderUtils.drawCustomSizedSplitString(xTop + 17 - 8, yTop + 18, 1, 119, 0x218e15, false, false, unlocalized, keys);
			return 27 + padding;
		}
	}

	public static class SmallHeading extends Writable
	{
		SmallHeading(String unlocalized, Object... keys)
		{
			super(unlocalized, keys);
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			int height = RenderUtils.drawCustomSizedSplitString(x + 57, y, 1.2, 116, 0, false, true, unlocalized) + 3;
			MinecraftClient.getInstance().getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
			height += RenderUtils.texRect(x + 14, y + height, 46, 180, 85, 3, 85, 3, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT) + 3;
			return height + padding;
		}
	}

	public static class Paragraph extends Writable
	{
		private final boolean centered;
		private final double scale;
		private final int width;

		Paragraph(boolean centered, double scale, String unlocalized, Object... keys)
		{
			this(centered, scale, 116, unlocalized, keys);
		}

		Paragraph(boolean centered, double scale, int width, String unlocalized, Object... keys)
		{
			super(unlocalized, keys);
			this.centered = centered;
			this.scale = scale;
			this.width = width;
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			return RenderUtils.drawCustomSizedSplitString(x + (centered ? 57 : 0), y, scale, width, 0, false, centered, unlocalized, keys) + padding;
		}
	}

	public static class ItemInfoButton extends ItemInfo
	{
		final NotebookSection link;

		private int startX;
		private int startY;
		private int endX;
		private int endY;

		ItemInfoButton(NotebookSection link, ItemProvider item, String unlocalizedTitle, String unlocalizedDesc, Object... descKeys)
		{
			super(item, unlocalizedTitle, unlocalizedDesc, descKeys);
			this.link = link;
		}

		@Override
		public boolean mouseOver(int mouseX, int mouseY)
		{
			return mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY;
		}

		@Override
		public NotebookSection handleClick(int mouseX, int mouseY)
		{
			if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY)
			{
				return link;
			}
			return null;
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			this.startX = x;
			this.startY = y;
			this.endX = startX + 26;
			this.endY = startY + 26;

			if (this.link.hasNewInfo((DataHolder) MinecraftClient.getInstance().player))
			{
				if (mouseOver(mouseX, mouseY))
				{
					this.color = 0xff17a836;
				} else
				{
					this.color = 0xff14912f;
				}
			} else
			{
				if (mouseOver(mouseX, mouseY))
				{
					this.color = 0xff634316;
				} else
				{
					this.color = 0xff422c0e;
				}
			}

			return super.draw(screen, x, y, mouseX, mouseY, xTop, yTop);
		}
	}

	public static class ItemInfo extends Writable
	{
		private final ItemProvider item;
		private final String unlocalizedTitle;
		int color = -1;

		ItemInfo(ItemProvider item, String unlocalizedTitle, String unlocalizedDesc, Object... descKeys)
		{
			super(unlocalizedDesc, descKeys);
			this.item = item;
			this.unlocalizedTitle = unlocalizedTitle;
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			int largest = 0;
			int tmp = RenderUtils.drawItemInBox(screen, new ItemStack(item), null, color, x, y, mouseX, mouseY);
			largest = tmp > largest ? tmp : largest;
			tmp = RenderUtils.drawCustomSizedSplitString(x + 29, y + 2, 0.9, 80, 0, false, false, unlocalizedTitle) + 3;
			tmp += RenderUtils.drawCustomSizedSplitString(x + 29, y + 2 + tmp, 0.7, 80, 0, false, false, unlocalized, keys);
			return (tmp > largest ? tmp : largest) + padding;
		}
	}

	public static class Padding implements NotebookElement
	{
		private final int amount;

		Padding(int amount)
		{
			this.amount = amount;
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			return amount;
		}
	}

	public static class Recipe implements NotebookElement
	{
		private final net.minecraft.recipe.Recipe<? extends Inventory> recipe;
		private int startX;
		private int startY;

		public Recipe(net.minecraft.recipe.Recipe<? extends Inventory> recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			if (this.recipe != null)
			{
				if (this.recipe instanceof TransfigurationRecipe)
				{
					GlStateManager.pushMatrix();

					MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(ArcaneMagic.DOMAIN, "textures/misc/circle_hud.png"));

					float percent = ((float) ((TransfigurationRecipe) this.recipe).getSoul() / (ArcaneMagicConstants.SOUL_METER_MAX));
					int stage = Math.round(percent * ArcaneMagicConstants.SOUL_METER_STAGES);
					int row = stage / 10;
					int col = stage % 10;
					RenderUtils.texRect(x + 36, y, 0, 0, 36, 36, 36, 36, 360, 360);
					RenderUtils.texRect(x + 36, y, 36 * col, 36 + 36 * row, 36, 36, 36, 36, 360, 360);

					GuiLighting.enableForItems();
					MinecraftClient.getInstance().getItemRenderer().renderGuiItem(this.recipe.getOutput(), x + 46, y + 10);
					MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, this.recipe.getOutput(), x + 46, y + 10, null);
					GuiLighting.disable();

					GlStateManager.popMatrix();
				} else
				{
					RenderUtils.drawRecipeOutput(this.recipe, x + 41, y);
				}

				RenderUtils.drawRecipeItems(this.recipe, x + 20, y + (this.recipe instanceof TransfigurationRecipe ? 46 : 40));
				this.startX = x;
				this.startY = y;
				return this.recipe instanceof TransfigurationRecipe ? 117 : 111;
			}
			return 0;
		}

		@Override
		public void drawOverlay(Screen screen, int mouseX, int mouseY, int xTop, int yTop)
		{
			if (this.recipe != null)
			{
				RenderUtils.drawRecipeTooltips(screen, this.recipe, this.startX + 20, this.startY + (this.recipe instanceof TransfigurationRecipe ? 46 : 40), mouseX, mouseY);
				RenderUtils.drawItemstackTooltip(screen, recipe.getOutput(), this.startX + 46, this.startY + (this.recipe instanceof TransfigurationRecipe ? 10 : 5), mouseX, mouseY);
			}
		}
	}

	private static abstract class Writable implements NotebookElement
	{
		final String unlocalized;
		final Object[] keys;
		int padding = 0;

		private Writable(String unlocalized, Object... keys)
		{
			this.unlocalized = unlocalized;
			this.keys = keys;
		}

		Writable withPadding(int padding)
		{
			this.padding = padding;
			return this;
		}
	}
}
