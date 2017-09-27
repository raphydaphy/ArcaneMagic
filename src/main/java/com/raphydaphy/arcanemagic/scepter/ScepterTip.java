package com.raphydaphy.arcanemagic.scepter;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.IScepterCap;

import net.minecraft.util.ResourceLocation;

public class ScepterTip implements IScepterCap
{
	private String unlocalizedName;
	private ResourceLocation texture;

	public ScepterTip(String name)
	{
		this.unlocalizedName = "arcanemagic.scepter.tip." + name;
		this.texture = new ResourceLocation(ArcaneMagic.MODID, "textures/items/scepter/tip_" + name);
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
