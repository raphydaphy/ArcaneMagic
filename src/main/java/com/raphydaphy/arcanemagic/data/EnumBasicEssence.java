package com.raphydaphy.arcanemagic.data;

import java.util.Locale;

import com.raphydaphy.arcanemagic.api.essence.Essence;

public enum EnumBasicEssence implements IPropertyEnum
{
	OZONE(Essence.OZONE), DEPTH(Essence.DEPTH), INFERNO(Essence.INFERNO), HORIZON(Essence.HORIZON), PEACE(Essence.PEACE), CHAOS(Essence.CHAOS), CREATION(Essence.CREATION);

	final Essence essence;

	EnumBasicEssence(Essence ess)
	{
		essence = ess;
	}

	public Essence getEssence()
	{
		return essence;
	}
	
	public int getColor()
	{
		return essence.getColorInt();
	}
	
	@Override
	public String getName()
	{
		return "essence_" + this.name().toLowerCase(Locale.ROOT);
	}
}
