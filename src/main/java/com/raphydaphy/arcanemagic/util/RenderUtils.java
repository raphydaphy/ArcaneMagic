package com.raphydaphy.arcanemagic.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.cooking.SmeltingRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RenderUtils
{
	/**
	 * Draws a string split across multiple lines when needed
	 * @return The total height of the string
	 */
	public static int drawSplitString(TextRenderer textRenderer, String text, float x, float y, int wrap, int color, boolean verticallyCentered, boolean horizontallyCentered)
	{
		List<String> strings = textRenderer.wrapStringToWidthAsList(text, wrap);

		if (verticallyCentered)
		{

			y -= (strings.size() / 2f) * textRenderer.fontHeight;
		}
		for (String s : strings)
		{
			textRenderer.draw(s, horizontallyCentered ? (x - textRenderer.getStringWidth(s) / 2f) : x, y, color);
			y += textRenderer.fontHeight;
		}

		return strings.size() * textRenderer.fontHeight;
	}

	public static int drawCustomSizedSplitString(int x, int y, double scale, int width, int color, boolean verticallyCentered, boolean horizontallyCentered, String unlocalizedText, Object... keys)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scaled(scale, scale, scale);
		int height = RenderUtils.drawSplitString(MinecraftClient.getInstance().textRenderer, I18n.translate(unlocalizedText, keys),
		                                         (int) (x / scale), (int) (y / scale), (int) (width / scale), color, verticallyCentered, horizontallyCentered);
		GlStateManager.popMatrix();
		return (int) (height * scale);
	}

	@Environment(EnvType.CLIENT)
	public static void rotateTo(Direction dir)
	{
		if (dir == Direction.EAST)
		{
			GlStateManager.rotated(-90, 0, 1, 0);
			GlStateManager.translated(0, 0, -1);
		} else if (dir == Direction.SOUTH)
		{
			GlStateManager.rotated(-180, 0, 1, 0);
			GlStateManager.translated(-1, 0, -1);
		} else if (dir == Direction.WEST)
		{
			GlStateManager.rotated(-270, 0, 1, 0);
			GlStateManager.translated(-1, 0, 0);
		}
	}


	/**
	 * A smarter version of renderBox
	 * Takes coordinates in pixels, and adds
	 * uses the start coordinates and size to
	 * calculate the end coordinates
	 */
	public static void renderCube(BufferBuilder builder, double x, double y, double z, double xSize, double ySize, double zSize, TextureBounds[] textures)
	{
		double pixel = 0.0625;
		renderBox(builder, x * pixel, y * pixel, z * pixel, (x + xSize) * pixel, (y + ySize) * pixel, (z + zSize) * pixel, 255, 255, 255, 255, textures, new int[]{1, 1, 1, 1, 1, 1});
	}

	/**
	 * Draws a box with the specified dimensions
	 * Expects drawing in GL_QUADS mode
	 * Set u/v or maxU/V of any side to 0 to remove it
	 */
	public static void renderBox(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, TextureBounds[] textures, int[] inversions)
	{
		renderBox(builder, x1, y1, z1, x2, y2, z2, 255, 255, 255, 255, textures, inversions);
	}

	public static void renderBox(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, int r, int g, int b, int a, TextureBounds[] textures, int[] inversions)
	{
		if (textures[0].u != 0 || textures[0].maxU != 0)
		{
			// Bottom
			builder.vertex(x1, y1, z1).texture(textures[0].u, textures[0].v).color(r, g, b, a).normal(0, -1 * inversions[0], 0).next();
			builder.vertex(x1, y1, z2).texture(textures[0].maxU, textures[0].v).color(r, g, b, a).normal(0, -1 * inversions[0], 0).next();
			builder.vertex(x2, y1, z2).texture(textures[0].maxU, textures[0].maxV).color(r, g, b, a).normal(0, -1 * inversions[0], 0).next();
			builder.vertex(x2, y1, z1).texture(textures[0].u, textures[0].maxV).color(r, g, b, a).normal(0, -1 * inversions[0], 0).next();
		}

		if (textures[1].u != 0 || textures[1].maxU != 0)
		{
			// Top
			builder.vertex(x1, y2, z1).texture(textures[1].u, textures[1].v).color(r, g, b, a).normal(0, inversions[1], 0).next();
			builder.vertex(x1, y2, z2).texture(textures[1].maxU, textures[1].v).color(r, g, b, a).normal(0, inversions[1], 0).next();
			builder.vertex(x2, y2, z2).texture(textures[1].maxU, textures[1].maxV).color(r, g, b, a).normal(0, inversions[1], 0).next();
			builder.vertex(x2, y2, z1).texture(textures[1].u, textures[1].maxV).color(r, g, b, a).normal(0, inversions[1], 0).next();
		}

		if (textures[2].u != 0 || textures[2].maxU != 0)
		{
			// North
			builder.vertex(x1, y1, z1).texture(textures[2].u, textures[2].v).color(r, g, b, a).normal(0, 0, -1 * inversions[2]).next();
			builder.vertex(x2, y1, z1).texture(textures[2].maxU, textures[2].v).color(r, g, b, a).normal(0, 0, -1 * inversions[2]).next();
			builder.vertex(x2, y2, z1).texture(textures[2].maxU, textures[2].maxV).color(r, g, b, a).normal(0, 0, -1 * inversions[2]).next();
			builder.vertex(x1, y2, z1).texture(textures[2].u, textures[2].maxV).color(r, g, b, a).normal(0, 0, -1 * inversions[2]).next();
		}

		if (textures[3].u != 0 || textures[3].maxU != 0)
		{
			// South
			builder.vertex(x1, y1, z2).texture(textures[3].u, textures[3].v).color(r, g, b, a).normal(0, 0, inversions[3]).next();
			builder.vertex(x2, y1, z2).texture(textures[3].maxU, textures[3].v).color(r, g, b, a).normal(0, 0, inversions[3]).next();
			builder.vertex(x2, y2, z2).texture(textures[3].maxU, textures[3].maxV).color(r, g, b, a).normal(0, 0, inversions[3]).next();
			builder.vertex(x1, y2, z2).texture(textures[3].u, textures[3].maxV).color(r, g, b, a).normal(0, 0, inversions[3]).next();
		}

		if (textures[4].u != 0 || textures[4].maxU != 0)
		{
			// West
			builder.vertex(x1, y1, z1).texture(textures[4].u, textures[4].v).color(r, g, b, a).normal(-1 * inversions[4], 0, 0).next();
			builder.vertex(x1, y1, z2).texture(textures[4].maxU, textures[4].v).color(r, g, b, a).normal(-1 * inversions[4], 0, 0).next();
			builder.vertex(x1, y2, z2).texture(textures[4].maxU, textures[4].maxV).color(r, g, b, a).normal(-1 * inversions[4], 0, 0).next();
			builder.vertex(x1, y2, z1).texture(textures[4].u, textures[4].maxV).color(r, g, b, a).normal(-1 * inversions[4], 0, 0).next();
		}

		if (textures[5].u != 0 || textures[5].maxU != 0)
		{
			// East
			builder.vertex(x2, y1, z1).texture(textures[5].u, textures[5].v).color(r, g, b, a).normal(inversions[5], 0, 0).next();
			builder.vertex(x2, y1, z2).texture(textures[5].maxU, textures[5].v).color(r, g, b, a).normal(inversions[5], 0, 0).next();
			builder.vertex(x2, y2, z2).texture(textures[5].maxU, textures[5].maxV).color(r, g, b, a).normal(inversions[5], 0, 0).next();
			builder.vertex(x2, y2, z1).texture(textures[5].u, textures[5].maxV).color(r, g, b, a).normal(inversions[5], 0, 0).next();
		}
	}

	public static void drawRecipeItems(Recipe<? extends Inventory> recipe, int x, int y)
	{
		MinecraftClient client = MinecraftClient.getInstance();

		DefaultedList<Ingredient> ingredients = recipe.getPreviewInputs();

		GlStateManager.pushMatrix();

		if (!recipe.getPreviewInputs().isEmpty())
		{
			DrawableHelper.fill(x + 20, y - 3, x + 20 + 2, y - 3 + 73, 0xff422c0e);
			DrawableHelper.fill(x - 3, y + 20, x - 3 + 73, y + 20 + 2, 0xff422c0e);
			DrawableHelper.fill(x + 45, y - 3, x + 45 + 2, y - 3 + 73, 0xff422c0e);
			DrawableHelper.fill(x - 3, y + 45, x - 3 + 73, y + 45 + 2, 0xff422c0e);

			GlStateManager.pushMatrix();

			GuiLighting.enableForItems();

			boolean thin = recipe.fits(1, 3);

			for (int inputX = thin ? 1 : 0; inputX < (thin ? 2 : 3); inputX++)
			{
				for (int inputY = 0; inputY < 3; inputY++)
				{
					int slot = thin ? inputY : (inputX + (3 * inputY));
					if (slot < ingredients.size())
					{
						ItemStack[] stackArray = ingredients.get(slot).getStackArray();
						if (stackArray.length > 0)
						{
							int id = (int) (client.world.getTime() / 30) % stackArray.length;
							if (!stackArray[id].isEmpty())
							{
								// Render the recipe component
								client.getItemRenderer().renderGuiItem(stackArray[id], x + (inputX * 25),
								                                       y + (inputY * 25));
							}
						}
					}
				}
			}

			GlStateManager.popMatrix();
		}

		GuiLighting.disable();

		GlStateManager.popMatrix();

	}

	public static void drawRecipeOutput(Recipe<? extends Inventory> recipe, int x, int y)
	{
		GlStateManager.pushMatrix();

		// Crafting Output Box
		box(x, y, 24, 24, 2, 0xff422c0e, -1);

		// Draw the output item
		GuiLighting.enableForItems();
		MinecraftClient.getInstance().getItemRenderer().renderGuiItem(recipe.getOutput(), x + 5, y + 5);
		GuiLighting.disable();
		GlStateManager.popMatrix();
	}

	public static void drawRecipeTooltips(Screen screen, Recipe<? extends Inventory> recipe, int x, int y, int mouseX, int mouseY)
	{
		DefaultedList<Ingredient> ingredients = recipe.getPreviewInputs();
		boolean thin = recipe.fits(1, 3);
		// Render the tooltips for the recipe matrix
		for (int inputX = thin ? 1 : 0; inputX < (thin ? 2 : 3); inputX++)
		{
			for (int inputY = 0; inputY < 3; inputY++)
			{
				int slot = thin ? inputY : (inputX + (3 * inputY));
				if (slot < ingredients.size())
				{
					ItemStack[] stackArray = ingredients.get(slot).getStackArray();
					if (stackArray.length != 0)
					{
						int id = (int) (MinecraftClient.getInstance().world.getTime() / 30) % stackArray.length;
						if (!stackArray[id].isEmpty())
						{
							// Render the recipe component
							drawItemstackTooltip(screen, stackArray[id], x + (inputX * 25), y + (inputY * 25), mouseX, mouseY);
						}
					}
				}
			}
		}
	}

	public static void drawRecipe(Screen screen, BiConsumer<Integer, Integer> drawArrow, Recipe<? extends Inventory> recipe, int x, int y, int mouseX, int mouseY)
	{
		drawRecipeItems(recipe, x, y);
		drawRecipeOutput(recipe, x + 108, y + 21);

		GlStateManager.pushMatrix();
		drawArrow.accept(x + 78, y + 26);
		GlStateManager.popMatrix();

		drawRecipeTooltips(screen, recipe, x, y, mouseX, mouseY);

		// Draw the tooltip for the output item
		drawItemstackTooltip(screen, recipe.getOutput(), x + 113, y + 25, mouseX, mouseY);
	}

	public static int drawItemInBox(Screen screen, ItemStack item, List<String> tooltip, int color, int x, int y, int mouseX, int mouseY)
	{
		MinecraftClient client = MinecraftClient.getInstance();
		GlStateManager.pushMatrix();
		GlStateManager.pushTextureAttributes();

		box(x, y, 24, 24, 2, color, -1);

		if (!item.isEmpty())
		{
			GuiLighting.enableForItems();
			client.getItemRenderer().renderGuiItem(item, x + 5, y + 5);
			client.getItemRenderer().renderGuiItemOverlay(client.textRenderer, item, x + 5, y + 5, null);
			GuiLighting.disable();

			if (tooltip != null && !tooltip.isEmpty() && mouseX >= x + 5 && mouseY >= y + 5 && mouseX <= x + 21 && mouseY <= y + 21)
			{
				// Actually draw the tooltip
				screen.renderTooltip(tooltip, mouseX, mouseY);

			}
		}

		GlStateManager.popAttributes();
		GlStateManager.popMatrix();

		return 26;
	}

	public static void drawRequiredItems(Screen screen, Map<Ingredient, Boolean> items, int x, int y, int mouseX, int mouseY)
	{
		MinecraftClient client = MinecraftClient.getInstance();
		GlStateManager.pushMatrix();

		int i = 0;
		for (Map.Entry<Ingredient, Boolean> item : items.entrySet())
		{
			box(x + i * 35, y, 24, 24, 2, 0xff422c0e, items.get(item.getKey()) ? 0x8010ce40 : 0x80e80d0d);

			GuiLighting.enableForItems();
			ItemStack[] stackArray = item.getKey().getStackArray();
			if (stackArray.length != 0)
			{
				int id = (int) (client.world.getTime() / 30) % stackArray.length;
				if (!stackArray[id].isEmpty())
				{
					// Render the recipe component
					client.getItemRenderer().renderGuiItem(stackArray[id], x + 5 + i * 35, y + 4);
				}
			}
			GuiLighting.disable();
			i++;
		}

		i = 0;
		for (Map.Entry<Ingredient, Boolean> item : items.entrySet())
		{
			ItemStack[] stackArray = item.getKey().getStackArray();
			if (stackArray.length != 0)
			{
				int id = (int) (MinecraftClient.getInstance().world.getTime() / 30) % stackArray.length;
				if (!stackArray[id].isEmpty())
				{
					// Render the recipe component
					drawItemstackTooltip(screen, stackArray[id], x + 5 + i * 35, y + 4, mouseX, mouseY);
				}
			}
			i++;
		}

		GlStateManager.popMatrix();
	}

	public static void drawItemstackTooltip(Screen screen, ItemStack stack, int x, int y, int mouseX, int mouseY)
	{
		if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16)
		{
			if (stack != null && !stack.isEmpty())
			{
				GlStateManager.pushMatrix();

				// Actually draw the tooltip
				screen.renderTooltip(screen.getTooltipFromItem(stack), mouseX, mouseY);

				GlStateManager.popMatrix();
			}
		}
	}

	/**
	 * Draws a brown box with the given dimensions.
	 * @param background The background color. Set to -1 for no background
	 */
	public static void box(int x, int y, int width, int height, int lineWidth, int border, int background)
	{
		DrawableHelper.fill(x, y, x + width, y + lineWidth, border);
		DrawableHelper.fill(x, y + height, x + width, y + height + lineWidth, border);
		DrawableHelper.fill(x, y, x + lineWidth, y + height, border);
		DrawableHelper.fill(x + width, y, x + width + lineWidth, y + height + lineWidth, border);
		if (background != -1)
		{
			DrawableHelper.fill(x + lineWidth, y + lineWidth, x + width, y + width, background);
		}
	}

	public static int texRect(int x, int y, int u, int v, int maxU, int maxV, int width, int height, int textureWidth, int textureHeight)
	{
		DrawableHelper.blit(x, y, width, height, u, v, maxU, maxV, textureWidth, textureHeight);
		return height;
	}

	public static List<String> wrapText(String unlocalized, int width)
	{
		return MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(I18n.translate(unlocalized), width);
	}

	public static class TextureBounds
	{
		double u;
		double v;
		double maxU;
		double maxV;

		public TextureBounds(double u, double v, double maxU, double maxV)
		{
			this(u, v, maxU, maxV, 16, 16);
		}

		public TextureBounds(double u, double v, double maxU, double maxV, double textureWidth, double textureHeight)
		{
			this.u = u / textureWidth;
			this.v = v / textureHeight;
			this.maxU = maxU / textureWidth;
			this.maxV = maxV / textureHeight;
		}
	}
}
