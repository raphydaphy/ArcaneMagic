package com.raphydaphy.arcanemagic.util;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent.Register;

public interface IHasRecipe {

	public void initRecipes(Register<IRecipe> e);
	
}
