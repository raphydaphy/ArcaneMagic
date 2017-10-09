package com.raphydaphy.arcanemagic.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityWritingDesk extends TileEntity
{
	public static int SIZE = 2;

	public TileEntityWritingDesk()
	{
		
	}

	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			TileEntityWritingDesk.this.markDirty();
		}
	};

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("items"))
		{
			itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			// only extract with automation
			if (facing == null || facing == EnumFacing.DOWN)
			{
				return true;
			}
			return false;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		return super.getCapability(capability, facing);
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
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}
}
