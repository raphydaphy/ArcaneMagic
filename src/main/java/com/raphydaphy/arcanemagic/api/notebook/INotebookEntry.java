package com.raphydaphy.arcanemagic.api.notebook;

import com.raphydaphy.arcanemagic.api.util.IconTypePair;
import com.raphydaphy.arcanemagic.api.util.Pos2;

import net.minecraft.util.ResourceLocation;

public interface INotebookEntry
{
	/*
	 *  The name displayed at the start of the page
	 */
	public String getUnlocalizedName();

	/*
	 *  The icon displayed in the notebook
	 */
	public IconTypePair getIcon();

	/*
	 *  The position in the notebook
	 */
	public Pos2 getPos();
	
	/**
	 * The name of this entry
	 * @return
	 */
	public ResourceLocation getRegistryName();
}
