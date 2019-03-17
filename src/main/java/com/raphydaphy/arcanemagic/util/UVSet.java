package com.raphydaphy.arcanemagic.util;

public class UVSet extends RenderUtils.TextureBounds
{
	public UVSet(double u, double v, double maxU, double maxV)
	{
		super(maxU + u, maxV + v, u, v);
	}
}