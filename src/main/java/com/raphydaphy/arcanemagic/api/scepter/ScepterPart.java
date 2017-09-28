package com.raphydaphy.arcanemagic.api.scepter;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class ScepterPart
{

	protected final PartCategory category;

	public ScepterPart(PartCategory category)
	{
		this.category = category;
	}

	public abstract String getUnlocalizedName();

	public abstract @Nonnull ResourceLocation getTexture();

	public abstract ResourceLocation getRegistryName();

	public PartCategory getType()
	{
		return category;
	}

	public static enum PartCategory
	{
		CAP, CORE,
	}

	public float getCostMultiplier()
	{
		return 1;
	}
}
