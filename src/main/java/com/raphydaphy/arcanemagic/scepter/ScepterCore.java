package com.raphydaphy.arcanemagic.scepter;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.wand.IScepterRod;

import net.minecraft.util.ResourceLocation;

public class ScepterCore implements IScepterRod
{
	private String unlocalizedName;
	private ResourceLocation texture;

	public ScepterCore(String name)
	{
		this.unlocalizedName = "arcanemagic.scepter.core." + name;
		this.texture = new ResourceLocation(ArcaneMagic.MODID, "textures/items/scepter/core_" + name);
	}

	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return texture;
	}

}
