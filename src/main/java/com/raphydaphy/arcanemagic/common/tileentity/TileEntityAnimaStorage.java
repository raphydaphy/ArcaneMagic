package com.raphydaphy.arcanemagic.common.tileentity;

import javax.annotation.Nullable;

import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;
import com.raphydaphy.arcanemagic.common.capabilities.AnimaStorage;

import net.minecraft.block.state.IBlockState;
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
public abstract class TileEntityAnimaStorage extends TileEntity
{

	protected AnimaStorage animaStorage;
	private static final String ANIMA_KEY = "anima_storage";

	public TileEntityAnimaStorage(int capacity)
	{
		TileEntityAnimaStorage that = this;
		animaStorage = new AnimaStorage(() ->
		{
			that.markDirty();
			if (this.world != null && this.pos != null)
			{
				IBlockState state = this.world.getBlockState(this.pos);
				this.world.markAndNotifyBlock(this.pos, this.world.getChunkFromBlockCoords(this.pos), state, state,
						1 | 2);
			}
		}, capacity);
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
		animaStorage.deserializeNBT(compound.getCompoundTag(ANIMA_KEY));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag(ANIMA_KEY, animaStorage.serializeNBT());
		return compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == IAnimaStorage.CAP || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == IAnimaStorage.CAP ? IAnimaStorage.CAP.cast(animaStorage)
				: super.getCapability(capability, facing);
	}
}