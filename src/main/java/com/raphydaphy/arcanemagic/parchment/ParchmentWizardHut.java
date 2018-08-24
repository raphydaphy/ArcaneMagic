package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.api.IParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nullable;

public class ParchmentWizardHut implements IParchment
{

    @Override
    public void setParchmentStack(ItemStack stack) {}

    @Override
    public String getName()
    {
        return ArcaneMagicResources.WIZARD_HUT_PARCHMENT;
    }

    @Override
    public String getText()
    {
        return "parchment.arcanemagic.wizard_hut_1";
    }

    @Override
    public double getPercent()
    {
        return 0;
    }


    @Override
    public boolean showProgressBar()
    {
        return false;
    }

    @Nullable
    @Override
    public IRecipe getRecipe()
    {
        return null;
    }

    @Override
    public boolean isAncient()
    {
        return true;
    }
}
