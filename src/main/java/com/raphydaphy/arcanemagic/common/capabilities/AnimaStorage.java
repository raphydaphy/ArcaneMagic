package com.raphydaphy.arcanemagic.common.capabilities;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;

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
public class AnimaStorage implements IAnimaStorage, ICapabilityProvider {

	private Map<Anima, AnimaStack> storage = new HashMap<>();
	private Runnable saveFunc;
	private int capacity;

	public AnimaStorage() {
		this(() -> {
		}, 1000);
	}

	public AnimaStorage(@Nonnull Runnable s, @Nonnull int c) {
		Preconditions.checkNotNull(s);
		this.saveFunc = s;
		this.capacity = c;
	}

	@Override
	public HashMap<Anima, AnimaStack> getStored() {
		HashMap<Anima, AnimaStack> ret = new HashMap<>();
		for (Map.Entry<Anima, AnimaStack> e : storage.entrySet()) {
			ret.put(e.getKey(), new AnimaStack.ImmutableAnimaStack(e.getValue()));
		}
		return ret;
	}

	@Override
	public int getTotalStored() {
		int total = 0;
		for (AnimaStack e : getStored().values()) {
			total += e.getCount();
		}
		return total;
	}

	@Override
	public AnimaStack store(AnimaStack in, boolean simulate) {
		if (storage.containsKey(in.getAnima())) {
			int amountToDiscardOnGrow = in.getCount() + storage.get(in.getAnima()).getCount()
					- getCapacity(in.getAnima());
			if (amountToDiscardOnGrow <= 0) {
				if (!simulate) {
					storage.get(in.getAnima()).grow(in.getCount());
					markDirty();
				}
			} else {
				if (!simulate) {
					storage.get(in.getAnima()).grow(in.getCount() - amountToDiscardOnGrow);
					markDirty();
				}
				return new AnimaStack(in.getAnima(), amountToDiscardOnGrow);
			}
		} else {
			int amountToDiscardOnPut = in.getCount() - getCapacity(in.getAnima());
			if (amountToDiscardOnPut <= 0) {
				if (!simulate) {
					storage.put(in.getAnima(), in.copy());
					markDirty();
				}
			} else {
				if (!simulate) {
					storage.put(in.getAnima(), new AnimaStack(in.getAnima(), in.getCount() - amountToDiscardOnPut));
					markDirty();
				}
				return new AnimaStack(in.getAnima(), amountToDiscardOnPut);
			}
		}
		markDirty();
		return null;
	}

	private void markDirty() {
		saveFunc.run();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		AnimaStack.writeToNBT(tag, storage.values());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		storage = AnimaStack.buildMapFromNBT(nbt);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IAnimaStorage.CAP;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == IAnimaStorage.CAP ? IAnimaStorage.CAP.cast(this) : null;
	}

	public static class DefaultStorage implements Capability.IStorage<IAnimaStorage> {

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IAnimaStorage> capability, IAnimaStorage instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IAnimaStorage> capability, IAnimaStorage instance, EnumFacing side,
				NBTBase nbt) {
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}

	@Override
	public int getCapacity(Anima type) {
		return capacity;
	}

	@Override
	public AnimaStack take(AnimaStack out, boolean simulate) {
		if (storage.containsKey(out.getAnima())) {
			if (storage.get(out.getAnima()).getCount() - out.getCount() >= 0) {
				if (!simulate) {
					storage.get(out.getAnima()).shrink(out.getCount());
					markDirty();
				}
				return null;
			} else {
				int amountTaken = storage.get(out.getAnima()).getCount();
				if (!simulate) {
					storage.get(out.getAnima()).setCount(0);
					markDirty();
				}
				return new AnimaStack(out.getAnima(), out.getCount() - amountTaken);
			}
		}
		markDirty();
		return new AnimaStack(out.getAnima(), out.getCount());
	}
}
