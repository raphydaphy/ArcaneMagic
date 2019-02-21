package com.raphydaphy.empowered.block.entity;

import com.raphydaphy.empowered.Empowered;
import com.raphydaphy.empowered.init.ModRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;

public class TransfigurationTableBlockEntity extends BlockEntity implements Tickable
{
	public TransfigurationTableBlockEntity()
	{
		super(ModRegistry.TRANSFIGURATION_TABLE_TE);
	}

	@Override
	public void tick()
	{
		Empowered.getLogger().info("i live at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
	}
}
