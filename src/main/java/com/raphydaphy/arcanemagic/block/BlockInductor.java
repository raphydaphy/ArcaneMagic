package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockInductor extends BlockWaterloggableBase implements ITileEntityProvider
{
    private static final VoxelShape shape;

    public BlockInductor(Builder builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(IBlockState p_getShape_1_, IBlockReader p_getShape_2_, BlockPos p_getShape_3_)
    {
        return shape;
    }

    static
    {
        shape = Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    }

    @Override
    public void beforeReplacingBlock(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean p_196243_5_)
    {
        ArcaneMagicUtils.beforeReplacingTileEntity(state, world, pos, newState);
        super.beforeReplacingBlock(state, world, pos, newState, p_196243_5_);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(IBlockReader iBlockReader)
    {
        return new TileEntityAltar();
    }
}
