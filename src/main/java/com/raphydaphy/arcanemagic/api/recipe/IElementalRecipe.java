package com.raphydaphy.arcanemagic.api.recipe;

import javax.annotation.Nonnull;

import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public interface IElementalRecipe
{

	/**
	 * @return The required essence for this recipe, may be empty.
	 */
	@Nonnull
	public EssenceStack getReqEssence();

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(ItemStack wand, NonNullList<ItemStack> stacks, World world);

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
		if (!getReqEssence().isEmpty())
			wand.getCapability(IEssenceStorage.CAP, null).take(getReqEssence(), false);
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
