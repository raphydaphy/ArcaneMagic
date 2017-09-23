package com.raphydaphy.thaumcraft.api.util;

import net.minecraft.util.ResourceLocation;

public class IconTypePair 
{
	private ResourceLocation icon;
	private IconType type;
	
	public IconTypePair(ResourceLocation icon, IconType type)
	{
		this.icon = icon;
		this.type = type;
	}
	
	public ResourceLocation getTexture()
	{
		return this.icon;
	}
	
	public IconType getType()
	{
		return this.type;
	}
	
	public enum IconType
	{
		CIRCLE("circle"), SQUARE("square"), CIRCLE_FANCY("circle_fancy"), SQUARE_FANCY("square_fancy");

		private final String name;

		IconType(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return this.name;
		}
	}
}
