package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;

public class TileEntityPedestal extends TileEntityInventory implements ITickable
{
    public static final int SLOT = 0;

    private int time = 0;
    private int prevTime = 0;

    public TileEntityPedestal()
    {
        super(ArcaneMagic.PEDESTAL_TE, "container.arcanemagic.pedestal", 1);
    }

    public void sync()
    {
        markDirty();
        if (world != null)
        {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.getUpdateTag();
        writeContents(tag);
        return tag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTag = super.getUpdateTag();
        this.writeContents(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), -1, nbtTag);
    }

    @Override
    public void update()
    {
        if (world.isRemote)
        {
            prevTime = time;
            time++;

            if (time > Integer.MAX_VALUE / 4)
            {
                time = 0;
            }
        }
    }

    // TODO: sideonly client ?
    public int getTime()
    {
        return time;
    }

    // TODO: sideonly client?
    public int getPrevTime()
    {
        return prevTime;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return getStackInSlot(index).isEmpty();
    }
}