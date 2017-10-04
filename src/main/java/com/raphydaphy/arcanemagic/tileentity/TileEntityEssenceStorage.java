package com.raphydaphy.arcanemagic.tileentity;

import javax.annotation.Nullable;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.capabilities.EssenceStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Base class for storing Essence using the capability. Extend me pls.
 *
 * ok
 */
public abstract class TileEntityEssenceStorage extends TileEntity
{

	protected EssenceStorage essenceStorage;
	private static final String ESSENCE_KEY = "essence_storage";

	public TileEntityEssenceStorage(int capacity)
	{
		essenceStorage = new EssenceStorage(this::markDirty, capacity);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		// shadows told me to put 150 so i did
		return new SPacketUpdateTileEntity(this.pos, 150, this.getUpdateTag());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		essenceStorage.deserializeNBT(compound.getCompoundTag(ESSENCE_KEY));
		System.out.println("Read essence from compound ! Values: " + essenceStorage.getStored().values());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag(ESSENCE_KEY, essenceStorage.serializeNBT());
		System.out.println("Wrote essence to compound ! Values: " + essenceStorage.getStored().values());
		return compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == IEssenceStorage.CAP || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == IEssenceStorage.CAP ? IEssenceStorage.CAP.cast(essenceStorage)
				: super.getCapability(capability, facing);
	}
}