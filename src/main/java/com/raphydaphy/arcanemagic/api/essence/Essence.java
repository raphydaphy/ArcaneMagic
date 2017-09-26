package com.raphydaphy.arcanemagic.api.essence;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum Essence implements IStringSerializable
{
	DEMONIC("arcanemagic.vis.demonic", 0xa5500b), AQUATIC("arcanemagic.vis.aquatic", 0x187ea3), 
	ATMOSPHERIC("arcanemagic.vis.atmospheric", 0xeaeaea), EARTHERN("arcanemagic.vis.earthern", 0x066018), 
			STABLE("arcanemagic.vis.stable", 0x303a1f), UNSTABLE("arcanemagic.vis.unstable", 0x85b72f);

	private String unlocalizedName;
	private int color;

	public static final String KEY = "stored_essence";
	public static final String MAX_KEY = "max_essence";

	Essence(String unlocalizedName, int color)
	{
		this.unlocalizedName = unlocalizedName;
		this.color = color;

		ArcaneMagicAPI.ESSENCE_TYPES.put(unlocalizedName, this);
	}

	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	public int getColor()
	{
		return color;
	}

	public static Essence getByName(String name)
	{
		return ArcaneMagicAPI.ESSENCE_TYPES.get(name);
	}

	public String getMultiKey()
	{
		return KEY + "_" + unlocalizedName;
	}

	public String getMultiMaxKey()
	{
		return MAX_KEY + "_" + unlocalizedName;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getName()
	{
		return I18n.format(unlocalizedName);
	}

}