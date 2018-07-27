package com.raphydaphy.arcanemagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockAltar extends Block implements ITileEntityProvider
{
    public BlockAltar()
    {
        super(Block.Builder.create(Material.ROCK).hardnessAndResistance(5.0F, 1200.0F).soundType(SoundType.STONE));
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(IBlockReader iBlockReader)
    {
        return new TileEntityAltar();
    }
}

class TileEntityAltar extends TileEntity implements ITickable
{
    TileEntityAltar()
    {
        super(TileEntityType.registerTileEntityType("arcanemagic:altar", TileEntityType.Builder.create(TileEntityAltar::new)));
    }

    @Override
    public void update()
    {
        System.out.println("Altar!");
    }
}
