package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

public interface INotebookCategory
{
	/**
	 * The name of the category's tab
	 * @return The unlocalized name
	 */
	public String getUnlocalizedName();
	
	/**
	 * A list of all the entries within the category
	 * @return
	 */
	public List<INotebookEntry> getEntries();
}
