package com.raphydaphy.arcanemagic.common.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityInfernalSmelter extends TileEntityEssenceStorage implements ITickable
{
	public static int SIZE = 7;
	public static int ORE = 0;
	// crystals go in every slot above 0

	private int progress = 0;

	public TileEntityInfernalSmelter()
	{
		super(10000);
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		if (TileEntityInfernalSmelter.this.world != null && TileEntityInfernalSmelter.this.pos != null)
		{
			IBlockState state = TileEntityInfernalSmelter.this.world.getBlockState(TileEntityInfernalSmelter.this.pos);
			TileEntityInfernalSmelter.this.world.markAndNotifyBlock(TileEntityInfernalSmelter.this.pos,
					TileEntityInfernalSmelter.this.world.getChunkFromBlockCoords(TileEntityInfernalSmelter.this.pos), state, state,
					1 | 2);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
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
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			TileEntityInfernalSmelter.this.markDirty();
		}
		
		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack)
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

	public int getProgress()
	{
		return progress;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public void update()
	{
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos.add(-1,0,-1), pos.add(1,1,1));
	}
}
