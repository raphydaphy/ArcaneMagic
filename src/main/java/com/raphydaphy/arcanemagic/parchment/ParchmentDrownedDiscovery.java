package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.client.gui.GUIParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ParchmentDrownedDiscovery implements IParchment
{
    public static final String DROWNED_KILLS = "key.arcanemagic.drowned_discovery_kills";

    @Override
    public void drawParchment(ItemStack parchment, Minecraft mc, int screenX, int screenY)
    {
        int kills = 0;
        if (parchment.getTagCompound() != null)
        {
            kills = parchment.getTagCompound().getInteger(DROWNED_KILLS);
            kills = kills > 4 ? 4 : kills;
        }
        int progression = kills == 0 ? 0 : (int)((GUIParchment.FULL_PROGRESS / 4.0f) * kills);

        bindTexture(mc);
        drawBackground(screenX, screenY);

        if (kills <= 3)
        {
            drawProgressBar(screenX, screenY, progression);
            drawText(mc,"parchment.arcanemagic.drowned_discovery_1", screenY, 28);
        }
        else
        {
            drawText(mc,"parchment.arcanemagic.drowned_discovery_2", screenY, 28);
        }


    }

    @Override
    public String getName()
    {
        return ArcaneMagicResources.DROWNED_DISCOVERY;
    }
}
