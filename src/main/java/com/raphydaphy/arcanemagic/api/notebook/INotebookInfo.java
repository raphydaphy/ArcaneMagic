package com.raphydaphy.arcanemagic.api.notebook;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface INotebookInfo extends INBTSerializable<NBTTagCompound>
{
	/* PSA: this field will be null UNLESS you have a hard dep on the mod */
	@CapabilityInject(INotebookInfo.class)
	static Capability<INotebookInfo> CAP = null;
	
	/*
	 * If set the true, the notebook will reset the players
	 * usage information the next time it is opened.
	 */
	public void setUsed(boolean used);

	/*
	 * Sets the currently open page in the notebook.
	 * Pages are within categories, which are the
	 * things on the sidebar.
	 */
	public void setPage(int page);

	/*
	 * Sets the currently selected page on the list
	 * of categories, to the left of the currently
	 * viewed page within the category.
	 */
	public void setIndexPage(int indexPage);

	/*
	 * Sets the currently viewed category, which
	 * is the tab selected on the left.
	 */
	public void setCategory(int category);

	/*
	 * Sets the category corresponding to
	 * the tag specified to be unlocked in the
	 * notebook. It will only be visible if
	 * the player also has unlocked the prerequisites
	 * for the category.
	 */
	public void setUnlocked(String tag);

	/*
	 * Sets the words that are currently entered
	 * into the search bar of the notebook.
	 */
	public void setSearchKey(String notebookSearchKey);

	/*
	 * Returns true if the player has already opened
	 * a notebook in their playthrough.
	 */
	public boolean getUsed();

	/*
	 * Gets the currently open page within the open
	 * category in the notebook.
	 */
	public int getPage();

	/*
	 * Gets thhe currently open page within the list
	 * of categories on the left side of the notebook.
	 */
	public int getIndexPage();

	/*
	 * Gets the currently open category in the players notebook.
	 */
	public int getCategory();

	/*
	 * Gets the words currently typed into the search
	 * bar in the players notebook.
	 */
	public String getSearchKey();

	/*
	 * Returns true if the category corresponding to the
	 * specified tag is unlocked in the players notebook.
	 * Will return true even if the category is not
	 * actually visible due to a lack of having the
	 * prerequisite category unlocked.
	 */
	public boolean isUnlocked(String tag);
	
	/*
	 * Returns true if the category is unlocked as well as
	 * the prerequisite categories. Therefore, the
	 * specified category is also visible in the notebook.
	 */
	public boolean isVisible(NotebookCategory cat);

	/*
	 * Returns true if the specified category should show
	 * up in the search results since it matches the
	 * currently entered search term.
	 */
	@SideOnly(Side.CLIENT)
	public boolean matchesSearchKey(NotebookCategory cat);
}
