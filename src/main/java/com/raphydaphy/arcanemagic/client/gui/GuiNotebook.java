package com.raphydaphy.arcanemagic.client.gui;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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

	public static final String tagUsedNotebook = "usedNotebook";

	// Current category selected
	public static final String tagCategory = "notebookCategory";

	// Page within the selected category
	public static final String tagPage = "notebookPage";

	// Page on the list of pages
	public static final String tagIndexPage = "notebookIndexPage";

	private EntityPlayer player;

	private static final ResourceLocation notebook = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/notebook.png");

	public GuiNotebook(EntityPlayer player)
	{
		this.player = player;

		if (player.getEntityData().getBoolean(tagUsedNotebook) == false)
		{
			System.out.println("Player opened Notebook for first time, doing initial setup.");
			// player.getEntityData().setBoolean(tagUsedNotebook, true);
			player.getEntityData().setInteger(tagCategory, 0);
			player.getEntityData().setInteger(tagPage, 0);
			player.getEntityData().setInteger(tagIndexPage, 0);
		}
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.setGuiSize(mc.displayWidth, mc.displayHeight);
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

	private void drawCraftRecipe(ItemStack[][] recipeIn, ItemStack recipeOut, int x, int y, int mouseX, int mouseY,
			int screenX, int screenY)
	{
		// Make an embedded matrix for rendering crafting recipes
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();

		mc.getTextureManager().bindTexture(notebook);
		drawScaledCustomSizeModalRect(screenX + x + 20, screenY + y - 3, 18, 18, 1, 1, 2, 73, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);
		drawScaledCustomSizeModalRect(screenX + x - 3, screenY + y + 20, 18, 18, 1, 1, 73, 2, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);

		drawScaledCustomSizeModalRect(screenX + x + 45, screenY + y - 3, 18, 18, 1, 1, 2, 73, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);
		drawScaledCustomSizeModalRect(screenX + x - 3, screenY + y + 45, 18, 18, 1, 1, 73, 2, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);

		drawScaledCustomSizeModalRect(screenX + x + 108, screenY + y + 20, 18, 18, 1, 1, 26, 2, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);
		drawScaledCustomSizeModalRect(screenX + x + 108, screenY + y + 44, 18, 18, 1, 1, 26, 2, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);

		drawScaledCustomSizeModalRect(screenX + x + 108, screenY + y + 20, 18, 18, 1, 1, 2, 25, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);
		drawScaledCustomSizeModalRect(screenX + x + 132, screenY + y + 20, 18, 18, 1, 1, 2, 25, NOTEBOOK_WIDTH,
				NOTEBOOK_TEX_HEIGHT);

		// This involves rendering items, so we need to ensure they don't turn black
		RenderHelper.enableGUIStandardItemLighting();

		// Render the crafting recipe
		for (int inputX = 0; inputX < 3; inputX++)
		{
			for (int inputY = 0; inputY < 3; inputY++)
			{
				if (recipeIn[inputY][inputX] != ItemStack.EMPTY)
				{
					mc.getRenderItem().renderItemAndEffectIntoGUI(recipeIn[inputY][inputX], screenX + x + (inputX * 25),
							screenY + y + (inputY * 25));
				}
			}
		}

		// Draw the arrow to show the output of the recipe
		mc.getTextureManager().bindTexture(notebook);
		drawScaledCustomSizeModalRect(screenX + x + 80, screenY + y + 26, 158, 182, 22, 15, (int) (22), (int) (15),
				NOTEBOOK_WIDTH, NOTEBOOK_TEX_HEIGHT);

		// Draw the output item in the recipe
		mc.getRenderItem().renderItemAndEffectIntoGUI(recipeOut, screenX + x + 113, screenY + y + 25);

		// Go back to the main render matrix
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
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
		int screenX = (int) ((res.getScaledWidth() / 2) - (SCALED_NOTEBOOK_WIDTH / 2));
		int screenY = (int) ((res.getScaledHeight() / 2) - (SCALED_NOTEBOOK_HEIGHT / 2));

		// Main notebook texture
		mc.getTextureManager().bindTexture(notebook);
		drawScaledCustomSizeModalRect(screenX, screenY, 0, 0, NOTEBOOK_WIDTH, NOTEBOOK_HEIGHT, SCALED_NOTEBOOK_WIDTH,
				SCALED_NOTEBOOK_HEIGHT, NOTEBOOK_WIDTH, NOTEBOOK_TEX_HEIGHT);

		// Some arrows! These ones let you choose your category page
		drawArrow(true, 13, 240, mouseX, mouseY, screenX, screenY);
		drawArrow(false, 89, 240, mouseX, mouseY, screenX, screenY);

		// Selected Category indicator
		int curCategory = 5;
		List<String> categories = Lists.newArrayList("Ancient Relics", "Basic Linguistics", "Elemental Particles",
				"Mystical Energy", "Natural Harmony", "Ancient Crystals", "Essence Collection", "Crystal Transformation", "Dangers of Magic","Hidden Secrets", "");

		mc.getTextureManager().bindTexture(notebook);
		drawScaledCustomSizeModalRect((int) ((screenX + 13) + (1 * scale)),
				(int) ((screenY + 14 + (curCategory * 20)) + (1 * scale)), 86, 182, 70, 16, (int) (70 * scale),
				(int) (16 * scale), NOTEBOOK_WIDTH, NOTEBOOK_TEX_HEIGHT);

		List<String> categoryText = Lists.newArrayList(
				"Through your journeys thus far, you have discovered light particles imbued with what appears to be some form of magic. Now that you have learned more about the behaviour of this energy, perhaps it might be time to try manipulating it to your advantage.",
				"An Essence Crystallizer appears to do exactly what you want. Perhaps placing one near the particles you found might prove useful?");
		int curY = 0;
		for (String sentence : categoryText)
		{
			fontRenderer.drawSplitString(sentence, screenX + 145, screenY + 40 + curY, 180, 0x000000);
			curY += 75;
		}

		ItemStack[][] itemsIn = { { ItemStack.EMPTY, new ItemStack(Items.BLAZE_ROD), ItemStack.EMPTY },
				{ new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.PRISMARINE_CRYSTALS),
						new ItemStack(Items.BLAZE_ROD) },
				{ new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Blocks.IRON_BLOCK),
						new ItemStack(Blocks.IRON_BLOCK) } };
		drawCraftRecipe(itemsIn, new ItemStack(ModRegistry.CRYSTALLIZER), 170, 173, mouseX, mouseY, screenX, screenY);

		// Current Category Name
		double largeText = 1.4;
		GlStateManager.scale(largeText, largeText, largeText);
		fontRenderer.drawStringWithShadow(categories.get(curCategory), (int) ((screenX + 145) / largeText),
				(int) ((screenY + 17) / largeText), 0x666666); // satan is coming

		// Category List
		double categoryNameSize = 0.8;
		GlStateManager.scale((1 / largeText) * categoryNameSize, (1 / largeText) * categoryNameSize,
				(1 / largeText) * categoryNameSize);

		for (int category = 0; category < categories.size(); category++)
		{
			// Basic category positioning
			int catX = 18;
			boolean doShadow = false;

			// Selected category is offset and bold
			if (category == curCategory)
			{
				catX = 26;
				doShadow = true;
			}

			// Draw the category!
			fontRenderer.drawString(categories.get(category), (int) ((screenX + catX) * (1 / categoryNameSize)),
					(int) ((screenY + 24 + (category * 20)) * (1 / categoryNameSize)), 0x32363d, doShadow);
		}

		// Goodbye matrix!
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0)
		{

		}
	}
}
