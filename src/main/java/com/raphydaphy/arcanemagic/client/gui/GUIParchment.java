package com.raphydaphy.arcanemagic.client.gui;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFWGamepadState;

// TODO: sideonly client
public class GUIParchment extends GuiScreen
{
    // x and y size of the parchment texture
    private static final int DIMENSIONS = 64;
    private static final int TEX_HEIGHT = 69;

    private static final int PROGRESS_BAR_LENGTH = 48;

    private static final float scale = 3;
    private static final int SCALED_DIMENSIONS = (int) (DIMENSIONS * scale);

    private EntityPlayer player;
    private ItemStack stack;

    public GUIParchment(EntityPlayer player, ItemStack stack)
    {
        this.player = player;
        this.stack = stack;
    }

    @Override
    protected void initGui()
    {
        super.initGui();
        System.out.println("init gui");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        // The start x and y coords of the notebook on the screen
        int screenX = (mc.mainWindow.getScaledWidth() / 2) - (SCALED_DIMENSIONS / 2);
        int screenY = (mc.mainWindow.getScaledHeight() / 2) - (SCALED_DIMENSIONS / 2);

        mc.getTextureManager().bindTexture(ArcaneMagicResources.PARCHMENT);
        drawScaledCustomSizeModalRect(screenX, screenY, 0, 0, DIMENSIONS, DIMENSIONS,
                SCALED_DIMENSIONS, SCALED_DIMENSIONS, DIMENSIONS, TEX_HEIGHT);

        drawScaledCustomSizeModalRect(
                (int)(screenX + 8 * scale), (int)(screenY + 54 * scale), 0, DIMENSIONS,
                PROGRESS_BAR_LENGTH, 5, (int)(PROGRESS_BAR_LENGTH * scale), (int)((5) * scale), DIMENSIONS, TEX_HEIGHT);

        drawScaledCustomSizeModalRect(
                (int)(screenX + 9 * scale), (int)(screenY + 55 * scale), PROGRESS_BAR_LENGTH, DIMENSIONS + 1,
                1, 3, (int)(15 * scale), (int)((3) * scale), DIMENSIONS, TEX_HEIGHT);

        ArcaneMagicUtils.drawCenteredSplitString(mc.fontRenderer, I18n.format("parchment.arcanemagic.drowned_discovery"), mc.mainWindow.getScaledWidth() / 2, (int)(screenY + 28 * scale), 160,0);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton))
        {
            if (mouseButton == 0)
            {
                System.out.println("left click");
            }

            return true;
        }
        return false;
    }
}
