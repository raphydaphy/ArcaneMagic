package com.raphydaphy.arcanemagic.client.gui;

import java.io.IOException;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookChanged;
import com.raphydaphy.arcanemagic.common.util.GuiTextFieldNoShadow;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNotebook extends GuiScreen
{
	// width and height of the texture, and of just the book area
	public static final int NOTEBOOK_WIDTH = 232;
	public static final int NOTEBOOK_HEIGHT = 180;
	public static final int NOTEBOOK_TEX_HEIGHT = 200;

	// Scale up the notebook so it isn't so tiny
	final double scale = 1.5;

	// Because for some reason the mouse information provided in onMouseClick is wrong
	public int relMouseX = 0;
	public int relMouseY = 0;

	// The player, to store entity data
	private EntityPlayer player;

	public static final ResourceLocation notebook = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/notebook.png");

	private GuiTextFieldNoShadow searchField;

	public GuiNotebook(EntityPlayer player)
	{
		this.player = player;
		NotebookInfo cap = player.getCapability(NotebookInfo.CAP, null);

		// player opened it for the first time!
		if (cap != null && !cap.getUsed())
		{
			cap.setUsed(true);
			ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketNotebookChanged(cap));
		}
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.setGuiSize(mc.displayWidth, mc.displayHeight);

		NotebookInfo cap = player.getCapability(NotebookInfo.CAP, null);

		if (cap != null)
		{
			// MC Screen resolution based on GUI scale
			ScaledResolution res = new ScaledResolution(mc);

			// Size of the notebook when taking the larger scale into account
			final int SCALED_NOTEBOOK_WIDTH = (int) (NOTEBOOK_WIDTH * scale);
			final int SCALED_NOTEBOOK_HEIGHT = (int) (NOTEBOOK_HEIGHT * scale);

			// The start x and y coords of the notebook on the screen
			int screenX = (res.getScaledWidth() / 2) - (SCALED_NOTEBOOK_WIDTH / 2);
			int screenY = (res.getScaledHeight() / 2) - (SCALED_NOTEBOOK_HEIGHT / 2);
			this.searchField = new GuiTextFieldNoShadow(1, mc.fontRenderer, screenX + 27, screenY + 24, 88, 30);
			this.searchField.setVisible(true);
			this.searchField.setCanLoseFocus(false);
			this.searchField.setFocused(true);
			this.searchField.setMaxStringLength(50);
			this.searchField.setEnableBackgroundDrawing(false);
			this.searchField.setText(cap.getSearchKey());
			this.searchField.setTextColor(0x000000);
		}
	}

	private void drawArrow(boolean isLeft, int x, int y, int mouseX, int mouseY, int screenX, int screenY)
	{
		int offset = isLeft ? 2 : 22;

		if (mouseX >= screenX + x && mouseY >= screenY + y && mouseX <= screenX + x + (18 * scale)
				&& mouseY <= screenY + y + (10 * scale))
		{
			offset += 40;
		}
		drawScaledCustomSizeModalRect(screenX + x, screenY + y, offset, 182, 18, 10, (int) (18 * scale),
				(int) (10 * scale), NOTEBOOK_WIDTH, NOTEBOOK_TEX_HEIGHT);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);

		NotebookInfo cap = player.getCapability(NotebookInfo.CAP, null);

		if (cap != null)
		{
			if (this.searchField.textboxKeyTyped(typedChar, keyCode))
			{
				cap.setSearchKey(this.searchField.getText());
				ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketNotebookChanged(cap));
			} else
			{
				super.keyTyped(typedChar, keyCode);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		NotebookInfo cap = player.getCapability(NotebookInfo.CAP, null);

		if (cap != null)
		{
			// Save the real mouse coordinates for access in onMouseClick
			relMouseX = mouseX;
			relMouseY = mouseY;

			// Because having shading is important
			this.drawDefaultBackground();
			super.drawScreen(mouseX, mouseY, partialTicks);

			// Don't break the rest of the world
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();

			// Because some of the textures are only semi-opaque
			GlStateManager.enableBlend();

			// MC Screen resolution based on GUI scale
			ScaledResolution res = new ScaledResolution(mc);

			// Size of the notebook when taking the larger scale into account
			final int SCALED_NOTEBOOK_WIDTH = (int) (NOTEBOOK_WIDTH * scale);
			final int SCALED_NOTEBOOK_HEIGHT = (int) (NOTEBOOK_HEIGHT * scale);

			// The start x and y coords of the notebook on the screen
			int screenX = (res.getScaledWidth() / 2) - (SCALED_NOTEBOOK_WIDTH / 2);
			int screenY = (res.getScaledHeight() / 2) - (SCALED_NOTEBOOK_HEIGHT / 2);

			// Main notebook texture
			mc.getTextureManager().bindTexture(notebook);
			drawScaledCustomSizeModalRect(screenX, screenY, 0, 0, NOTEBOOK_WIDTH, NOTEBOOK_HEIGHT,
					SCALED_NOTEBOOK_WIDTH, SCALED_NOTEBOOK_HEIGHT, NOTEBOOK_WIDTH, NOTEBOOK_TEX_HEIGHT);

			// Selected Category indicator
			int curCategory = cap.getCategory();
			int curPage = cap.getPage();
			int renderCurCategory = 0;

			if (cap.matchesSearchKey(ArcaneMagicAPI.getNotebookCategories().get(curCategory)))
			{
				for (int category = 0; category < ArcaneMagicAPI.getCategoryCount(); category++)
				{
					if (cap.matchesSearchKey(ArcaneMagicAPI.getNotebookCategories().get(category)))
					{
						if (category < curCategory)
						{
							renderCurCategory++;
						} else
						{
							break;
						}
					}
				}
			} else
			{
				renderCurCategory = -1;
			}
			// if they haven't unlocked the current category, or it dosen't exist
			if (curCategory > ArcaneMagicAPI.getNotebookCategories().size()
					|| !cap.isVisible(ArcaneMagicAPI.getNotebookCategories().get(curCategory)))
			{
				cap.setCategory(1);
				curCategory = 1;
				ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketNotebookChanged(cap));
			}

			// if they haven't unlocked the current page, or it dosen't exist
			if (curPage >= ArcaneMagicAPI.getNotebookCategories().get(curCategory).getPages(cap).size())
			{
				cap.setPage(0);
				curPage = 0;
				ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketNotebookChanged(cap));
			}

			// If there are more than 1 pages in this category, draw arrows to navitage!
			if (ArcaneMagicAPI.getNotebookCategories().get(curCategory).getPages(cap).size() > 1)
			{
				// if they are past the first page, draw an arrow to go back
				if (curPage > 0)
				{
					drawArrow(true, 144, 238, mouseX, mouseY, screenX, screenY);
				}

				// If they are before the last page, draw an arrow to go forwards
				if (curPage < ArcaneMagicAPI.getNotebookCategories().get(curCategory).getPages(cap).size() - 1)
				{
					drawArrow(false, 285, 238, mouseX, mouseY, screenX, screenY);
				}
			}
			mc.getTextureManager().bindTexture(notebook);

			// selected category background bar
			if (renderCurCategory >= 0)
			{
				drawScaledCustomSizeModalRect((int) ((screenX + 13) + (1 * scale)),
						(int) ((screenY + 40 + (renderCurCategory * 20)) + (1 * scale)), 86, 182, 70, 16,
						(int) (70 * scale), (int) (16 * scale), NOTEBOOK_WIDTH, NOTEBOOK_TEX_HEIGHT);
			}

			// Search bar background
			// TODO: proper texture
			drawScaledCustomSizeModalRect((int) ((screenX + 13) + (1 * scale)), (int) ((screenY + 15) + (1 * scale)),
					86, 182, 70, 16, (int) (70 * scale), (int) (16 * scale), NOTEBOOK_WIDTH, NOTEBOOK_TEX_HEIGHT);
			this.searchField.drawTextBox();

			boolean shouldDrawTitle = ArcaneMagicAPI.getNotebookCategories().get(curCategory).getUnlocalizedTitle(cap,
					curPage) != null;

			ArcaneMagicAPI.getNotebookCategories().get(curCategory).getPages(cap).get(curPage).draw(screenX + 145,
					screenY + (shouldDrawTitle ? 40 : 18), mouseX, mouseY, this);

			// Custom matrix for drawing scaled strings
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();

			// Current Category Name
			double largeText = 1.4;
			GlStateManager.scale(largeText, largeText, largeText);
			if (shouldDrawTitle)
			{
				ArcaneMagicAPI.getNotebookCategories().get(curCategory).getFontRenderer(this).drawString(
						I18n.format(ArcaneMagicAPI.getNotebookCategories().get(curCategory).getUnlocalizedTitle(cap,
								curPage)),
						(int) ((screenX + 145) / largeText), (int) ((screenY + 20) / largeText), 0x000000); // satan is coming
			}
			// Category List
			double categoryNameSize = 0.8;
			GlStateManager.scale((1 / largeText) * categoryNameSize, (1 / largeText) * categoryNameSize,
					(1 / largeText) * categoryNameSize);

			int cat = 0;
			for (NotebookCategory category : ArcaneMagicAPI.getNotebookCategories())
			{
				// if the category is visible in the book
				if (cap.matchesSearchKey(category))
				{
					// Draw the category!
					ArcaneMagicAPI.getNotebookCategories().get(curCategory).getFontRenderer(this).drawString(
							I18n.format(category.getUnlocalizedName()),
							(int) ((screenX + (cat == renderCurCategory ? 26 : 18)) * (1 / categoryNameSize)),
							(int) ((screenY + 50 + (cat * 20)) * (1 / categoryNameSize)),
							cat == renderCurCategory ? 0x515151 : 0x32363d);

					cat++;
				}
			}

			// Go back to default scaling
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();

			ArcaneMagicAPI.getNotebookCategories().get(curCategory).getPages(cap).get(curPage).drawPost(screenX + 145,
					screenY + (shouldDrawTitle ? 40 : 18), mouseX, mouseY, this);

			// Goodbye matrix!
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0)
		{
			NotebookInfo cap = player.getCapability(NotebookInfo.CAP, null);

			if (cap != null)
			{
				// Things from drawScreen to calculate positions based on GUI Scale
				ScaledResolution res = new ScaledResolution(mc);

				final int SCALED_NOTEBOOK_WIDTH = (int) (NOTEBOOK_WIDTH * scale);
				final int SCALED_NOTEBOOK_HEIGHT = (int) (NOTEBOOK_HEIGHT * scale);

				int screenX = (res.getScaledWidth() / 2) - (SCALED_NOTEBOOK_WIDTH / 2);
				int screenY = (res.getScaledHeight() / 2) - (SCALED_NOTEBOOK_HEIGHT / 2);

				// if there are arrows to click on
				if (ArcaneMagicAPI.getNotebookCategories().get(cap.getCategory()).getPages(cap).size() > 1)
				{
					// if there if a backwards arrow
					if (cap.getPage() > 0)
					{
						// .. and if the mouse is over it
						if (relMouseX >= screenX + 144 && relMouseY >= screenY + 238
								&& relMouseX <= screenX + 144 + (18 * scale)
								&& relMouseY <= screenY + 238 + (10 * scale))
						{
							player.getEntityWorld().playSound(player.posX, player.posY, player.posZ,
									ArcaneMagicSoundHandler.randomPageSound(), SoundCategory.MASTER, 1f, 1f, false);
							cap.setPage(cap.getPage() - 1);
							ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketNotebookChanged(cap));
						}
					}

					// if there if a forwards arrow
					if (cap.getPage() < ArcaneMagicAPI.getNotebookCategories().get(cap.getCategory()).getPages(cap)
							.size() - 1)
					{
						// .. and if the mouse is over it
						if (relMouseX >= screenX + 285 && relMouseY >= screenY + 238
								&& relMouseX <= screenX + 285 + (18 * scale)
								&& relMouseY <= screenY + 238 + (10 * scale))
						{
							player.getEntityWorld().playSound(player.posX, player.posY, player.posZ,
									ArcaneMagicSoundHandler.randomPageSound(), SoundCategory.MASTER, 1f, 1f, false);
							cap.setPage(cap.getPage() + 1);
							ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketNotebookChanged(cap));
						}
					}
				}

				if (relMouseX >= screenX + 10 && relMouseX <= screenX + 118)
				{
					int tab = 0;

					for (int unRealTab = 0; unRealTab < ArcaneMagicAPI.getNotebookCategories().size(); unRealTab++)
					{
						// if they have unlocked this category
						if (cap.matchesSearchKey(ArcaneMagicAPI.getNotebookCategories().get(unRealTab)))
						{
							if (relMouseY >= screenY + (tab * 23) && relMouseY <= screenY + (tab * 20) + 60)
							{
								if (cap.getCategory() != unRealTab)
								{
									player.getEntityWorld().playSound(player.posX, player.posY, player.posZ,
											ArcaneMagicSoundHandler.clack, SoundCategory.MASTER, 1f, 1f, false);
									cap.setCategory(unRealTab);
									cap.setPage(0);
									ArcaneMagicPacketHandler.INSTANCE.sendToServer(new PacketNotebookChanged(cap));
								}
								break;
							}

							tab++;
						}

					}
				}
			}
		}
	}
}
