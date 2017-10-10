package com.raphydaphy.arcanemagic.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.util.RecipeHelper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import mezz.jei.config.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

@JEIPlugin
public class ArcaneMagicJEIPlugin implements IModPlugin {
	public static final String ELEMENTAL_CRAFTING_UID = "arcanemagic.elementalcrafting";

	public void register(IModRegistry registry) {
		registry.addRecipes(RecipeHelper.ELEMENTAL_RECIPES, ELEMENTAL_CRAFTING_UID);
		registry.handleRecipes(IElementalRecipe.class, ElementalCraftingWrapper::new, ELEMENTAL_CRAFTING_UID);
		registry.addRecipeCatalyst(new ItemStack(ModRegistry.ELEMENTAL_CRAFTING_TABLE), ELEMENTAL_CRAFTING_UID);
		registry.addIngredientInfo(new ItemStack(ModRegistry.ANCIENT_PARCHMENT), ItemStack.class, "desc.arcanemagic.ancient_parchment");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new ElementalCraftingCategory(guiHelper));
	}

	public class ElementalCraftingCategory implements IRecipeCategory<ElementalCraftingWrapper> {

		public static final int width = 116;
		public static final int height = 54;

		private final IDrawable background;

		public ElementalCraftingCategory(IGuiHelper guiHelper) {
			ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
			background = guiHelper.createDrawable(location, 0, 60, width, height);
		}

		@Override
		public String getUid() {
			return ELEMENTAL_CRAFTING_UID;
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
		public void setRecipe(IRecipeLayout layout, ElementalCraftingWrapper wrapper, IIngredients ingredients) {
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

	public class ElementalCraftingWrapper implements ICustomCraftingRecipeWrapper {

		private final IElementalRecipe recipe;

		public ElementalCraftingWrapper(IElementalRecipe rec) {
			recipe = rec;
		}

		@Override
		public void getIngredients(IIngredients ingredients) {
			List<List<ItemStack>> inputs = new ArrayList<>();
			for (Ingredient i : recipe.getIngredients())
				inputs.add(Arrays.asList(i.getMatchingStacks()));
			ingredients.setInputLists(ItemStack.class, inputs);
			ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
		}

		@Override
		public void setRecipe(IRecipeLayout layout, IIngredients input) {
			if (recipe.isShapeless()) layout.setShapeless();
		}

		@Override
		public void drawInfo(Minecraft mc, int width, int height, int mouseX, int mouseY) {
			//TODO draw essence stack in here or something
		}

	}
}
