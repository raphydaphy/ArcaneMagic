package com.raphydaphy.arcanemagic.api.notebook;

import com.raphydaphy.arcanemagic.api.util.IconTypePair;
import com.raphydaphy.arcanemagic.api.util.Pos2;

public interface INotebookEntry
{
	// The name displayed at the start of the page
	public String getUnlocalizedName();

	// The icon displayed in the thaumonomicon
	public IconTypePair getIcon();

	// The position in the thaumonomicon
	public Pos2 getPos();
}
