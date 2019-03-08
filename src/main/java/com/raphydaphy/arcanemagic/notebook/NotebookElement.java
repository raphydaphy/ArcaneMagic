package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Screen;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;

public class NotebookElement
{
	public static class BigHeading extends Writable
	{
		public BigHeading(String unlocalized, Object... keys)
		{
			super(unlocalized, keys);
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			MinecraftClient.getInstance().getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
			DrawableHelper.drawTexturedRect(xTop - 8, yTop + 11, 0, 206, 140, 31, 140, 30, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT);
			RenderUtils.drawCustomSizedSplitString(xTop + 17 - 8, yTop + 18, 1, 119, 0x218e15, false, false, unlocalized, keys);
			return 27 + padding;
		}
	}

	public static class SmallHeading extends Writable
	{
		public SmallHeading(String unlocalized, Object... keys)
		{
			super(unlocalized, keys);
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			int height = RenderUtils.drawCustomSizedSplitString(x + 57, y, 1, 116, 0, false, true, unlocalized) + 3;
			MinecraftClient.getInstance().getTextureManager().bindTexture(ArcaneMagicConstants.NOTEBOOK_TEXTURE);
			height += RenderUtils.drawTexturedRect(x + 14, y + height, 46, 180, 85, 3, 85, 3, ArcaneMagicConstants.NOTEBOOK_WIDTH, ArcaneMagicConstants.NOTEBOOK_TEX_HEIGHT) + 3;
			return height + padding;
		}
	}

	public static class Paragraph extends Writable
	{
		public final boolean centered;

		public Paragraph(boolean centered, String unlocalized, Object... keys)
		{
			super(unlocalized, keys);
			this.centered = centered;
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			return RenderUtils.drawCustomSizedSplitString(x, y, 0.7, 116, 0, false, false, unlocalized, keys) + padding;
		}
	}

	public static class ItemInfo extends Writable
	{
		public final ItemProvider item;
		public final String unlocalizedTitle;

		public ItemInfo(ItemProvider item, String unlocalizedTitle, String unlocalizedDesc, Object... descKeys)
		{
			super(unlocalizedDesc, descKeys);
			this.item = item;
			this.unlocalizedTitle = unlocalizedTitle;
		}

		@Override
		public int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop)
		{
			int largest = 0;
			int tmp = RenderUtils.drawItemInBox(screen, new ItemStack(item), null, x, y, mouseX, mouseY);
			largest = tmp > largest ? tmp : largest;
			tmp = RenderUtils.drawCustomSizedSplitString(x + 29, y + 2, 0.9, 80, 0, false, false, unlocalizedTitle) + 3;
			tmp += RenderUtils.drawCustomSizedSplitString(x + 29, y + 2 + tmp, 0.7, 80, 0, false, false, unlocalized, keys);
			return (tmp > largest ? tmp : largest) + padding;
		}
	}

	public static abstract class Writable implements INotebookElement
	{
		final String unlocalized;
		final Object[] keys;
		int padding = 0;

		private Writable(String unlocalized, Object... keys)
		{
			this.unlocalized = unlocalized;
			this.keys = keys;
		}

		public Writable withPadding(int padding)
		{
			this.padding = padding;
			return this;
		}
	}
}
