package com.raphydaphy.arcanemagic.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPedestal extends TileEntity
{
	private ItemStack stack = ItemStack.EMPTY;

	private int frameAge = 0;

	public TileEntityPedestal()
	{
		super();
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
		markDirty();

		if (world != null)
		{
			IBlockState state = world.getBlockState(this.pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		if (TileEntityPedestal.this.world != null && TileEntityPedestal.this.pos != null)
		{
			IBlockState state = TileEntityPedestal.this.world.getBlockState(TileEntityPedestal.this.pos);
			TileEntityPedestal.this.world.markAndNotifyBlock(TileEntityPedestal.this.pos,
					TileEntityPedestal.this.world.getChunkFromBlockCoords(TileEntityPedestal.this.pos), state,
					state, 1 | 2);
		}
	}

	public int getFrameAge()
	{
		return frameAge;
	}

	public int increaseFrameAge()
	{
		frameAge++;

		return frameAge;
	}

	public int setFrameAge(int newAge)
	{
		frameAge = newAge;

		return frameAge;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(pos, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("item"))
		{
			stack = new ItemStack(compound.getCompoundTag("item"));
		} else
		{
			stack = ItemStack.EMPTY;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if (!stack.isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			stack.writeToNBT(tagCompound);
			compound.setTag("item", tagCompound);
		}
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
}
