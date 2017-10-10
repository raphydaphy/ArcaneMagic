package com.raphydaphy.arcanemagic.integration.jei.category;

import java.util.List;

import com.raphydaphy.arcanemagic.integration.ArcaneMagicJEIPlugin;
import com.raphydaphy.arcanemagic.integration.jei.wrapper.WrapperElementalCrafting;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.config.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryElementalCrafting implements IRecipeCategory<WrapperElementalCrafting> {

	public static final int width = 116;
	public static final int height = 54;

	private final IDrawable background;

	public CategoryElementalCrafting(IGuiHelper guiHelper) {
		ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
		background = guiHelper.createDrawable(location, 0, 60, width, height);
	}

	@Override
	public String getUid() {
		return ArcaneMagicJEIPlugin.ELEMENTAL_CRAFTING_UID;
	}

	@Override
	public String getTitle() {
		return I18n.format("gui.arcanemagic.elemental_crafting");
	}

	@Override
	public String getModName() {
		return I18n.format("itemGroup.arcanemagic");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, WrapperElementalCrafting wrapper, IIngredients ingredients) {
		IGuiIngredientGroup<ItemStack> stacks = layout.getIngredientsGroup(ItemStack.class);
		wrapper.setRecipe(layout, ingredients);
		stacks.init(0, false, 94, 18);
		ItemStack output = ingredients.getOutputs(ItemStack.class).get(0).get(0);
		stacks.set(0, output);

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		for (int i = 0; i < 9; i++) {
			stacks.init(i+1, true, (i % 3) * 18, (i / 3) * 18);
			stacks.set(i+1, inputs.get(i));
		}
	}

}