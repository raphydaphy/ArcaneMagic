package com.raphydaphy.arcanemagic.scepter;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ScepterTip extends ScepterPart
{
	public static final ScepterTip IRON = new ScepterTip("iron");
	public static final ScepterTip GOLD = new ScepterTip("gold");
	public static final ScepterTip THAUMIUM = new ScepterTip("thaumium");

	private final String UNLOC_NAME;
	private final ResourceLocation TEXTURE;
	private final ResourceLocation REGNAME;

	public ScepterTip(String name)
	{
		super(PartCategory.CAP);
		UNLOC_NAME = ArcaneMagic.MODID + ".cap." + name;
		TEXTURE = new ResourceLocation(ArcaneMagic.MODID, "textures/items/scepter/tip_" + name);
		REGNAME = new ResourceLocation(ArcaneMagic.MODID, name);
	}

	@Override
	public String getUnlocalizedName()
	{
		return UNLOC_NAME;
	}

	@Override
	public @Nonnull ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		return REGNAME;
	}

}
