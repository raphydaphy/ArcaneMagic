package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ModRegistry;
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

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class NotebookScreen extends Screen
{
	// x and y size of the parchment texture
	private static final int NOTEBOOK_WIDTH = 272;
	private static final int NOTEBOOK_HEIGHT = 180;
	private static final int TEX_HEIGHT = 256;

	private Identifier BACKGROUND = new Identifier(ArcaneMagic.DOMAIN, "textures/gui/notebook.png");

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

		int xTop = (client.window.getScaledWidth() / 2) - (NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (NOTEBOOK_HEIGHT / 2);

		int left = xTop + 17;
		int right = xTop + 142;
		int rightCenter = right + 57;

		client.getTextureManager().bindTexture(BACKGROUND);
		DrawableHelper.drawTexturedRect(xTop, yTop, 0, 0, NOTEBOOK_WIDTH, NOTEBOOK_HEIGHT, NOTEBOOK_WIDTH, NOTEBOOK_HEIGHT, NOTEBOOK_WIDTH, TEX_HEIGHT);

		// Intro page
		int pointer = yTop + 11;
		pointer += drawTexturedRect(xTop - 8, pointer, 0, 206, 140, 31, 140, 30, NOTEBOOK_WIDTH, TEX_HEIGHT) - 3;
		drawSplitString(left - 8, pointer -21, 1, 119, 0x218e15, false, false, "notebook.arcanemagic.title", client.player.getEntityName());
		drawSplitString(left, pointer, 0.7, 116, 0, false, false, "notebook.arcanemagic.intro");

		// Categories page
		pointer = yTop + 15;
		pointer += drawSplitString(rightCenter, pointer, 1, 116, 0, false, true, "notebook.arcanemagic.categories") + 3;
		pointer += drawTexturedRect(right + 14, pointer, 46, 180, 85, 3, 85, 3, NOTEBOOK_WIDTH, TEX_HEIGHT) + 3;
		pointer += drawItemBoxWithText(right + 5, pointer + 2, mouseX, mouseY,ModRegistry.GOLDEN_SCEPTER,"Draining", "Using a Scepter to extract Soul") + 5;
		pointer += drawItemBoxWithText(right + 5, pointer + 2, mouseX, mouseY,ModRegistry.TRANSFIGURATION_TABLE,"Transfiguration", "Using Soul to craft various items") + 5;
		pointer += drawItemBoxWithText(right + 5, pointer + 2, mouseX, mouseY,ModRegistry.SOUL_PENDANT,"Soul Storage", "Binding Soul to items for storage") + 5;
		pointer += drawItemBoxWithText(right + 5, pointer + 2, mouseX, mouseY,ModRegistry.ALTAR,"Soul Collection", "Gathering Soul more efficiently") + 5;

		boolean overRightArrow = mouseX >= right + 85 && mouseY >= yTop + NOTEBOOK_HEIGHT - 21 && mouseX <= right + 103 && mouseY <= yTop + NOTEBOOK_HEIGHT - 11;
		drawTexturedRect(right + 85, yTop + NOTEBOOK_HEIGHT - 21, overRightArrow ? 23 : 0, 180, 18, 10, 18, 10, NOTEBOOK_WIDTH, TEX_HEIGHT);

		GlStateManager.popMatrix();
	}

	private int drawItemBoxWithText(int x, int y, int mouseX, int mouseY, ItemProvider item, String unlocalizedTitle, String unlocalizedDesc)
	{
		int largest = 0;
		int tmp = RenderUtils.drawItemInBox(this, new ItemStack(item), null, x, y, mouseX, mouseY);
		largest = tmp > largest ? tmp : largest;
		tmp = drawSplitString(x + 29, y + 2, 0.9, 80, 0, false, false, unlocalizedTitle) + 3;
		tmp += drawSplitString(x + 29, y + 2 + tmp, 0.7, 80, 0, false, false, unlocalizedDesc);
		return tmp > largest ? tmp : largest;
	}

	private int drawTexturedRect(int x, int y, int u, int v, int maxU, int maxV, int width, int height, int textureWidth, int textureHeight)
	{
		client.getTextureManager().bindTexture(BACKGROUND);
		DrawableHelper.drawTexturedRect(x, y, u, v, maxU, maxV, width, height, textureWidth, textureHeight);
		return height;
	}

	private int drawSplitString(int x, int y, double scale, int width, int color, boolean verticallyCentered, boolean horizontallyCentered, String unlocalizedText, Object... keys)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scaled(scale, scale, scale);
		int height = RenderUtils.drawSplitString(MinecraftClient.getInstance().textRenderer, I18n.translate(unlocalizedText, keys),
				(int) (x / scale), (int) (y / scale), (int) (width / scale), color, verticallyCentered, horizontallyCentered);
		GlStateManager.popMatrix();
		return (int) (height * scale);
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
