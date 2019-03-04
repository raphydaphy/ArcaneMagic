package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.parchment.IParchment;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.cooking.SmeltingRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ParchmentScreen extends Screen
{
	// x and y size of the parchment texture
	private static final int DIMENSIONS = 64;
	private static final int TEX_HEIGHT = 69;

	private static final int PROGRESS_BAR_LENGTH = 48;
	private static final int FULL_PROGRESS = PROGRESS_BAR_LENGTH - 2;

	private static final float SCALE = 3;
	private static final int SCALED_DIMENSIONS = (int) (DIMENSIONS * SCALE);

	private Identifier BACKGROUND = new Identifier(ArcaneMagic.DOMAIN, "textures/gui/parchment.png");

	private ItemStack stack;
	private IParchment parchment;

	public ParchmentScreen(ItemStack stack, IParchment parchment)
	{
		this.stack = stack;
		this.parchment = parchment;
		parchment.setParchmentStack(stack);
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
		int screenCenterX = (client.window.getScaledWidth() / 2) - (SCALED_DIMENSIONS / 2);
		int screenCenterY = (client.window.getScaledHeight() / 2) - (SCALED_DIMENSIONS / 2);

		drawBackground(screenCenterX, screenCenterY);

		drawText(parchment.getText(), screenCenterY + (parchment.verticallyCenteredText() ? SCALED_DIMENSIONS / 2f : 4 * SCALE));

		int percent = (int) (parchment.getPercent() * FULL_PROGRESS);

		if (parchment.getRecipe() != null)
		{
			drawRecipe(parchment.getRecipe(), screenCenterX, screenCenterY, mouseX, mouseY);
		}

		if (parchment.showProgressBar()) drawProgressBar(percent, screenCenterX, screenCenterY);

		GlStateManager.popMatrix();
	}

	private void drawBackground(int screenCenterX, int screenCenterY)
	{
		DrawableHelper.drawTexturedRect(screenCenterX, screenCenterY, 0, 0, DIMENSIONS, DIMENSIONS,
				SCALED_DIMENSIONS, SCALED_DIMENSIONS, DIMENSIONS, TEX_HEIGHT);
	}

	private void drawText(String unlocalizedText, float top)
	{
		RenderUtils.drawSplitString(client.textRenderer, I18n.translate(unlocalizedText), client.window.getScaledWidth() / 2f, (int) (top), 160, 0, parchment.verticallyCenteredText());
	}

	private void drawProgressBar(int progress, int screenCenterX, int screenCenterY)
	{
		GlStateManager.pushMatrix();

		client.getTextureManager().bindTexture(BACKGROUND);

		DrawableHelper.drawTexturedRect(
				(int) (screenCenterX + 8 * SCALE), (int) (screenCenterY + 54 * SCALE), 0, DIMENSIONS,
				PROGRESS_BAR_LENGTH, 5, (int) (PROGRESS_BAR_LENGTH * SCALE), (int) ((5) * SCALE), DIMENSIONS, TEX_HEIGHT);

		if (progress > 0)
		{
			System.out.println(progress);
			DrawableHelper.drawTexturedRect(
					(int) (screenCenterX + 9 * SCALE), (int) (screenCenterY + 55 * SCALE), PROGRESS_BAR_LENGTH, DIMENSIONS + 1,
					1, 3, (int) (progress * SCALE), (int) ((3) * SCALE), DIMENSIONS, TEX_HEIGHT);
		}

		GlStateManager.popMatrix();

	}

	private void drawRecipe(Recipe<? extends Inventory> recipe, int screenCenterX, int screenCenterY, int mouseX, int mouseY)
	{
		int x = screenCenterX + 31;
		int y = (int) (screenCenterY + 37 * SCALE);

		DefaultedList<Ingredient> ingredients = recipe.getPreviewInputs();
		ItemStack output = recipe.getOutput();

		ParchmentRecipeType type = getRecipeType(recipe);

		GlStateManager.pushMatrix();

		client.getTextureManager().bindTexture(BACKGROUND);

		if (type == ParchmentRecipeType.LARGE_CRAFTING)
		{
			DrawableHelper.drawRect(x + 20, y - 3, x + 20 + 2, y - 3 + 73, 0xff422c0e);
			DrawableHelper.drawRect(x - 3, y + 20, x - 3 + 73, y + 20 + 2, 0xff422c0e);
			DrawableHelper.drawRect(x + 45, y - 3, x + 45 + 2, y - 3  + 73, 0xff422c0e);
			DrawableHelper.drawRect(x - 3, y + 45, x - 3 + 73, y + 45 + 2, 0xff422c0e);

			GlStateManager.pushMatrix();

			GuiLighting.enableForItems();

			for (int inputX = 0; inputX < 3; inputX++)
			{
				for (int inputY = 0; inputY < 3; inputY++)
				{
					ItemStack[] ingredient = ingredients.get((inputX) + (3 * inputY)).getStackArray();
					if (ingredient.length != 0)
					{
						if (!ingredient[(int) (client.world.getTime() % ingredient.length)].isEmpty())
						{
							// Render the recipe component
							client.getItemRenderer().renderGuiItem(ingredient[0], x + (inputX * 25),
									y + (inputY * 25));
						}
					}
				}
			}

			GlStateManager.popMatrix();
		}

		GuiLighting.disable();

		GlStateManager.popMatrix();

		client.getTextureManager().bindTexture(BACKGROUND);

		// Draw the crafting arrow
		DrawableHelper.drawTexturedRect(x + 78, y + 26, 48, 64, 7, 5, 22, 15, DIMENSIONS,
				TEX_HEIGHT);

		// Crafting Output Box
		DrawableHelper.drawRect(x + 108, y + 21, x + 108 + 26, y + 21 + 2, 0xff422c0e);
		DrawableHelper.drawRect(x + 108, y + 45, x + 108 + 26, y + 45 + 2, 0xff422c0e);
		DrawableHelper.drawRect(x + 108, y + 21, x + 108 + 2, y + 21 + 25, 0xff422c0e);
		DrawableHelper.drawRect(x + 132, y + 21, x + 132 + 2, y + 21 + 25, 0xff422c0e);

		// Draw the output item
		GuiLighting.enableForItems();
		client.getItemRenderer().renderGuiItem(output, x + 113, y + 26);
		GuiLighting.disable();

		// Render the tooltips for the recipe matrix
		for (int inputX = 0; inputX < 3; inputX++)
		{
			for (int inputY = 0; inputY < 3; inputY++)
			{
				ItemStack[] ingredient = ingredients.get((inputX) + (3 * inputY)).getStackArray();
				if (ingredient.length != 0)
				{
					if (!ingredient[0].isEmpty())
					{
						// Render the recipe component
						drawItemstackTooltip(ingredient[(int) (client.world.getTime() % ingredient.length)], x + (inputX * 25), y + (inputY * 25), mouseX, mouseY);

					}
				}
			}
		}

		// Draw the tooltip for the output item
		drawItemstackTooltip(output, x + 113, y + 25, mouseX, mouseY);
	}

	private ParchmentRecipeType getRecipeType(Recipe recipe)
	{
		if (recipe instanceof SmeltingRecipe)
		{
			return ParchmentRecipeType.SMELTING;
		} else if (recipe.fits(2, 2))
		{
			return ParchmentRecipeType.SMALL_CRAFTING;
		} else if (recipe.fits(3, 3))
		{
			return ParchmentRecipeType.LARGE_CRAFTING;
		} else
		{
			return ParchmentRecipeType.UNSUPPORTED;
		}
	}

	private void drawItemstackTooltip(ItemStack stack, int x, int y, int mouseX, int mouseY)
	{
		if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16)
		{
			if (stack != null && !stack.isEmpty())
			{
				GlStateManager.pushMatrix();

				// Actually draw the tooltip
				drawStackTooltip(stack, mouseX, mouseY);

				GlStateManager.popMatrix();
			}
		}
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	@Override
	public void onClosed()
	{
		super.onClosed();
		/*
		if (parchment.isAncient())
		{
			NetHandlerPlayClient connection = Minecraft.getMinecraft().getConnection();
			if (connection != null)
			{
				connection.sendPacket(new PacketAncientParchment());
			}
		}*/
	}

	enum ParchmentRecipeType
	{
		SMELTING, SMALL_CRAFTING, LARGE_CRAFTING, UNSUPPORTED
	}
}
