package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.api.IParchment;
import com.raphydaphy.arcanemagic.client.gui.GuiParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

public class ParchmentDrownedDiscovery implements IParchment
{
    public static final String DROWNED_KILLS = "key.arcanemagic.drowned_discovery_kills";
    public static final String ALTAR_USED = "key.arcanemagic.altar_used";
    public static final String EARLY_WIZARD_HUT = "key.arcanemagic.early_wizard_hut";
    public static final String FOUND_WIZARD_HUT = "key.arcanemagic.found_wizard_hut";
    public static final String PREV_ANCIENT_PARCHMENT_USAGES = "key.arcanemagic.prev_ancient_parchment_usages";
    public static final String REOPENED_ANCIENT_PARCHMENT = "key.arcanemagic.reopened_ancient_parchment";

    private int drowned_kills;
    private ItemStack parchment;

    @Override
    public void setParchmentStack(ItemStack stack) {
        this.parchment = stack;
    }

    @Override
    public String getName()
    {
        return ArcaneMagicResources.DROWNED_DISCOVERY_PARCHMENT;
    }

    @Override
    public String getText() {
        if (!Objects.requireNonNull(parchment.getTagCompound()).getBoolean(ALTAR_USED)) {
            if (drowned_kills <= 3) return "parchment.arcanemagic.drowned_discovery_quest";
            else return "parchment.arcanemagic.drowned_discovery_altar";
        } else if (!Objects.requireNonNull(parchment.getTagCompound()).getBoolean(FOUND_WIZARD_HUT))
        {
            return "parchment.arcanemagic.drowned_discovery_find_a_hut";
        } else
            {
            if (Objects.requireNonNull(parchment.getTagCompound()).getBoolean(EARLY_WIZARD_HUT) && !Objects.requireNonNull(parchment.getTagCompound()).getBoolean(REOPENED_ANCIENT_PARCHMENT))
            {
                return "parchment.arcanemagic.drowned_discovery_already_found_hut";
            } else
                {
                if (Objects.requireNonNull(parchment.getTagCompound()).getBoolean(EARLY_WIZARD_HUT)) {
                    return "parchment.arcanemagic.drowned_discovery_inductor_old_hut";
                } else
                    {
                    return "parchment.arcanemagic.drowned_discovery_inductor_recent_hut";
                }
            }
        }
    }

    @Override
    public int getPercent() {
        drowned_kills = parchment.getTagCompound().getInteger(DROWNED_KILLS);
        drowned_kills = drowned_kills > 4 ? 4 : drowned_kills;
        return drowned_kills == 0 ? 0 : (int) ((GuiParchment.FULL_PROGRESS / 4.0f) * 100);
    }

    @Override
    public boolean showProgressBar() {
        return drowned_kills <= 3;
    }

    @Nullable
    @Override
    public IRecipe getRecipe() {
        if (drowned_kills <= 3) return null;
        else {
            RecipeManager manager = new RecipeManager();
            return manager.getRecipe(new ResourceLocation("arcanemagic:altar"));
        }
    }

    @Override
    public boolean isAncient() {
        return false;
    }
}
