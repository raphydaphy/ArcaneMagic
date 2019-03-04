package com.raphydaphy.arcanemagic.api.parchment;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;

import java.util.Optional;

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
	 * If true, the text will automatically be centered not only horizontally but also vertically
	 */
	default boolean verticallyCenteredText()
	{
		return false;
	}

	/**
	 * @return the current percentage of progress for the parchment, from 0 to 1
	 */
	default double getPercent()
	{
		return 0;
	}

	/**
	 * @return true if the parchment should be an Ancient Parchment item
	 */
	default boolean isAncient()
	{
		return false;
	}

	/**
	 * @return true if a progress bar should be shown currently
	 */
	default boolean showProgressBar()
	{
		return false;
	}

	/**
	 * @return the recipe to display, if necessary
	 * return null to not display a recipe
	 */
	default Recipe<? extends Inventory> getRecipe()
	{
		return null;
	}

	/**
	 * Provides the stack which the parchment is on
	 * Used to check NBT for rendering
	 */
	default void setParchmentStack(ItemStack stack)
	{

	}

}