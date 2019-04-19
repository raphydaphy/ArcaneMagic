package com.raphydaphy.arcanemagic.block.base;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.BaseFluid;

public class FluidBlockBase extends FluidBlock {
    public FluidBlockBase(BaseFluid fluid, FabricBlockSettings settings) {
        super(fluid, settings.build());
    }
}
