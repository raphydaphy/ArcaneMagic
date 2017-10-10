package com.raphydaphy.arcanemagic.capabilities;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class NotebookInfo implements INBTSerializable<NBTTagCompound>, ICapabilityProvider
{
	// Current category selected
	public static final String tagCategory = "notebookCategory";

	// Page within the selected category
	public static final String tagPage = "notebookPage";

	// Page on the list of pages
	public static final String tagIndexPage = "notebookIndexPage";

	// If the player has ever used a notebook
	public static final String tagUsedNotebook = "usedNotebook";

	@CapabilityInject(NotebookInfo.class)
	public static Capability<NotebookInfo> CAP = null;

	private Map<String, Boolean> unlockedCategories = new HashMap<>();
	private int curCategory;
	private int curPage;
	private int curIndexPage;
	private boolean usedNotebook;

	public NotebookInfo()
	{
		System.out.println("new notebookinfo made!");
		curCategory = 0;
		curPage = 0;
		curIndexPage = 0;
		usedNotebook = false;
	}

	public static class DefaultInfo implements Capability.IStorage<NotebookInfo>
	{

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<NotebookInfo> capability, NotebookInfo instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<NotebookInfo> capability, NotebookInfo instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}

	public void setUsed(boolean used)
	{
		this.usedNotebook = true;
	}

	public void setPage(int page)
	{
		this.curPage = page;
	}

	public void setIndexPage(int indexPage)
	{
		this.curIndexPage = indexPage;
	}

	public void setCategory(int category)
	{
		this.curCategory = category;
	}

	public boolean getUsed()
	{
		return this.usedNotebook;
	}

	public int getPage()
	{
		return this.curPage;
	}

	public int getIndexPage()
	{
		return this.curIndexPage;
	}

	public int getCategory()
	{
		return this.curCategory;
	}

	public void setUnlocked(String tag)
	{
		if (unlockedCategories.containsKey(tag))
		{
			if (!unlockedCategories.get(tag))
			{
				unlockedCategories.remove(tag);
			}
		}

		unlockedCategories.put(tag, true);
	}

	public boolean isUnlocked(String tag)
	{
		if (tag.equals(tagUsedNotebook))
		{
			return true;
		}
		if (unlockedCategories.containsKey(tag))
		{
			return unlockedCategories.get(tag);
		}
		return false;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		System.out.println("serialised with used: " + usedNotebook + ", category: " + curCategory);
		NBTTagCompound tag = new NBTTagCompound();
		for (String cat : unlockedCategories.keySet())
		{
			tag.setBoolean("notebook_info_" + cat, unlockedCategories.get(cat));
		}

		tag.setBoolean(tagUsedNotebook, usedNotebook);
		tag.setInteger(tagCategory, curCategory);
		tag.setInteger(tagPage, curPage);
		tag.setInteger(tagIndexPage, curIndexPage);

		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

		for (String key : nbt.getKeySet())
		{
			if (key.length() > 14 && key.substring(0, 14).equals("notebook_info_"))
			{
				System.out.println(
						"Deserialized key: " + key + " as " + key.substring(14) + " with value " + nbt.getBoolean(key));
				unlockedCategories.put(key.substring(14), nbt.getBoolean(key));
			}
		}

		usedNotebook = nbt.getBoolean(tagUsedNotebook);
		curCategory = nbt.getInteger(tagCategory);
		curPage = nbt.getInteger(tagPage);
		curIndexPage = nbt.getInteger(tagIndexPage);

		System.out.println("deserialized with used: " + usedNotebook + ", category: " + curCategory);

	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == NotebookInfo.CAP;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == NotebookInfo.CAP ? NotebookInfo.CAP.cast(this) : null;
	}
}
