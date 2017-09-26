package com.raphydaphy.arcanemagic.api.util;

public class IconTypePair
{
	private Object icon;
	private IconType type;

	public IconTypePair(Object icon, IconType type)
	{
		this.icon = icon;
		this.type = type;
	}

	public Object getTexture()
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
