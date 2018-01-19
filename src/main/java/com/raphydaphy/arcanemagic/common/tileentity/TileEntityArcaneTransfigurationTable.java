package com.raphydaphy.arcanemagic.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityArcaneTransfigurationTable extends TileEntity implements ITickable
{
	public static final int SIZE = 10;
	public int frameAge = 0;

	@Override
	public void update()
	{
		if (world.isRemote)
		{
			return;
		}
		markDirty();
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		if (TileEntityArcaneTransfigurationTable.this.world != null && TileEntityArcaneTransfigurationTable.this.pos != null)
		{
			IBlockState state = TileEntityArcaneTransfigurationTable.this.world
					.getBlockState(TileEntityArcaneTransfigurationTable.this.pos);
			TileEntityArcaneTransfigurationTable.this.world
					.markAndNotifyBlock(
							TileEntityArcaneTransfigurationTable.this.pos, TileEntityArcaneTransfigurationTable.this.world
									.getChunkFromBlockCoords(TileEntityArcaneTransfigurationTable.this.pos),
							state, state, 1 | 2);
		}
	}

	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			TileEntityArcaneTransfigurationTable.this.markDirty();

			if (TileEntityArcaneTransfigurationTable.this.world != null
					&& TileEntityArcaneTransfigurationTable.this.pos != null)
			{
				IBlockState state = TileEntityArcaneTransfigurationTable.this.world
						.getBlockState(TileEntityArcaneTransfigurationTable.this.pos);
				TileEntityArcaneTransfigurationTable.this.world.markAndNotifyBlock(
						TileEntityArcaneTransfigurationTable.this.pos, TileEntityArcaneTransfigurationTable.this.world
								.getChunkFromBlockCoords(TileEntityArcaneTransfigurationTable.this.pos),
						state, state, 1 | 2);
			}
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return 1;
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
		if (facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
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

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos, pos.add(1, 2, 1));
	}
}
