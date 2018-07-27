package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAltar extends BlockWaterloggableBase implements ITileEntityProvider
{
    private static final VoxelShape shape;

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

    @Override
    public boolean onRightClick(IBlockState blockstate, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            return true;
        }
    }

    @Override
    public VoxelShape getShape(IBlockState p_getShape_1_, IBlockReader p_getShape_2_, BlockPos p_getShape_3_)
    {
        return shape;
    }

    @Override
    public void beforeReplacingBlock(final IBlockState state, final World world, final BlockPos pos, final IBlockState p_196243_4_, final boolean p_196243_5_)
    {
        if (state.getBlock() == p_196243_4_.getBlock())
        {
            return;
        }
        super.beforeReplacingBlock(state, world, pos, p_196243_4_, p_196243_5_);
        world.removeTileEntity(pos);
    }

    public BlockFaceShape getBlockFaceShape(IBlockReader reader, IBlockState blockState, BlockPos pos, EnumFacing facing)
    {
        return BlockFaceShape.UNDEFINED;
    }

    static
    {
        shape = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    }
}
