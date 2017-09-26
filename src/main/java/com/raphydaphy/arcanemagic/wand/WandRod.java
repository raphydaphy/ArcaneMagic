package com.raphydaphy.arcanemagic.wand;

import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.api.wand.IWandRod;

import net.minecraft.util.ResourceLocation;

public class WandRod implements IWandRod
{
	private String unlocalizedName;
	private ResourceLocation texture;
	
	public WandRod(String name)
	{
		this.unlocalizedName = "thaumcraft.wand.rod." + name;
		this.texture = new ResourceLocation(Thaumcraft.MODID, "textures/items/wand/wand_rod_" + name);
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
