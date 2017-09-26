package com.raphydaphy.arcanemagic.api.vis;

import com.raphydaphy.arcanemagic.api.ThaumcraftAPI;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum Vis implements IStringSerializable
{
	AER("thaumcraft.vis.aer", TextFormatting.YELLOW), AQUA("thaumcraft.vis.aqua", TextFormatting.AQUA), IGNIS(
			"thaumcraft.vis.ignis",
			TextFormatting.RED), ORDO("thaumcraft.vis.ordo", TextFormatting.WHITE), PERDITIO("thaumcraft.vis.perditio",
					TextFormatting.BLACK), TERRA("thaumcraft.vis.terra", TextFormatting.GREEN);

	private String unlocalizedName;
	private TextFormatting format;

	public static final String KEY = "stored_vis";
	public static final String MAX_KEY = "max_vis";
	public static final String DISCOUNT_KEY = "vis_discount";

	Vis(String unlocalizedName, TextFormatting format)
	{
		this.unlocalizedName = unlocalizedName;
		this.format = format;

		ThaumcraftAPI.VIS_TYPES.put(unlocalizedName, this);
	}

	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	public TextFormatting getColor()
	{
		return format;
	}

	public static Vis getByName(String name)
	{
		return ThaumcraftAPI.VIS_TYPES.get(name);
	}

	public String getMultiKey()
	{
		return KEY + "_" + unlocalizedName;
	}

	public String getMultiMaxKey()
	{
		return MAX_KEY + "_" + unlocalizedName;
	}

	public String getMultiDiscountKey()
	{
		return DISCOUNT_KEY + "_" + unlocalizedName;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getName()
	{
		return I18n.format(unlocalizedName);
	}

}