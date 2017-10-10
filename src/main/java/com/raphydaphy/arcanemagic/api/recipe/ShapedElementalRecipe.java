package com.raphydaphy.arcanemagic.api.recipe;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack.ImmutableEssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShapedElementalRecipe implements IElementalRecipe
{

	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;
	private final ImmutableEssenceStack essence;

	public ShapedElementalRecipe(ItemStack output, NonNullList<Ingredient> inputs, EssenceStack reqEssence)
	{
		Preconditions.checkArgument(inputs.size() == 9);
		this.output = output;
		this.inputs = inputs;
		this.essence = reqEssence.toImmutable();
	}

	@Override
	public EssenceStack getReqEssence()
	{
		return essence;
	}

	@Override
	public boolean matches(ItemStack wand, NonNullList<ItemStack> stacks, World world)
	{
		for (int i = 0; i < 9; i++)
		{
			if (this.inputs.get(i) == Ingredient.EMPTY && stacks.get(i).isEmpty())
				continue;
			if (!this.inputs.get(i).apply(stacks.get(i)))
				return false;
		}
		return essence.isEmpty() || wand.getCapability(IEssenceStorage.CAP, null).take(essence, true) == null;
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
