package com.raphydaphy.arcanemagic.common.data;

import java.util.Locale;

import com.raphydaphy.arcanemagic.api.anima.Anima;

public enum EnumBasicAnimus implements IPropertyEnum
{
	OZONE(Anima.OZONE), DEPTH(Anima.DEPTH), INFERNO(Anima.INFERNO), HORIZON(Anima.HORIZON), PEACE(Anima.PEACE), CHAOS(
			Anima.CHAOS);

	final Anima anima;

	EnumBasicAnimus(Anima anima)
	{
		this.anima = anima;
	}

	public Anima getAnima()
	{
		return anima;
	}

	public int getColor()
	{
		return anima.getColorInt();
	}

	@Override
	public String getName()
	{
		return "anima_" + this.name().toLowerCase(Locale.ROOT);
	}
}
