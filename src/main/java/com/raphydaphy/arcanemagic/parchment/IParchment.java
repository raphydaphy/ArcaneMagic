package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.client.gui.GuiParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public interface IParchment
{
    String getName();

    // TODO: sideonly client
    void drawParchment(ItemStack parchment, GuiParchment gui, Minecraft mc, int screenX, int screenY, int mouseX, int mouseY);

    // TODO: sideonly client
    default void bindTexture(Minecraft mc)
    {
        mc.getTextureManager().bindTexture(ArcaneMagicResources.PARCHMENT);
    }

    // TODO: sideonly client
    default void drawBackground(int screenX, int screenY)
    {
        GuiScreen.drawScaledCustomSizeModalRect(screenX, screenY, 0, 0, GuiParchment.DIMENSIONS, GuiParchment.DIMENSIONS,
                GuiParchment.SCALED_DIMENSIONS, GuiParchment.SCALED_DIMENSIONS, GuiParchment.DIMENSIONS, GuiParchment.TEX_HEIGHT);
    }

    // TODO: sideonly client
    default void drawProgressBar(int screenX, int screenY, int progress)
    {
        GuiScreen.drawScaledCustomSizeModalRect(
                (int) (screenX + 8 * GuiParchment.SCALE), (int) (screenY + 54 * GuiParchment.SCALE), 0, GuiParchment.DIMENSIONS,
                GuiParchment.PROGRESS_BAR_LENGTH, 5, (int) (GuiParchment.PROGRESS_BAR_LENGTH * GuiParchment.SCALE), (int) ((5) * GuiParchment.SCALE), GuiParchment.DIMENSIONS, GuiParchment.TEX_HEIGHT);

        if (progress > 0)
        {
            GuiScreen.drawScaledCustomSizeModalRect(
                    (int) (screenX + 9 * GuiParchment.SCALE), (int) (screenY + 55 * GuiParchment.SCALE), GuiParchment.PROGRESS_BAR_LENGTH, GuiParchment.DIMENSIONS + 1,
                    1, 3, (int) (progress * GuiParchment.SCALE), (int) ((3) * GuiParchment.SCALE), GuiParchment.DIMENSIONS, GuiParchment.TEX_HEIGHT);
        }
    }

    // TODO: sideonly client
    default void drawCraftingRecipe(GuiParchment parchment, Minecraft mc, ItemStack[][] recipeIn, ItemStack recipeOut, int x, int y, int mouseX, int mouseY)
    {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        bindTexture(mc);

        // Draw the crafting grid
        GuiScreen.drawScaledCustomSizeModalRect(x + 20, y - 3, 0, GuiParchment.DIMENSIONS, 1, 1, 2, 73, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x - 3, y + 20, 0, GuiParchment.DIMENSIONS, 1, 1, 73, 2, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x + 45, y - 3, 0, GuiParchment.DIMENSIONS, 1, 1, 2, 73, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x - 3, y + 45, 0, GuiParchment.DIMENSIONS, 1, 1, 73, 2, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        RenderHelper.enableGUIStandardItemLighting();

        // Render the crafting recipe
        for (int inputX = 0; inputX < 3; inputX++)
        {
            for (int inputY = 0; inputY < 3; inputY++)
            {
                if (recipeIn[inputY][inputX] != ItemStack.EMPTY)
                {
                    // Render the recipe component
                    mc.getRenderItem().renderItemAndEffectIntoGUI(recipeIn[inputY][inputX], x + (inputX * 25),
                            y + (inputY * 25));

                }
            }
        }

        RenderHelper.disableStandardItemLighting();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        bindTexture(mc);

        // Draw the crafting arrow
        GuiScreen.drawScaledCustomSizeModalRect(x + 78, y + 26, 49,64, 7, 5, (22), (15), GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);

        // Draw the crafting output box
        GuiScreen.drawScaledCustomSizeModalRect(x + 108, y + 21, 0, GuiParchment.DIMENSIONS, 1, 1, 26, 2, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x + 108, y + 45, 0, GuiParchment.DIMENSIONS, 1, 1, 26, 2, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x + 108, y + 21, 0, GuiParchment.DIMENSIONS, 1, 1, 2, 25, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x + 132, y + 21, 0, GuiParchment.DIMENSIONS, 1, 1, 2, 25, GuiParchment.DIMENSIONS,
                GuiParchment.TEX_HEIGHT);

        // Draw the output item
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(recipeOut, x + 113, y + 26);
        RenderHelper.disableStandardItemLighting();

        // Render the tooltips for the recipe matrix
        for (int inputX = 0; inputX < 3; inputX++)
        {
            for (int inputY = 0; inputY < 3; inputY++)
            {
                if (recipeIn[inputY][inputX] != ItemStack.EMPTY)
                {
                    // Render the tooltip if the mouse is over the item
                    drawItemstackTooltip(recipeIn[inputY][inputX], x + (inputX * 25), y + (inputY * 25), mouseX, mouseY, parchment);
                }
            }
        }

        // Draw the tooltip for the output item
        drawItemstackTooltip(recipeOut, x + 113, y + 25, mouseX, mouseY, parchment);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    default void drawItemstackTooltip(ItemStack stack, int x, int y, int mouseX, int mouseY, GuiParchment parchment)
    {
        if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16)
        {
            if (stack != null && !stack.isEmpty())
            {
                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                // TODO: callforge hook
                //net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);

                // Actually draw the tooltip
                parchment.drawHoveringText(parchment.getItemToolTip(stack), mouseX, mouseY);

                // TODO: call forge hook
                //net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();

                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
        }
    }

    // TODO: sideonly client
    default void drawText(Minecraft mc, String unlocalizedText, float center)
    {
        ArcaneMagicUtils.drawCenteredSplitString(mc.fontRenderer, I18n.format(unlocalizedText), mc.mainWindow.getScaledWidth() / 2, (int)(center), 160,0);
    }
}
