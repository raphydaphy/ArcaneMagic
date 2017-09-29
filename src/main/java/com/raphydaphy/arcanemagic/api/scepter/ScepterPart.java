package com.raphydaphy.arcanemagic.api.scepter;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface ScepterPart
{

	String getUnlocalizedName();

	@Nonnull ResourceLocation getTexture();

	ResourceLocation getRegistryName();

	PartCategory getType();

	enum PartCategory
	{
		TIP, CORE,
	}

	default float getCostMultiplier()
	{
		return 1;
	}
}
