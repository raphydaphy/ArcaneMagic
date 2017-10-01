package com.raphydaphy.arcanemagic.api.scepter;

import javax.annotation.Nonnull;

import net.minecraft.util.ResourceLocation;

public interface ScepterPart
{

	String getUnlocalizedName();

	@Nonnull
	ResourceLocation getTexture();

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
