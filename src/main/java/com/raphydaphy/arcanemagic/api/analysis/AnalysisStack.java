package com.raphydaphy.arcanemagic.api.analysis;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public class AnalysisStack extends AnalysisIngredient {

	protected AnalysisStack(Ingredient ingred) {
		super(ingred);
	}

	protected AnalysisStack(ItemStack stack) {
		this(Ingredient.fromStacks(stack));
	}
	
	protected AnalysisStack(Item item) {
		this(Ingredient.fromItem(item));
	}

	protected AnalysisStack(String ore) {
		this(new OreIngredient(ore));
	}

	@Override
	public boolean apply(Object obj) {
		if (!(obj instanceof ItemStack)) return false;
		return ((Ingredient) this.source).apply((ItemStack) obj);
	}

}
