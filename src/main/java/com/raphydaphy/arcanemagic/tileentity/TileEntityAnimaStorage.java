package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.anima.IAnimaBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityAnimaStorage extends TileEntity implements IAnimaBlock
{
    private static final String ANIMA_KEY = "anima";
    int anima = 0;

    public TileEntityAnimaStorage(TileEntityType type)
    {
        super(type);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        if (tag.hasKey(ANIMA_KEY))
        {
            anima = tag.getInteger(ANIMA_KEY);
        }
        else
        {
            anima = 0;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger(ANIMA_KEY, getCurrentAnima());
        return tag;
    }

    @Override
    public int getCurrentAnima()
    {
        return anima;
    }
}
