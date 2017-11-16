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
	
	public void setUsed(boolean used);

	public void setPage(int page);

	public void setIndexPage(int indexPage);

	public void setCategory(int category);

	public void setUnlocked(String tag);

	public void setSearchKey(String notebookSearchKey);

	public boolean getUsed();

	public int getPage();

	public int getIndexPage();

	public int getCategory();

	public String getSearchKey();

	public boolean isUnlocked(String tag);
	
	public boolean isVisible(NotebookCategory cat);

	@SideOnly(Side.CLIENT)
	public boolean matchesSearchKey(NotebookCategory cat);
}
