package com.raphydaphy.arcanemagic.common.util;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

// TODO: get the player who is crafting the recipe... ? how
public class UnlockableRecipe implements IRecipe
{
	public UnlockableRecipe(String requiredTag)
	{
		
	}
	@Override
	public IRecipe setRegistryName(ResourceLocation name)
	{
		return null;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		return null;
	}

	@Override
	public Class<IRecipe> getRegistryType()
	{
		return null;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return null;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return false;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}

}
