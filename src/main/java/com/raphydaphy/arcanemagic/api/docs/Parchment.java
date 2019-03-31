package com.raphydaphy.arcanemagic.api.docs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Hand;
import net.minecraft.world.IWorld;

import java.util.Collections;
import java.util.Map;

public interface Parchment
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

	@Environment(EnvType.CLIENT)
	default int getVerticalTextOffset()
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
	default Recipe<? extends Inventory> getRecipe(RecipeManager manager)
	{
		return null;
	}

	@Environment(EnvType.CLIENT)
	default int getVerticalFeatureOffset()
	{
		return 0;
	}

	/**
	 * @return a list of items to be displayed for collection at the bottom of the page
	 * If the list is empty, no items will be shown
	 */
	@Environment(EnvType.CLIENT)
	default Map<Ingredient, Boolean> getRequiredItems()
	{
		return Collections.emptyMap();
	}

	/**
	 * Called on the client-side when the parchment screen is opened
	 */
	@Environment(EnvType.CLIENT)
	default void initScreen(ItemStack stack)
	{

	}

	/**
	 * Called on the client and server when the parchment is opened
	 */
	default void onOpened(IWorld world, PlayerEntity player, Hand hand, ItemStack stack)
	{

	}

}