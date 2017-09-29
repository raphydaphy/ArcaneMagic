package com.raphydaphy.arcanemagic.scepter;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;

import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ScepterCore implements ScepterPart
{

	public static final ScepterCore WOOD = new ScepterCore("wood");
	public static final ScepterCore GREATWOOD = new ScepterCore("greatwood");
	public static final ScepterCore SILVERWOOD = new ScepterCore("silverwood");

	public static void registerDefaults(){
		ScepterRegistry.registerAll(ScepterCore.WOOD, ScepterCore.GREATWOOD,
				ScepterCore.SILVERWOOD);
	}

	private final String UNLOC_NAME;
	private final ResourceLocation TEXTURE;
	private final ResourceLocation REGNAME;

	public ScepterCore(String name)
	{
		UNLOC_NAME = ArcaneMagic.MODID + ".core." + name;
		TEXTURE = new ResourceLocation(ArcaneMagic.MODID, "textures/items/scepter/core_" + name);
		REGNAME = new ResourceLocation(ArcaneMagic.MODID, name);
	}

	@Override
	public String getUnlocalizedName()
	{
		return UNLOC_NAME;
	}

	@Override
	public @Nonnull	ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		return REGNAME;
	}

	@Override
	public PartCategory getType() {
		return PartCategory.CORE;
	}
}
