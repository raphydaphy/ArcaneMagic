package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.anima.AnimaReceiveMethod;
import com.raphydaphy.arcanemagic.anima.IAnimaInductible;
import com.raphydaphy.arcanemagic.anima.IAnimaReceiver;
import com.raphydaphy.arcanemagic.block.BlockInductor;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityInductor extends TileEntityAnimaStorage implements IAnimaInductible
{
    private static final int CAPACITY = 10000;
    private static final String MODE_KEY = "block_mode";

    private boolean blockMode = true;

    public TileEntityInductor()
    {
        super(ArcaneMagic.INDUCTOR_TE);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        if (tag.hasKey(MODE_KEY))
        {
            blockMode = tag.getBoolean(MODE_KEY);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean(MODE_KEY, blockMode);
        return tag;
    }

    public boolean setMode(boolean blockMode)
    {
        if (!world.isRemote)
        {
            this.blockMode = blockMode;
            world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockInductor.BLOCK_MODE, blockMode));
            markDirty();
        }
        return this.blockMode;
    }

    public boolean getMode()
    {
        return blockMode;
    }

    @Override
    public boolean takeAnima(int anima)
    {
        if (getCurrentAnima() >= anima)
        {
            this.anima -= anima;
            this.markDirty();
            return true;
        }
        return false;
    }
}
