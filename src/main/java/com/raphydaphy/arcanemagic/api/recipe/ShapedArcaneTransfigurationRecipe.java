package com.raphydaphy.arcanemagic.api.recipe;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack.ImmutableAnimaStack;
import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShapedArcaneTransfigurationRecipe implements IArcaneTransfigurationRecipe
{

	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;
	private final ImmutableAnimaStack anima;

	public ShapedArcaneTransfigurationRecipe(ItemStack output, NonNullList<Ingredient> inputs, AnimaStack reqAnima)
	{
		Preconditions.checkArgument(inputs.size() == 9,
				"Attempting to create invalid arcane transfiguration recipe! (Wrong input list size)");
		this.output = output;
		this.inputs = inputs;
		this.anima = reqAnima.toImmutable();
	}

	@Override
	public AnimaStack getReqAnima()
	{
		return anima;
	}

	@Override
	public boolean matches(EntityPlayer player, ItemStack wand, NonNullList<ItemStack> stacks, World world)
	{
		for (int i = 0; i < 9; i++)
		{
			if (this.inputs.get(i) == Ingredient.EMPTY && stacks.get(i).isEmpty())
				continue;
			if (!this.inputs.get(i).apply(stacks.get(i)))
				return false;
		}

		IArcaneTransfigurationItem crafter = (IArcaneTransfigurationItem) wand.getItem();
		if (!crafter.matches(this, player, wand, stacks, world))
			return false;
		return anima.isEmpty() || (crafter.containsAnimus()
				&& wand.getCapability(IAnimaStorage.CAP, null).take(anima, true) == null);
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return inputs;
	}

	@Override
	public boolean isShapeless()
	{
		return false;
	}

}
