package com.raphydaphy.arcanemagic.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.util.Pos2;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNotebook extends GuiScreen
{
	public static final int FRAME_WIDTH = 344;
	public static final int FRAME_HEIGHT = 230;
	public static final int FRAME_TEX_HEIGHT = 256;

	public static final int INNER_BG_WIDTH = 225;
	public static final int INNER_BG_HEIGHT = 195;

	public static final String tagUsedNotebook = "usedNotebook";
	public static final String tagPageX = "notebookPageX";
	public static final String tagPageY = "notebookPageY";
	public static final String tagTab = "notebookTab";

	private EntityPlayer player;

	private int lastDragX = 0;
	private int lastDragY = 0;

	private int relMouseX = 0;
	private int relMouseY = 0;

	private static final ResourceLocation frame = new ResourceLocation(ArcaneMagic.MODID, "textures/gui/notebook.png");
	private static final ResourceLocation page = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/thaumonomicon_page.png");

	public GuiNotebook(EntityPlayer player)
	{
		this.player = player;

		if (player.getEntityData().getBoolean(tagUsedNotebook) == false)
		{
			System.out.println("Player opened Notebook for first time, doing initial setup.");
			// player.getEntityData().setBoolean(tagUsedNotebook, true);
			player.getEntityData().setInteger(tagPageX, 200);
			player.getEntityData().setInteger(tagPageY, 200);
			player.getEntityData().setInteger(tagTab, 0);
		}
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.setGuiSize(mc.displayWidth, mc.displayHeight);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		ScaledResolution res = new ScaledResolution(mc);

		relMouseX = mouseX;
		relMouseY = mouseY;

		int pageX = (int) player.getEntityData().getFloat(tagPageX);
		int pageY = (int) player.getEntityData().getFloat(tagPageY);
		float screenX = (res.getScaledWidth() / 2) - (FRAME_WIDTH / 2);
		float screenY = (res.getScaledHeight() / 2) - (FRAME_HEIGHT / 2);

		GlStateManager.pushMatrix();
		GlStateManager.viewport(((res.getScaledWidth() / 2) - (FRAME_WIDTH / 2)) + 80, 75, (((res.getScaledWidth() / 2) - (FRAME_WIDTH / 2) + FRAME_WIDTH) - 100) * 2,
				 ((res.getScaledHeight() / 2) - (FRAME_HEIGHT / 2) + FRAME_HEIGHT) * 2);
		GlStateManager.scale(1.985, 1.55, 1.55);
		//GlStateManager.translate((res.getScaledWidth() / 2) - (FRAME_WIDTH / 2),
		//		 (res.getScaledHeight() / 2) - (FRAME_HEIGHT / 2), 0);
		INotebookCategory curCategory = ArcaneMagicAPI.getNotebookCategories()
				.get(player.getEntityData().getInteger(tagTab));
		mc.getTextureManager().bindTexture(curCategory.getBackground().getKey());
		drawScaledCustomSizeModalRect(104, 17, pageX, pageY, INNER_BG_WIDTH * 2, INNER_BG_HEIGHT * 2, INNER_BG_WIDTH,
				INNER_BG_HEIGHT, curCategory.getBackground().getValue().getX(),
				curCategory.getBackground().getValue().getY());

		mc.getTextureManager().bindTexture(frame);
		for (int tab = 0; tab < ArcaneMagicAPI.getCategoryCount() * 2; tab++)
		{
			int thisTab = tab >= ArcaneMagicAPI.getCategoryCount() ? tab - ArcaneMagicAPI.getCategoryCount() : tab;
			if (thisTab == player.getEntityData().getInteger(tagTab))
			{
				if (tab < ArcaneMagicAPI.getCategoryCount())
				{
					drawTexturedModalRect(-24, tab * 23, 152, 232, 24, 24);
				} else
				{
					drawIcon(ArcaneMagicAPI.getNotebookCategories().get(thisTab).getIcon(), -19,
					 thisTab * 23 + 4);

					for (INotebookEntry entry : ArcaneMagicAPI.getNotebookCategories().get(thisTab).getEntries())
					{
						drawResearchEntry(entry);
					}
				}
			} else
			{
				if (tab < ArcaneMagicAPI.getCategoryCount())
				{
					drawTexturedModalRect(-16, tab * 23, 184, 232, 16, 22);
				} else
				{
					drawIcon(ArcaneMagicAPI.getNotebookCategories().get(thisTab).getIcon(), -12, thisTab * 23 + 3);
				}
			}
		}

		mc.getTextureManager().bindTexture(frame);
		GlStateManager.enableBlend();
		drawScaledCustomSizeModalRect(0, 0, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT, FRAME_WIDTH,
				FRAME_TEX_HEIGHT);
		
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();

		for (INotebookEntry entry : ArcaneMagicAPI.getNotebookCategories()
				.get(player.getEntityData().getInteger(tagTab)).getEntries())
		{
			// drawResearchInfoOnMouse(entry.getPos().getX(), entry.getPos().getY(),
			// Lists.newArrayList(
			// I18n.format(entry.getUnlocalizedName()),
			// I18n.format(entry.getUnlocalizedName() + ".desc")));
		}

		/* if (relMouseX >= screenX - 24 && relMouseY >= screenY && relMouseX <= screenX
		 * && relMouseY <= screenY + (ArcaneMagicAPI.getCategoryCount() * 23)) { for
		 * (int tab = 0; tab < ArcaneMagicAPI.getCategoryCount(); tab++) { int tabWidth
		 * = 16;
		 * 
		 * if (tab == player.getEntityData().getInteger(tagTab)) { tabWidth = 24; } if
		 * (relMouseY >= screenY + (tab * 23) && relMouseY <= screenY + (tab * 23) + 23
		 * && relMouseX >= screenX - tabWidth) { this.fontRenderer.drawString(
		 * I18n.format(ArcaneMagicAPI.getNotebookCategories().get(tab).
		 * getUnlocalizedName()), mouseX + 1, mouseY - 7, 0xFFFFFF); break; } } } */

		GlStateManager.popMatrix();
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	public void drawResearchInfoOnMouse(int iconX, int iconY, List<String> text)
	{
		ScaledResolution res = new ScaledResolution(mc);

		int pageX = (int) player.getEntityData().getFloat(tagPageX);
		int pageY = (int) player.getEntityData().getFloat(tagPageY);

		float screenX = (res.getScaledWidth() / 2) - (255 / 2);
		float screenY = (res.getScaledHeight() / 2) - (229 / 2);

		List<Integer> colors = Lists.newArrayList(0xFFFFFF, 0x9090ff, 0xaa00aa);

		if (text.size() > 2)
		{
			colors.set(0, 0xffff80);
		}

		iconX = iconX - (int) (pageX * 2);
		iconY = iconY - (int) (pageY * 2);

		if (relMouseX >= iconX + screenX && relMouseY >= iconY + screenY && relMouseX <= iconX + screenX + 22
				&& relMouseY <= iconY + screenY + 22)
		{
			if (relMouseX >= screenX + 16 && relMouseY >= screenY + 17 && relMouseX <= screenX + 239
					&& relMouseY <= screenY + 212)
			{
				drawHoveringText(ItemStack.EMPTY, text, colors, relMouseX - 6, relMouseY + 7, mc.displayWidth,
						mc.displayHeight, 200, fontRenderer);
			}

		}
	}

	private void drawHoveringText(@Nonnull final ItemStack stack, List<String> textLines, List<Integer> textColors,
			int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font)
	{
		if (!textLines.isEmpty())
		{
			RenderTooltipEvent.Pre event = new RenderTooltipEvent.Pre(stack, textLines, mouseX, mouseY, screenWidth,
					screenHeight, maxTextWidth, font);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return;
			}

			boolean flag = textLines.size() > 2;

			mouseX = event.getX();
			mouseY = event.getY();
			screenWidth = event.getScreenWidth();
			screenHeight = event.getScreenHeight();
			maxTextWidth = event.getMaxWidth();
			font = event.getFontRenderer();

			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int tooltipTextWidth = 0;

			for (String textLine : textLines)
			{
				int sizeMultiplier = 1;
				if (textLines.indexOf(textLine) != 0)
				{
					sizeMultiplier = 2;
				}
				int textLineWidth = font.getStringWidth(textLine) / sizeMultiplier;

				if (textLineWidth > tooltipTextWidth)
				{
					tooltipTextWidth = textLineWidth;
				}
			}

			boolean needsWrap = false;

			int titleLinesCount = 1;
			int tooltipX = mouseX + 12;
			if (tooltipX + tooltipTextWidth + 4 > screenWidth)
			{
				tooltipX = mouseX - 16 - tooltipTextWidth;
				if (tooltipX < 4) // if the tooltip doesn't fit on the screen
				{
					if (mouseX > screenWidth / 2)
					{
						tooltipTextWidth = mouseX - 12 - 8;
					} else
					{
						tooltipTextWidth = screenWidth - 16 - mouseX;
					}
					needsWrap = true;
				}
			}

			if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth)
			{
				tooltipTextWidth = maxTextWidth;
				needsWrap = true;
			}

			if (needsWrap)
			{
				int wrappedTooltipWidth = 0;
				List<String> wrappedTextLines = new ArrayList<String>();
				List<Integer> wrappedTextColors = new ArrayList<Integer>();
				for (int i = 0; i < textLines.size(); i++)
				{
					String textLine = textLines.get(i);
					List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
					if (i == 0)
					{
						titleLinesCount = wrappedLine.size();
					}

					for (String line : wrappedLine)
					{
						int widthMultiplier = 1;
						if (i != 0)
						{
							widthMultiplier = 2;
						}
						int lineWidth = font.getStringWidth(line) / widthMultiplier;
						if (lineWidth > wrappedTooltipWidth)
						{
							wrappedTooltipWidth = lineWidth;
						}
						wrappedTextLines.add(line);
						wrappedTextColors.add(textColors.get(i));
					}
				}
				tooltipTextWidth = wrappedTooltipWidth;
				textLines = wrappedTextLines;
				textColors = wrappedTextColors;
				if (mouseX > screenWidth / 2)
				{
					tooltipX = mouseX - 16 - tooltipTextWidth;
				} else
				{
					tooltipX = mouseX + 12;
				}
			}

			int tooltipY = mouseY - 12;
			int tooltipHeight = 8;

			if (textLines.size() > 1)
			{
				tooltipHeight += (textLines.size() - 1) * 10;
				if (textLines.size() > titleLinesCount)
				{
					tooltipHeight += 0.5; // gap between title lines and next lines
				}
			}

			if (tooltipY + tooltipHeight + 6 > screenHeight)
			{
				tooltipY = screenHeight - tooltipHeight - 6;
			}

			if (flag)
			{
				tooltipHeight -= 3;
			} else
			{
				tooltipHeight -= 1;
			}

			final int zLevel = 300;
			final int backgroundColor = 0xF0100010;
			GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3,
					tooltipY + tooltipHeight + 1, backgroundColor, backgroundColor);
			final int borderColorStart = 0x505000FF;
			final int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
			GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2,
					tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 1, borderColorEnd, borderColorEnd);

			MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(stack, textLines, tooltipX, tooltipY,
					font, tooltipTextWidth, tooltipHeight));
			int tooltipTop = tooltipY;

			for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
			{
				String line = textLines.get(lineNumber);
				int color = textColors.get(lineNumber);
				int sizeMultiplier = 1;
				if (lineNumber != 0)
				{
					sizeMultiplier = 2;
					GlStateManager.scale(0.5, 0.5, 0.5);
				}
				font.drawStringWithShadow(line, (float) tooltipX * sizeMultiplier, (float) tooltipY * sizeMultiplier,
						color);
				GlStateManager.scale(sizeMultiplier, sizeMultiplier, sizeMultiplier);
				if (lineNumber + 1 == titleLinesCount)
				{
					tooltipY += 1;
				}
				if (lineNumber == 1)
				{
					tooltipY += 8;
				} else
				{
					tooltipY += 10;
				}
			}

			MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(stack, textLines, tooltipX, tooltipTop, font,
					tooltipTextWidth, tooltipHeight));

			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}

	private void drawIcon(ResourceLocation icon, float x, float y)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		mc.getTextureManager().bindTexture(icon);
		GlStateManager.scale(0.5, 0.5, 1);
		drawModalRectWithCustomSizedTexture((int) (x * 2), (int) (y * 2), 0, 0, 32, 32, 32, 32);
		GlStateManager.scale(2, 2, 1);
		GlStateManager.popMatrix();
	}

	public void drawResearchEntry(INotebookEntry entry)
	{
		int pageX = player.getEntityData().getInteger(tagPageX);
		int pageY = player.getEntityData().getInteger(tagPageY);

		int x = (entry.getPos().getX() * 4) - (int) (pageX / 0.5f);
		int y = (entry.getPos().getY() * 4) - (int) (pageY / 0.5f);
		// if (relMouseX >= screenX + 59 && relMouseY >= screenY + 16 && relMouseX <=
		// screenX + 282 && relMouseY <= screenY + 210)
		//if (x > 318 && y > -20 && x < 1310 && y < 845)
		//{

			float xStart = x;
			float yStart = y;

			float u1 = 0;
			float u2 = 0;
			float v1 = 0;
			float v2 = 0;

			int width = 32;
			int height = 32;

			if (x > 1260)
			{
				//width -= 8;
			}

			if (y > 800)
			{
				//height -= 8;
			}

			// TODO: make research icons exit properly to the top and left of the screen

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			String heading = I18n.format(entry.getUnlocalizedName());
			String desc = I18n.format(entry.getUnlocalizedName() + ".desc");

			int boxWidth = fontRenderer.getStringWidth(heading) + 30;
			int boxHeight = 22;

			final int backgroundColor = 0xF0100010;

			GuiUtils.drawGradientRect((int) zLevel, (int) xStart / 4 - 2, (int) yStart / 4 - 3,
					(int) xStart / 4 + boxWidth + 3, (int) yStart / 4 + boxHeight + 1, backgroundColor,
					backgroundColor);
			final int borderColorStart = 0x505000FF;
			final int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;

			GuiUtils.drawGradientRect((int) zLevel, (int) xStart - 2, (int) yStart + boxHeight + 2,
					(int) xStart + boxHeight + 3, (int) yStart + boxHeight + 1, borderColorEnd, borderColorEnd);

			mc.getTextureManager().bindTexture(frame);
			// drawScaledCustomSizeModalRect((int) (xStart / 4), (int) (yStart / 4), 90,
			// 208, width - 10, height - 10,
			// width - 10, height - 10, FRAME_WIDTH, FRAME_HEIGHT);
			Object icon = entry.getIcon().getTexture();
			if (icon instanceof ResourceLocation)
			{
				mc.getTextureManager().bindTexture((ResourceLocation) icon);
				GlStateManager.scale(0.5, 0.5, 1);
				// this.drawScaledCustomSizeModalRect((int)(xStart / 2) + 2, (int)(yStart / 2) +
				// 2, (int)(u2 / 2), (int)(v2 / 2), (int)u2,(int)v2, 32, 32, 32, 32);
				drawModalRectWithCustomSizedTexture((int) (xStart / 2) + 5, (int) (yStart / 2) + 4, u2, v2, width,
						height, 32, 32);
				GlStateManager.scale(2, 2, 1);
			} else if (icon instanceof ItemStack)
			{
				this.itemRender.renderItemAndEffectIntoGUI((ItemStack) icon, (int) ((xStart + 3) / 1),
						(int) ((yStart + 3) / 1));
			}

			GlStateManager.pushMatrix();

			fontRenderer.drawStringWithShadow(heading, (float) xStart / 4 + 25, (float) yStart / 4 + 2, 0xb0b7b5);
			GlStateManager.scale(0.5, 0.5, 0.5);
			fontRenderer.drawStringWithShadow(desc, (float) xStart / 2 + 50, (float) yStart / 2 + 25, 0xa3cec3);
			GlStateManager.color(1, 1, 1);
			GlStateManager.popMatrix();

			GlStateManager.popMatrix();
		//}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

		GlStateManager.pushMatrix();
		if (clickedMouseButton == 0)
		{
			ScaledResolution res = new ScaledResolution(mc);

			float screenX = (res.getScaledWidth() / 2) - (255 / 2);
			float screenY = (res.getScaledHeight() / 2) - (229 / 2);

			if (relMouseX >= screenX + 59 && relMouseY >= screenY + 16 && relMouseX <= screenX + 282
					&& relMouseY <= screenY + 210)
			{
				float thisDragDistX = (lastDragX - (mouseX - (res.getScaledWidth() / 2) - (255 / 2))) * 1f;
				float thisDragDistY = (lastDragY - (mouseY - (res.getScaledHeight() / 2) - (229 / 2))) * 1f;

				float totalDragDistX = player.getEntityData().getFloat(tagPageX) + thisDragDistX;
				float totalDragDistY = player.getEntityData().getFloat(tagPageY) + thisDragDistY;
				Pos2 curCategoryBackDimensions = ArcaneMagicAPI.getNotebookCategories()
						.get(player.getEntityData().getInteger(tagTab)).getBackground().getValue();
				if (totalDragDistX >= 0 && totalDragDistX <= curCategoryBackDimensions.getX() - (INNER_BG_WIDTH * 2))
				{
					player.getEntityData().setFloat(tagPageX, totalDragDistX);
				}

				if (totalDragDistY >= 0 && totalDragDistY <= curCategoryBackDimensions.getY() - (INNER_BG_HEIGHT * 2))
				{
					player.getEntityData().setFloat(tagPageY, totalDragDistY);
				}
			}

			lastDragX = mouseX - (res.getScaledWidth() / 2) - (255 / 2);
			lastDragY = mouseY - (res.getScaledHeight() / 2) - (229 / 2);
		}
		GlStateManager.popMatrix();
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0)
		{
			ScaledResolution res = new ScaledResolution(mc);

			lastDragX = mouseX - (res.getScaledWidth() / 2) - (255 / 2);
			lastDragY = mouseY - (res.getScaledHeight() / 2) - (229 / 2);

			float screenX = (res.getScaledWidth() / 2) - (255 / 2);
			float screenY = (res.getScaledHeight() / 2) - (229 / 2);

			if (relMouseX >= screenX - 16 && relMouseY >= screenY && relMouseX <= screenX
					&& relMouseY <= screenY + (ArcaneMagicAPI.getCategoryCount() * 23))
			{
				for (int tab = 0; tab < ArcaneMagicAPI.getCategoryCount(); tab++)
				{
					if (relMouseY >= screenY + (tab * 23) && relMouseY <= screenY + (tab * 23) + 23)
					{
						if (player.getEntityData().getInteger(tagTab) != tab)
						{
							player.getEntityWorld().playSound(player.posX, player.posY, player.posZ,
									ArcaneMagicSoundHandler.randomCameraClackSound(), SoundCategory.MASTER, 1f, 1f,
									false);
							player.getEntityData().setInteger(tagTab, tab);
							break;
						}
					}
				}
			}
		}
	}
}
