package com.raphydaphy.arcanemagic.api.recipe;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack.ImmutableEssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;

import net.minecraft.entity.player.EntityPlayer;
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
		
		IElementalCraftingItem crafter = (IElementalCraftingItem) wand.getItem();
		if(!crafter.matches(this, player, wand, stacks, world)) return false;
		return essence.isEmpty() || (crafter.containsEssence() && wand.getCapability(IEssenceStorage.CAP, null).take(essence, true) == null);
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
