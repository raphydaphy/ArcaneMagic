package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.recipe.IElementalCraftingItem;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent.Register;

public class ItemEssenceChannelingRod extends ItemBase implements IHasRecipe, IElementalCraftingItem
{
	public ItemEssenceChannelingRod()
	{
		super("essence_channeling_rod", TextFormatting.GRAY);
	}

	@Override
	public void initRecipes(Register<IRecipe> e)
	{
		RecipeHelper.addShaped(this, 3, 3, null, "dustGlowstone", "dustRedstone", null, "stickWood", "dustGlowstone",
				"stickWood", null, null);
	}

	@Override
	public boolean containsEssence()
	{
		return false;
	}

}
