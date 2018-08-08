package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.gui.GuiParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class ParchmentDrownedDiscovery implements IParchment
{
    public static final String DROWNED_KILLS = "key.arcanemagic.drowned_discovery_kills";
    public static final String ALTAR_USED = "key.arcanemagic.altar_used";
    public static final String FOUND_WIZARD_HUT = "key.arcanemagic.found_wizard_hut";

    private static final ItemStack[][] altar =
            {
                    {ItemStack.EMPTY, new ItemStack(Items.GOLD_INGOT), ItemStack.EMPTY},
                    {new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.COAL_BLOCK)},
                    {new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.OBSIDIAN), new ItemStack(Blocks.COAL_BLOCK)}
            };

    private static final ItemStack[][] inductor =
            {
                    {ItemStack.EMPTY, new ItemStack(Blocks.OBSIDIAN), ItemStack.EMPTY},
                    {new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.GOLD_BLOCK), new ItemStack(Blocks.COAL_BLOCK)},
                    {ItemStack.EMPTY, new ItemStack(Blocks.OBSIDIAN), ItemStack.EMPTY}
            };

    @Override
    public void drawParchment(ItemStack parchment, GuiParchment gui, Minecraft mc, int screenX, int screenY, int mouseX, int mouseY)
    {
        bindTexture(mc);
        drawBackground(screenX, screenY);

        if (!Objects.requireNonNull(parchment.getTagCompound()).getBoolean(ALTAR_USED))
        {
            int drowned_kills = parchment.getTagCompound().getInteger(DROWNED_KILLS);
            drowned_kills = drowned_kills > 4 ? 4 : drowned_kills;
            int quest_progression = drowned_kills == 0 ? 0 : (int) ((GuiParchment.FULL_PROGRESS / 4.0f) * drowned_kills);

            if (drowned_kills <= 3)
            {
                drawProgressBar(screenX, screenY, quest_progression);
                drawText(mc, "parchment.arcanemagic.drowned_discovery_quest", screenY + 28 * GuiParchment.SCALE);
            } else
            {
                drawText(mc, "parchment.arcanemagic.drowned_discovery_altar", screenY + 60);
                drawCraftingRecipe(gui, mc, altar, new ItemStack(ArcaneMagic.ALTAR), screenX + 31, (int) (screenY + 37 * GuiParchment.SCALE), mouseX, mouseY);
            }
        }
        else if (!Objects.requireNonNull(parchment.getTagCompound()).getBoolean(FOUND_WIZARD_HUT))
        {
            drawText(mc, "parchment.arcanemagic.drowned_discovery_find_a_hut", screenY + 32 * GuiParchment.SCALE);
        } else
        {
            drawText(mc, "parchment.arcanemagic.drowned_discovery_inductor", screenY + 60);
            drawCraftingRecipe(gui, mc, inductor, new ItemStack(ArcaneMagic.INDUCTOR), screenX + 31, (int) (screenY + 37 * GuiParchment.SCALE), mouseX, mouseY);
        }
    }

    @Override
    public String getName()
    {
        return ArcaneMagicResources.DROWNED_DISCOVERY_PARCHMENT;
    }
}
