package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.client.gui.GuiParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ParchmentWizardHut implements IParchment
{
    @Override
    public void drawParchment(ItemStack parchment, GuiParchment gui, Minecraft mc, int screenX, int screenY, int mouseX, int mouseY)
    {
        bindTexture(mc);
        drawBackground(screenX, screenY);
        drawText(mc,"parchment.arcanemagic.wizard_hut_1", screenY + 32 * GuiParchment.SCALE);
    }

    @Override
    public String getName()
    {
        return ArcaneMagicResources.WIZARD_HUT_PARCHMENT;
    }

    @Override
    public boolean isAncient()
    {
        return true;
    }
}
