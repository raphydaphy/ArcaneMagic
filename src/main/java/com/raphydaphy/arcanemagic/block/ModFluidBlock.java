package com.raphydaphy.arcanemagic.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.BaseFluid;

public class ModFluidBlock extends FluidBlock
{
	public ModFluidBlock(BaseFluid fluid, FabricBlockSettings settings)
	{
		super(fluid, settings.build());
	}
}
