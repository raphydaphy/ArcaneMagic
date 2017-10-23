package com.raphydaphy.arcanemagic.api.recipe;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack.ImmutableEssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShapelessElementalRecipe implements IElementalRecipe
{

	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;
	private final ImmutableEssenceStack essence;

	public ShapelessElementalRecipe(ItemStack output, NonNullList<Ingredient> inputs, EssenceStack reqEssence)
	{
		Preconditions.checkArgument(inputs.size() == 9, "Attempting to create invalid elemental recipe! (Wrong input list size)");
		this.output = output;
		this.inputs = inputs;
		this.essence = reqEssence.toImmutable();
	}

	@Override
	public EssenceStack getReqEssence()
	{
		return essence;
	}

	/**
	 * For every ingredient, we need to check each itemstack.
	 * Then, if we find a match, mark that itemstack so it cannot be checked again.
	 */

	@Override
	public boolean matches(EntityPlayer player, ItemStack wand, NonNullList<ItemStack> stacks, World world)
	{	
		List<ItemStack> toCheck = Lists.newArrayList(stacks);
		for (Ingredient i : this.inputs)
		{
			for (int ix = 0; ix < toCheck.size(); ix++)
			{
				if (i.apply(toCheck.get(ix)))
				{
					toCheck.remove(ix);
					break;
				} else if (i == Ingredient.EMPTY && toCheck.get(ix).isEmpty())
					toCheck.remove(ix);
			}
		}
		
		IElementalCraftingItem crafter = (IElementalCraftingItem) wand.getItem();
		if(!crafter.matches(this, player, wand, stacks, world)) return false;
		return toCheck.isEmpty() && (essence.isEmpty() || (crafter.containsEssence() && wand.getCapability(IEssenceStorage.CAP, null).take(essence, true) == null));
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
		return true;
	}

}
