package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityAltar extends TileEntity implements ITickable
{
    public TileEntityAltar()
    {
        super(ArcaneMagic.ALTAR_TE);
    }

    @Override
    public void update()
    {

    }
}