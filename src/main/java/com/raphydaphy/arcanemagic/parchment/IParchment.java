package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.client.gui.GUIParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public interface IParchment
{
    void drawParchment(Minecraft mc, int screenX, int screenY);
    String getName();

    default void bindTexture(Minecraft mc)
    {
        mc.getTextureManager().bindTexture(ArcaneMagicResources.PARCHMENT);
    }

    default void drawBackground(int screenX, int screenY)
    {
        GuiScreen.drawScaledCustomSizeModalRect(screenX, screenY, 0, 0, GUIParchment.DIMENSIONS, GUIParchment.DIMENSIONS,
                GUIParchment.SCALED_DIMENSIONS, GUIParchment.SCALED_DIMENSIONS, GUIParchment.DIMENSIONS, GUIParchment.TEX_HEIGHT);
    }

    default void drawProgressBar(int screenX, int screenY, int progress)
    {
        GuiScreen.drawScaledCustomSizeModalRect(
                (int) (screenX + 8 * GUIParchment.SCALE), (int) (screenY + 54 * GUIParchment.SCALE), 0, GUIParchment.DIMENSIONS,
                GUIParchment.PROGRESS_BAR_LENGTH, 5, (int) (GUIParchment.PROGRESS_BAR_LENGTH * GUIParchment.SCALE), (int) ((5) * GUIParchment.SCALE), GUIParchment.DIMENSIONS, GUIParchment.TEX_HEIGHT);

        if (progress > 0)
        {
            GuiScreen.drawScaledCustomSizeModalRect(
                    (int) (screenX + 9 * GUIParchment.SCALE), (int) (screenY + 55 * GUIParchment.SCALE), GUIParchment.PROGRESS_BAR_LENGTH, GUIParchment.DIMENSIONS + 1,
                    1, 3, (int) (progress * GUIParchment.SCALE), (int) ((3) * GUIParchment.SCALE), GUIParchment.DIMENSIONS, GUIParchment.TEX_HEIGHT);
        }
    }

    default void drawText(Minecraft mc, String unlocalizedText, int screenY, int yOffset)
    {
        ArcaneMagicUtils.drawCenteredSplitString(mc.fontRenderer, I18n.format(unlocalizedText), mc.mainWindow.getScaledWidth() / 2, (int)(screenY + yOffset * GUIParchment.SCALE), 160,0);
    }
}
