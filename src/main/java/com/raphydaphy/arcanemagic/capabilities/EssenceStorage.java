package com.raphydaphy.arcanemagic.capabilities;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation that has no limits.
 *
 * NB. MUST call markDirty on change
 */
public class EssenceStorage implements IEssenceStorage, ICapabilityProvider {

    private HashMap<Essence,EssenceStack> storage = new HashMap<>();
    private Runnable saveFunc;

    public EssenceStorage(){
        this(()->{});
    }

    public EssenceStorage(@Nonnull Runnable s){
        Preconditions.checkNotNull(s);
        this.saveFunc = s;
    }

    @Override
    public HashMap<Essence, EssenceStack> getStored() {
        HashMap<Essence, EssenceStack> ret = new HashMap<>();
        for (Map.Entry<Essence, EssenceStack> e : storage.entrySet()){
            ret.put(e.getKey(), new EssenceStack.ImmutableEssenceStack(e.getValue()));
        }
        return ret;
    }

    @Override
    public EssenceStack store(EssenceStack in, boolean simulate) {
        if (simulate)
            return null;
        if (storage.containsKey(in.getEssence())){
            storage.get(in.getEssence()).grow(in.getCount());
        } else {
            storage.put(in.getEssence(), in.copy());
        }
        markDirty();
        return null;
    }

    private void markDirty(){
        saveFunc.run();
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList tag = new NBTTagList();
        for (EssenceStack e : storage.values()){
            tag.appendTag(e.serializeNBT());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagList nbt) {
        if (nbt.getTagType() == Constants.NBT.TAG_COMPOUND){
            storage.clear();
            for (int i=0; i<nbt.tagCount(); i++){
                EssenceStack e = new EssenceStack(nbt.getCompoundTagAt(i));
                if (e.getEssence() != null){
                    if (storage.containsKey(e.getEssence())){
                        storage.get(e.getEssence()).grow(e.getCount());
                    } else {
                        storage.put(e.getEssence(), e);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == IEssenceStorage.CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == IEssenceStorage.CAP ? IEssenceStorage.CAP.cast(this) : null;
    }

    public static class DefaultStorage implements Capability.IStorage<IEssenceStorage>{

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IEssenceStorage> capability, IEssenceStorage instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IEssenceStorage> capability, IEssenceStorage instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagList)nbt);
        }
    }
}
