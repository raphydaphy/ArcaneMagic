package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.client.gui.GUIParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;

public class ParchmentDrownedDiscovery implements IParchment
{
    @Override
    public void drawParchment(Minecraft mc, int screenX, int screenY)
    {
        bindTexture(mc);
        drawBackground(screenX, screenY);
        drawProgressBar(screenX, screenY, GUIParchment.FULL_PROGRESS);
        drawText(mc,"parchment.arcanemagic.drowned_discovery", screenY, 28);
    }

    @Override
    public String getName()
    {
        return ArcaneMagicResources.DROWNED_DISCOVERY;
    }
}
