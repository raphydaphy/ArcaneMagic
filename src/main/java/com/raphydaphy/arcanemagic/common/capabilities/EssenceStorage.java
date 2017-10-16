package com.raphydaphy.arcanemagic.common.capabilities;

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
	private int capacity;

	public EssenceStorage()
	{
		this(() ->
		{
		}, 1000);
	}

	public EssenceStorage(@Nonnull Runnable s, @Nonnull int c)
	{
		Preconditions.checkNotNull(s);
		this.saveFunc = s;
		this.capacity = c;
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
	public int getTotalStored()
	{
		int total = 0;
		for (EssenceStack e : getStored().values())
		{
			total += e.getCount();
		}
		return total;
	}

	@Override
	public EssenceStack store(EssenceStack in, boolean simulate)
	{
		if (storage.containsKey(in.getEssence()))
		{
			int amountToDiscardOnGrow = in.getCount() + storage.get(in.getEssence()).getCount()
					- getCapacity(in.getEssence());
			if (amountToDiscardOnGrow <= 0)
			{
				if (!simulate)
				{
					storage.get(in.getEssence()).grow(in.getCount());
					markDirty();
				}
			} else
			{
				if (!simulate)
				{
					storage.get(in.getEssence()).grow(in.getCount() - amountToDiscardOnGrow);
					markDirty();
				}
				return new EssenceStack(in.getEssence(), amountToDiscardOnGrow);
			}
		} else
		{
			int amountToDiscardOnPut = in.getCount() - getCapacity(in.getEssence());
			if (amountToDiscardOnPut <= 0)
			{
				if (!simulate)
				{
					storage.put(in.getEssence(), in.copy());
					markDirty();
				}
			} else
			{
				if (!simulate)
				{
					storage.put(in.getEssence(),
							new EssenceStack(in.getEssence(), in.getCount() - amountToDiscardOnPut));
					markDirty();
				}
				return new EssenceStack(in.getEssence(), amountToDiscardOnPut);
			}
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

	@Override
	public int getCapacity(Essence type)
	{
		return capacity;
	}

	@Override
	public EssenceStack take(EssenceStack out, boolean simulate)
	{
		if (storage.containsKey(out.getEssence()))
		{
			if (storage.get(out.getEssence()).getCount() - out.getCount() >= 0)
			{
				if (!simulate)
				{
					storage.get(out.getEssence()).shrink(out.getCount());
					markDirty();
				}
				return null;
			} else
			{
				int amountTaken = storage.get(out.getEssence()).getCount();
				if (!simulate)
				{
					storage.get(out.getEssence()).setCount(0);
					markDirty();
				}
				return new EssenceStack(out.getEssence(), out.getCount() - amountTaken);
			}
		}
		markDirty();
		return new EssenceStack(out.getEssence(), out.getCount());
	}
}
