package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.api.parchment.IParchment;

public class TestParchment implements IParchment
{
	public static final String NAME = "progression.arcanemagic.test_parchment";

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getText()
	{
		return "parchment.arcanemagic.test";
	}

	@Override
	public boolean verticallyCenteredText()
	{
		return true;
	}
}
