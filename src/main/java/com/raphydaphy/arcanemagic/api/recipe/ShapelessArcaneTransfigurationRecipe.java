package com.raphydaphy.arcanemagic.api.recipe;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack.ImmutableAnimaStack;
import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShapelessArcaneTransfigurationRecipe implements IArcaneTransfigurationRecipe {

	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;
	private final ImmutableAnimaStack anima;

	public ShapelessArcaneTransfigurationRecipe(ItemStack output, NonNullList<Ingredient> inputs, AnimaStack reqAnima) {
		Preconditions.checkArgument(inputs.size() == 9,
				"Attempting to create invalid arcane transfiguration recipe! (Wrong input list size)");
		this.output = output;
		this.inputs = inputs;
		this.anima = reqAnima.toImmutable();
	}

	@Override
	public AnimaStack getReqAnima() {
		return anima;
	}

	/**
	 * For every ingredient, we need to check each itemstack. Then, if we find a
	 * match, mark that itemstack so it cannot be checked again.
	 */

	@Override
	public boolean matches(EntityPlayer player, ItemStack wand, NonNullList<ItemStack> stacks, World world) {
		List<ItemStack> toCheck = Lists.newArrayList(stacks);
		for (Ingredient i : this.inputs) {
			for (int ix = 0; ix < toCheck.size(); ix++) {
				if (i.apply(toCheck.get(ix))) {
					toCheck.remove(ix);
					break;
				} else if (i == Ingredient.EMPTY && toCheck.get(ix).isEmpty())
					toCheck.remove(ix);
			}
		}

		IArcaneTransfigurationItem crafter = (IArcaneTransfigurationItem) wand.getItem();
		if (!crafter.matches(this, player, wand, stacks, world))
			return false;
		return toCheck.isEmpty() && (anima.isEmpty()
				|| (crafter.containsAnimus() && wand.getCapability(IAnimaStorage.CAP, null).take(anima, true) == null));
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@Override
	public boolean isShapeless() {
		return true;
	}

}
