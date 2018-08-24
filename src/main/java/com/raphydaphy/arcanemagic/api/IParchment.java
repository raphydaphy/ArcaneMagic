package com.raphydaphy.arcanemagic.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nullable;

public interface IParchment
{
    /**
     * @return the unlocalized name of the parchment
     */
    String getName();

    /**
     * @return the current unlocalized text for the parchment
     */
    String getText();

    /**
     * @return the current percentage of progress for the parchment, from 0 to 1
     */
    double getPercent();

    /**
     * @return true if the parchment should be an Ancient Parchment item
     */
    boolean isAncient();

    /**
     * @return true if a progress bar should be shown currently
     */
    boolean showProgressBar();

    /**
     * @return the recipe to display, if necessary
     * return null to not display a recipe
     */
    @Nullable
    IRecipe getRecipe();

    void setParchmentStack(ItemStack stack);

}
