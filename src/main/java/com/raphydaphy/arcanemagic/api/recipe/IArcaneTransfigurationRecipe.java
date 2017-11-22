package com.raphydaphy.arcanemagic.api.recipe;

import javax.annotation.Nonnull;

import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public interface IArcaneTransfigurationRecipe
{

	/**
	 * @return The required anima for this recipe, may be empty.
	 */
	@Nonnull
	public AnimaStack getReqAnima();

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(EntityPlayer player, ItemStack wand, NonNullList<ItemStack> stacks, World world);

	/**
	 * @return The recipe output stack, with access to the input.
	 */
	public default ItemStack getCraftingResult(NonNullList<ItemStack> stacks)
	{
		return getRecipeOutput();
	}

	/**
	 * Transforms the inputs when crafting is done.
	 * @param inputs The current crafting grid.
	 */
	public default void craft(ItemStack wand, NonNullList<ItemStack> stacks)
	{
		for (ItemStack s : stacks)
			s.shrink(1);
		if (!getReqAnima().isEmpty())
			wand.getCapability(IAnimaStorage.CAP, null).take(getReqAnima(), false);
	}

	/**
	 * @return The recipe output stack.
	 */
	public ItemStack getRecipeOutput();

	/**
	 * @return The recipe ingredients.
	 */
	public NonNullList<Ingredient> getIngredients();

	public boolean isShapeless();

}
