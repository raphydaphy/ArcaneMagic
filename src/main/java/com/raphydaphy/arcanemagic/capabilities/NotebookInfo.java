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

/**
 * Apparently u MUST call markDirty on change
 */
public class NotebookInfo implements  INBTSerializable<NBTTagCompound>, ICapabilityProvider
{
	@CapabilityInject(NotebookInfo.class)
	public static Capability<NotebookInfo> CAP = null;
	
	private Map<String, Boolean> unlockedCategories = new HashMap<>();
	
	public static class DefaultInfo implements Capability.IStorage<NotebookInfo>
	{

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<NotebookInfo> capability, NotebookInfo instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<NotebookInfo> capability, NotebookInfo instance, EnumFacing side,
				NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		for (String cat : unlockedCategories.keySet())
		{
			tag.setBoolean("notebook_info_" + cat, unlockedCategories.get(cat));
		}
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		for (String key : nbt.getKeySet())
		{
			if (key.substring(0, 14).equals("notebook_info_"))
			{
				System.out.println("Deserialized key: " + key + " as " + key.substring(14) + " with value " + nbt.getBoolean(key));
				unlockedCategories.put(key.substring(14), nbt.getBoolean(key));
			}
		}
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
