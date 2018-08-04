package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.anima.AnimaReceiveMethod;
import com.raphydaphy.arcanemagic.anima.IAnimaInductible;
import com.raphydaphy.arcanemagic.anima.IAnimaReceiver;
import com.raphydaphy.arcanemagic.block.BlockInductor;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityInductor extends TileEntityAnimaStorage implements IAnimaInductible, ITickable
{
    private static final int CAPACITY = 10000;
    private static final String MODE_KEY = "block_mode";
    private static final String LINK_KEY = "link_pos";

    private boolean blockMode = true;
    private BlockPos link = null;

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
        if (tag.hasKey(LINK_KEY))
        {
            link = BlockPos.fromLong(tag.getLong(LINK_KEY));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean(MODE_KEY, blockMode);
        if (link != null)
        {
            tag.setLong(LINK_KEY, link.toLong());
        }
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

    public void setLinkPos(BlockPos pos)
    {
        if (!world.isRemote)
        {
            link = pos;
            markDirty();
        }
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

    @Override
    public void update()
    {
        if (!world.isRemote && world.getTotalWorldTime() % 50 == 0)
        {
            System.out.println("Pos: " + link);
        }
    }
}
