package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

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

		client.getTextureManager().bindTexture(BACKGROUND);

		// The start x and y coords of the notebook on the screen
		int xTop = (client.window.getScaledWidth() / 2) - (NOTEBOOK_WIDTH / 2);
		int yTop = (client.window.getScaledHeight() / 2) - (NOTEBOOK_HEIGHT / 2);

		DrawableHelper.drawTexturedRect(xTop, yTop, 0, 0, NOTEBOOK_WIDTH, NOTEBOOK_HEIGHT, NOTEBOOK_WIDTH, NOTEBOOK_HEIGHT, NOTEBOOK_WIDTH, TEX_HEIGHT);

		int pointer = yTop + 15;
		pointer += drawSplitString(xTop + 17, pointer, 1, "notebook.arcanemagic.title", client.player.getEntityName());
		pointer += 5;
		drawSplitString(xTop + 17, pointer, 0.7, "notebook.arcanemagic.intro");


		GlStateManager.popMatrix();
	}

	private int drawSplitString(int x, int y, double scale, String unlocalizedText, Object... keys)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scaled(scale, scale, scale);
		int height = RenderUtils.drawSplitString(MinecraftClient.getInstance().textRenderer, I18n.translate(unlocalizedText, keys),
				(int)(x / scale), (int)(y / scale), (int)(116 / scale),0, false, false);
		GlStateManager.popMatrix();
		return (int)(height * scale);
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
