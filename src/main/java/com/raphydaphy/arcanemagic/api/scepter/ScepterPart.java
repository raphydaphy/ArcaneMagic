package com.raphydaphy.arcanemagic.api.scepter;

import javax.annotation.Nonnull;

import net.minecraft.util.ResourceLocation;

public interface ScepterPart
{

	/**
	 * The unlocalized name
	 * @return The internal unlocalizedName variable of this part, prefixed with .part
	 */
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
