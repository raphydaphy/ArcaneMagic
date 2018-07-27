package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAltar extends Block implements ITileEntityProvider, IBucketPickupHandler, ILiquidContainer
{
    private static final BooleanProperty WATERLOGGED;

    public BlockAltar()
    {
        super(Block.Builder.create(Material.ROCK).hardnessAndResistance(5.0F, 1200.0F).soundType(SoundType.STONE));
        this.setDefaultState((this.blockState.getBaseState()).withProperty(WATERLOGGED, false));
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
    public IBlockState getBlockToPlaceOnUse(BlockItemUseContext useContext)
    {
        IFluidState fluidState = useContext.func_195991_k().getFluidState(useContext.func_195995_a());
        return this.getDefaultState().withProperty(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public boolean canContainFluid(IBlockReader p_canContainFluid_1_, BlockPos p_canContainFluid_2_, IBlockState p_canContainFluid_3_, Fluid p_canContainFluid_4_)
    {
        return !p_canContainFluid_3_.getValue(WATERLOGGED) && p_canContainFluid_4_ == Fluids.WATER;
    }

    @Override
    public boolean receiveFluid(IWorld world, BlockPos pos, IBlockState blockState, IFluidState p_receiveFluid_4_)
    {
        if (!blockState.getValue(WATERLOGGED) && p_receiveFluid_4_.getFluid() == Fluids.WATER)
        {
            if (!world.isRemote())
            {
                world.setBlockState(pos, blockState.withProperty(WATERLOGGED, true), 3);
                world.getPendingFluidTickList().add(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Fluid getFluid(IWorld world, BlockPos pos, IBlockState blockState)
    {
        if (blockState.getValue(WATERLOGGED))
        {
            world.setBlockState(pos, blockState.withProperty(WATERLOGGED, false), 3);
            return Fluids.WATER;
        } else {
            return Fluids.EMPTY;
        }
    }

    @Override
    public IFluidState getFluidState(IBlockState blockState)
    {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(blockState);
    }

    @Override
    public IBlockState func_196271_a(IBlockState blockState, EnumFacing facing, IBlockState otherBlockState, IWorld world, BlockPos pos, BlockPos otherPos) {
        if (blockState.getValue(WATERLOGGED)) {
            world.getPendingFluidTickList().add(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.func_196271_a(blockState, facing, otherBlockState, world, pos, otherPos);
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

    @Override
    protected void addPropertiesToBuilder(net.minecraft.state.StateContainer.Builder<Block, IBlockState> p_addPropertiesToBuilder_1_)
    {
        p_addPropertiesToBuilder_1_.addProperties(WATERLOGGED);
    }

    static
    {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}
