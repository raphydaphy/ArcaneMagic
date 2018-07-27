package com.raphydaphy.arcanemagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class BlockAltar extends Block
{
    public BlockAltar()
    {
        super(Block.Builder.create(Material.WOOD));
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
