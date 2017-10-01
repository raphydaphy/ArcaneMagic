package com.raphydaphy.arcanemagic.notebook.entry;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.client.gui.GuiNotebook;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NotebookEntryCraftingRecipe implements INotebookEntry
{
	private ItemStack[][] recipeIn;
	private ItemStack recipeOut;

	public NotebookEntryCraftingRecipe(ItemStack[][] recipeIn, ItemStack recipeOut)
	{
		this.recipeIn = recipeIn;
		this.recipeOut = recipeOut;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		return new ResourceLocation(ArcaneMagic.MODID, "notebook_entry_crafting_recipe");
	}

	@Override
	public void drawPost(int x, int y, int mouseX, int mouseY, GuiScreen notebook)
	{
		// New matrix!
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();

		// Adjust the x and y values to fit the tooltips properly
		x += 22;
		y += 3;

		// Render the tooltips for the recipe matrix
		for (int inputX = 0; inputX < 3; inputX++)
		{
			for (int inputY = 0; inputY < 3; inputY++)
			{
				if (recipeIn[inputY][inputX] != ItemStack.EMPTY)
				{
					// Render the tooltip if the mouse is over the item
					drawItemstackTooltip(recipeIn[inputY][inputX], x + (inputX * 25), y + (inputY * 25), mouseX, mouseY,
							notebook);
				}
			}
		}

		// Draw the tooltip for the output item
		drawItemstackTooltip(recipeOut, x + 113, y + 25, mouseX, mouseY, notebook);

		// Go back to the main render matrix
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	@Override
	public void draw(int x, int y, int mouseX, int mouseY, GuiScreen notebook)
	{
		// Make an embedded matrix for rendering crafting recipes
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();

		// Adjust the x and y to make renderering easier
		x += 22;
		y += 3;

		// Bind the actual notebook texture
		notebook.mc.getTextureManager().bindTexture(GuiNotebook.notebook);

		// Draw the crafting grid
		Gui.drawScaledCustomSizeModalRect(x + 20, y - 3, 18, 18, 1, 1, 2, 73, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);
		Gui.drawScaledCustomSizeModalRect(x - 3, y + 20, 18, 18, 1, 1, 73, 2, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);
		Gui.drawScaledCustomSizeModalRect(x + 45, y - 3, 18, 18, 1, 1, 2, 73, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);
		Gui.drawScaledCustomSizeModalRect(x - 3, y + 45, 18, 18, 1, 1, 73, 2, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);

		// Draw the crafting output box
		Gui.drawScaledCustomSizeModalRect(x + 108, y + 20, 18, 18, 1, 1, 26, 2, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);
		Gui.drawScaledCustomSizeModalRect(x + 108, y + 44, 18, 18, 1, 1, 26, 2, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);
		Gui.drawScaledCustomSizeModalRect(x + 108, y + 20, 18, 18, 1, 1, 2, 25, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);
		Gui.drawScaledCustomSizeModalRect(x + 132, y + 20, 18, 18, 1, 1, 2, 25, GuiNotebook.NOTEBOOK_WIDTH,
				GuiNotebook.NOTEBOOK_TEX_HEIGHT);

		// This involves rendering items, so we need to ensure they don't turn black
		RenderHelper.enableGUIStandardItemLighting();

		// Render the crafting recipe
		for (int inputX = 0; inputX < 3; inputX++)
		{
			for (int inputY = 0; inputY < 3; inputY++)
			{
				if (recipeIn[inputY][inputX] != ItemStack.EMPTY)
				{
					// Render the recipe component
					notebook.mc.getRenderItem().renderItemAndEffectIntoGUI(recipeIn[inputY][inputX], x + (inputX * 25),
							y + (inputY * 25));

				}
			}
		}

		// Draw the arrow to show the output of the recipe
		notebook.mc.getTextureManager().bindTexture(GuiNotebook.notebook);
		Gui.drawScaledCustomSizeModalRect(x + 78, y + 26, 158, 182, 22, 15, (int) (22), (int) (15),
				GuiNotebook.NOTEBOOK_WIDTH, GuiNotebook.NOTEBOOK_TEX_HEIGHT);

		// Draw the output item in the recipe
		notebook.mc.getRenderItem().renderItemAndEffectIntoGUI(recipeOut, x + 113, y + 25);

		// Go back to the main render matrix
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	private void drawItemstackTooltip(ItemStack stack, int x, int y, int mouseX, int mouseY, GuiScreen notebook)
	{
		if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16)
		{
			if (stack != null && !stack.isEmpty())
			{
				// A seperate matrix because tooltip rendering changes a bunch of things that we don't want
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();

				// Cache the itemstack for drawing
				net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);

				// Actually draw the tooltip
				notebook.drawHoveringText(notebook.getItemToolTip(stack), mouseX, mouseY);

				// Goodbye cached itemstack
				net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();

				// Matrix now gone see u again soon
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
			}
		}
	}

	@Override
	public int getHeight(GuiScreen notebook)
	{
		return 74;
	}

}
