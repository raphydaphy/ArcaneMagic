package com.raphydaphy.arcanemagic.block.base;

import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.multiblockapi.MultiBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class DoubleBlockBase extends OrientableBlockBase implements MultiBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF;

    static {
        HALF = Properties.DOUBLE_BLOCK_HALF;
    }

    protected DoubleBlockBase(Settings settings) {
        super(settings);
        this.setDefaultState((this.getDefaultState()).with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public void onBlockRemoved(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean boolean_1) {
        if (ArcaneMagicUtils.handleTileEntityBroken(this, oldState, world, pos, newState)) {
            super.onBlockRemoved(oldState, world, pos, newState, boolean_1);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState otherState, IWorld world, BlockPos pos, BlockPos otherPos) {
        DoubleBlockHalf otherHalf = state.get(HALF);
        if (dir.getAxis() == Direction.Axis.Y && otherHalf == DoubleBlockHalf.LOWER == (dir == Direction.UP)) {
            return otherState.getBlock() == this && otherState.get(HALF) != otherHalf ? state.with(FACING, otherState.get(FACING)) : Blocks.AIR.getDefaultState();
        } else {
            return otherHalf == DoubleBlockHalf.LOWER && dir == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, dir, otherState, world, pos, otherPos);
        }
    }

    @Override
    public void afterBreak(World world_1, PlayerEntity playerEntity_1, BlockPos blockPos_1, BlockState blockState_1, BlockEntity blockEntity_1, ItemStack itemStack_1) {
        super.afterBreak(world_1, playerEntity_1, blockPos_1, Blocks.AIR.getDefaultState(), blockEntity_1, itemStack_1);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity breaker) {
        DoubleBlockHalf half = state.get(HALF);
        BlockPos otherPartPos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherPartState = world.getBlockState(otherPartPos);
        if (otherPartState.getBlock() == this && otherPartState.get(HALF) != half) {
            FluidState otherFluidState = world.getFluidState(otherPartPos);
            if (!otherFluidState.isEmpty()) {
                world.setBlockState(otherPartPos, otherFluidState.getBlockState(), 35);
            } else {
                world.setBlockState(otherPartPos, Blocks.AIR.getDefaultState(), 35);
            }
            world.playLevelEvent(breaker, 2001, otherPartPos, Block.getRawIdFromState(otherPartState));
            ItemStack itemStack_1 = breaker.getMainHandStack();
            if (!world.isClient && !breaker.isCreative()) {
                Block.dropStacks(state, world, pos, null, breaker, itemStack_1);
                Block.dropStacks(otherPartState, world, otherPartPos, null, breaker, itemStack_1);
            }
        }
        super.onBreak(world, pos, state, breaker);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos above = ctx.getBlockPos();
        if (above.getY() < 255 && ctx.getWorld().getBlockState(above.up()).canReplace(ctx)) {
            FluidState fluid = ctx.getWorld().getFluidState(ctx.getBlockPos());
            return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(HALF, DoubleBlockHalf.LOWER).with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
        } else {
            return null;
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        FluidState fluidAbove = world.getFluidState(pos.up());
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER).with(WATERLOGGED, fluidAbove.getFluid() == Fluids.WATER), 3);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState blockState_1) {
        return PistonBehavior.DESTROY;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public long getRenderingSeed(BlockState blockState_1, BlockPos blockPos_1) {
        return MathHelper.hashCode(blockPos_1.getX(), blockPos_1.down(blockState_1.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), blockPos_1.getZ());
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> map) {
        super.appendProperties(map);
        map.add(HALF);
    }

    @Override
    public List<BlockPos> getOtherParts(BlockState blockState, BlockPos blockPos) {
        return blockState.getBlock() == this ? (blockState.get(HALF) == DoubleBlockHalf.LOWER ? Collections.singletonList(blockPos.up()) : Collections.singletonList(blockPos.down())) : Collections.emptyList();
    }
}
