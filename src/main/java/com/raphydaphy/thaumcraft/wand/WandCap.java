package com.raphydaphy.thaumcraft.wand;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.api.wand.IWandCap;

import net.minecraft.util.ResourceLocation;

public class WandCap implements IWandCap
{
	private String unlocalizedName;
	private ResourceLocation texture;
	
	public WandCap(String name)
	{
		this.unlocalizedName = "thaumcraft.wand.cap." + name;
		this.texture = new ResourceLocation(Thaumcraft.MODID, "textures/items/wand/wand_cap_" + name);
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
