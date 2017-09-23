package com.raphydaphy.thaumcraft.api.research;

import com.raphydaphy.thaumcraft.api.util.IconTypePair;
import com.raphydaphy.thaumcraft.api.util.Pos2;

public interface IThaumonomiconEntry 
{
	// The name displayed at the start of the page
	public String getUnlocalizedName();
	
	// The icon displayed in the thaumonomicon
	public IconTypePair getIcon();
	
	// The position in the thaumonomicon
	public Pos2 getPos();
}
