package com.raphydaphy.arcanemagic.recipe;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ShapedTransfigurationRecipe implements TransfigurationRecipe
{
	private static final RecipeSerializer<ShapedTransfigurationRecipe> SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, ArcaneMagic.DOMAIN + "transfiguration", new ShapedTransfigurationRecipeSerializer());

	private final Identifier id;
	final ItemStack output;
	final DefaultedList<Ingredient> inputs;
	final int soul;
	final int width;
	final int height;

	public ShapedTransfigurationRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> inputs, int soul, int width, int height)
	{
		this.id = id;
		this.output = output;
		this.inputs = inputs;
		this.soul = soul;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean matches(Inventory inventory, World world)
	{
		for (int i = 0; i < inventory.getInvSize(); i++)
		{
			boolean recipeEmpty = i >= inputs.size() || this.inputs.get(i) == Ingredient.EMPTY;
			boolean invEmpty = inventory.getInvStack(i).isEmpty() || inventory.getInvStack(i).getItem() == Items.GLASS_PANE;
			if (recipeEmpty && invEmpty)
			{
				continue;
			}
			if (recipeEmpty != invEmpty)
			{
				return false;
			} else if (!this.inputs.get(i).method_8093(inventory.getInvStack(i))) // apply
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients()
	{
		return inputs;
	}

	@Override
	public int getSoul()
	{
		return soul;
	}

	@Override
	public boolean isShapeless()
	{
		return false;
	}

	@Override
	public boolean fits(int width, int height)
	{
		return width >= this.width && height >= this.height;
	}

	@Override
	public ItemStack getOutput()
	{
		return output;
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public RecipeSerializer<ShapedTransfigurationRecipe> getSerializer()
	{
		return SERIALIZER;
	}
}
