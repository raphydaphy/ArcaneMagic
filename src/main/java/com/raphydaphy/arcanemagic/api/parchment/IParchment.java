package com.raphydaphy.arcanemagic.api.parchment;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;

import java.util.Collections;
import java.util.List;
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
	@Environment(EnvType.CLIENT)
	String getText();

	/**
	 * If true, the text will automatically be centered not only horizontally but also vertically
	 */
	@Environment(EnvType.CLIENT)
	default boolean verticallyCenteredText()
	{
		return false;
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
	@Environment(EnvType.CLIENT)
	default boolean showProgressBar()
	{
		return false;
	}

	/**
	 * @return the current percentage of progress for the parchment, from 0 to 1
	 */
	@Environment(EnvType.CLIENT)
	default double getProgressPercent()
	{
		return 0;
	}

	/**
	 * @return the recipe to display, if necessary
	 * return null to not display a recipe
	 */
	@Environment(EnvType.CLIENT)
	default Recipe<? extends Inventory> getRecipe()
	{
		return null;
	}

	/**
	 * @return a list of items to be displayed for collection at the bottom of the page
	 * If the list is empty, no items will be shown
	 */
	@Environment(EnvType.CLIENT)
	default List<Ingredient> getRequiredItems()
	{
		return Collections.emptyList();
	}

	/**
	 * Provides the stack which the parchment is on
	 * Used to check NBT for rendering
	 */
	@Environment(EnvType.CLIENT)
	default void init(ItemStack stack)
	{

	}

}