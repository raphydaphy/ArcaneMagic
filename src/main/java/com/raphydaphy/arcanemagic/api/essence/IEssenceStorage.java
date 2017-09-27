package com.raphydaphy.arcanemagic.api.essence;

import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

/**
 * TODO more operations
 */
public interface IEssenceStorage extends INBTSerializable<NBTTagList> {

    /* NB: do NOT use this field UNLESS you have a hard dep on this mod */
    @CapabilityInject(IEssenceStorage.class)
    static Capability<IEssenceStorage> CAP = null;

    /**
     * @return a map of all the currently stored EssenceStacks
     */
    Map<Essence,EssenceStack> getStored();

    /**
     * Store the stack in the storage
     * @param in stack to store
     * @param simulate actually do the thing?
     * @return the amount that couldn't be stored, or null if everything got stored
     */
    EssenceStack store(EssenceStack in, boolean simulate);
}
