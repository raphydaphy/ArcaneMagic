package com.raphydaphy.arcanemagic.fluid;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;

public abstract class LiquifiedSoulFluid extends BaseFluid {

    @Override
    public Fluid getFlowing() {
        return ModRegistry.FLOWING_LIQUIFIED_SOUL;
    }

    @Override
    public Fluid getStill() {
        return ModRegistry.LIQUIFIED_SOUL;
    }

    @Override
    protected boolean isInfinite() {
        return false;
    }

    @Override
    protected void beforeBreakingBlock(IWorld var1, BlockPos var2, BlockState var3) {

    }

    @Override
    protected int method_15733(ViewableWorld var1) {
        return 4; // Copied from WaterFluid
    }

    @Override
    protected int getLevelDecreasePerBlock(ViewableWorld var1) {
        return 1; // Copied from WaterFluid
    }

    @Override
    protected BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getBucketItem() {
        return ModRegistry.LIQUIFIED_SOUL_BUCKET;
    }

    @Override
    protected boolean method_15777(FluidState var1, BlockView var2, BlockPos var3, Fluid var4, Direction var5) {
        return false;
    }

    @Override
    public boolean matchesType(Fluid other) {
        return other == ModRegistry.LIQUIFIED_SOUL || other == ModRegistry.FLOWING_LIQUIFIED_SOUL;
    }

    @Override
    public int getTickRate(ViewableWorld var1) {
        return 5; // Copied from WaterFluid
    }

    @Override
    protected float getBlastResistance() {
        return 100.0F; // Copied from WaterFluid
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return ModRegistry.LIQUIFIED_SOUL_BLOCK.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
    }

    public static class Flowing extends LiquifiedSoulFluid {
        @Override
        protected void appendProperties(StateFactory.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.with(LEVEL);
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState_1) {
            return false;
        }
    }

    public static class Still extends LiquifiedSoulFluid {
        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }
}
