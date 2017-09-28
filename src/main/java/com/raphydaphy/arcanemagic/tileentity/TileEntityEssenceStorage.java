package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.capabilities.EssenceStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * Base class for storing Essence using the capability. Extend me pls.
 */
public abstract class TileEntityEssenceStorage extends TileEntity {

    protected EssenceStorage essenceStorage = new EssenceStorage(this::markDirty);
    private static final String ESSENCE_KEY = "essence_storage";

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey(ESSENCE_KEY, Constants.NBT.TAG_LIST)){
            essenceStorage.deserializeNBT(compound);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag(ESSENCE_KEY, essenceStorage.serializeNBT());
        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == IEssenceStorage.CAP || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == IEssenceStorage.CAP ? IEssenceStorage.CAP.cast(essenceStorage) : super.getCapability(capability, facing);
    }
}
