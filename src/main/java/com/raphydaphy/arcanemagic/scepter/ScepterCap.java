package com.raphydaphy.arcanemagic.scepter;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;

import net.minecraft.util.ResourceLocation;

public class ScepterCap extends ScepterPart
{
	public static final ScepterCap IRON = new ScepterCap("iron");
	public static final ScepterCap GOLD = new ScepterCap("gold");
	public static final ScepterCap THAUMIUM = new ScepterCap("thaumium");

	private final String UNLOC_NAME;
	private final ResourceLocation TEXTURE;
	private final ResourceLocation REGNAME;

	public ScepterCap(String name)
	{
		super(PartCategory.CAP);
		UNLOC_NAME = ArcaneMagic.MODID + ".cap." + name;
		TEXTURE = new ResourceLocation(ArcaneMagic.MODID, "textures/items/scepter/core_" + name);
		REGNAME = new ResourceLocation(ArcaneMagic.MODID, name);
	}

	@Override
	public String getUnlocalizedName()
	{
		return UNLOC_NAME;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		return REGNAME;
	}

}
