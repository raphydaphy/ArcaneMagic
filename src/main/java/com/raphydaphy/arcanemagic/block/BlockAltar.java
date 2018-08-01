package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockAltar extends BlockWaterloggableBase implements ITileEntityProvider
{
    private static final VoxelShape shape;

    public BlockAltar(Builder builder)
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
        VoxelShape bottom = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
        VoxelShape middle = Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D);
        VoxelShape top = Block.makeCuboidShape(0.0D, 6.0D, 0.0D, 16.0D, 12.0D, 16.0D);
        shape = ShapeUtils.func_197872_a(ShapeUtils.func_197872_a(bottom, middle), top);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(IBlockReader iBlockReader)
    {
        return new TileEntityAltar();
    }
}
