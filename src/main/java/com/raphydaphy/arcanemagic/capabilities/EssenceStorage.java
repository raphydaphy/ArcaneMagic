package com.raphydaphy.arcanemagic.capabilities;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * Basic implementation that has no limits.
 *
 * NB. MUST call markDirty on change
 */
public class EssenceStorage implements IEssenceStorage, ICapabilityProvider
{

	private Map<Essence, EssenceStack> storage = new HashMap<>();
	private Runnable saveFunc;

	public EssenceStorage()
	{
		this(() ->
		{
		});
	}

	public EssenceStorage(@Nonnull Runnable s)
	{
		Preconditions.checkNotNull(s);
		this.saveFunc = s;
	}

	@Override
	public HashMap<Essence, EssenceStack> getStored()
	{
		HashMap<Essence, EssenceStack> ret = new HashMap<>();
		for (Map.Entry<Essence, EssenceStack> e : storage.entrySet())
		{
			ret.put(e.getKey(), new EssenceStack.ImmutableEssenceStack(e.getValue()));
		}
		return ret;
	}

	@Override
	public EssenceStack store(EssenceStack in, boolean simulate)
	{
		if (simulate)
			return null;
		if (storage.containsKey(in.getEssence()))
		{
			storage.get(in.getEssence()).grow(in.getCount());
		} else
		{
			storage.put(in.getEssence(), in.copy());
		}
		markDirty();
		return null;
	}

	private void markDirty()
	{
		saveFunc.run();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		EssenceStack.writeToNBT(tag, storage.values());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		storage = EssenceStack.buildMapFromNBT(nbt);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == IEssenceStorage.CAP;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == IEssenceStorage.CAP ? IEssenceStorage.CAP.cast(this) : null;
	}

	public static class DefaultStorage implements Capability.IStorage<IEssenceStorage>
	{

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IEssenceStorage> capability, IEssenceStorage instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IEssenceStorage> capability, IEssenceStorage instance, EnumFacing side,
				NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}
}
