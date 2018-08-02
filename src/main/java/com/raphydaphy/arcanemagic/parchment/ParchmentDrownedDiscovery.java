package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.gui.GuiParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ParchmentDrownedDiscovery implements IParchment
{
    public static final String DROWNED_KILLS = "key.arcanemagic.drowned_discovery_kills";

    @Override
    public void drawParchment(ItemStack parchment, GuiParchment gui, Minecraft mc, int screenX, int screenY, int mouseX, int mouseY)
    {
        int kills = 0;
        if (parchment.getTagCompound() != null)
        {
            kills = parchment.getTagCompound().getInteger(DROWNED_KILLS);
            kills = kills > 4 ? 4 : kills;
        }
        int progression = kills == 0 ? 0 : (int)((GuiParchment.FULL_PROGRESS / 4.0f) * kills);

        bindTexture(mc);
        drawBackground(screenX, screenY);

        if (kills <= 3)
        {
            drawProgressBar(screenX, screenY, progression);
            drawText(mc,"parchment.arcanemagic.drowned_discovery_1", screenY + 28 * GuiParchment.SCALE);
        }
        else
        {
            ItemStack[][] altar =
                    {
                            { ItemStack.EMPTY, new ItemStack(Items.GOLD_INGOT), ItemStack.EMPTY},
                            { new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.COAL_BLOCK)},
                            { new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.OBSIDIAN), new ItemStack(Blocks.COAL_BLOCK)}
                    };

            drawText(mc,"parchment.arcanemagic.drowned_discovery_2", screenY + 60);
            drawCraftingRecipe(gui, mc, altar, new ItemStack(ArcaneMagic.ALTAR), screenX + 31, (int)(screenY + 37 * GuiParchment.SCALE), mouseX, mouseY);
        }


    }

    @Override
    public String getName()
    {
        return ArcaneMagicResources.DROWNED_DISCOVERY;
    }
}
