package com.raphydaphy.arcanemagic.block.entity.base;

import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.Direction;

public abstract class DoubleFluidBlockEntity extends DoubleBlockEntity implements FluidContainer {
    public DoubleFluidBlockEntity(BlockEntityType type, int size) {
        super(type, size);
    }

    @Override
    public boolean canInsertFluid(Direction fromSide, Fluid fluid, int amount) {
        if (isBottom()) {
            return canInsertFluidImpl(true, fromSide, fluid, amount);
        } else {
            DoubleBlockEntity bottomBlockEntity = getBottom();
            if (bottomBlockEntity instanceof DoubleFluidBlockEntity) {
                return ((DoubleFluidBlockEntity) bottomBlockEntity).canInsertFluidImpl(isBottom(), fromSide, fluid, amount);
            }
        }
        return false;
    }

    @Override
    public boolean canExtractFluid(Direction fromSide, Fluid fluid, int amount) {
        if (isBottom()) {
            return canExtractFluidImpl(true, fromSide, fluid, amount);
        } else {
            DoubleBlockEntity bottomBlockEntity = getBottom();
            if (bottomBlockEntity instanceof DoubleFluidBlockEntity) {
                return ((DoubleFluidBlockEntity) bottomBlockEntity).canExtractFluidImpl(isBottom(), fromSide, fluid, amount);
            }
        }
        return false;
    }

    @Override
    public void insertFluid(Direction fromSide, Fluid fluid, int amount) {
        if (isBottom()) {
            insertFluidImpl(true, fromSide, fluid, amount);
        } else {
            DoubleBlockEntity bottomBlockEntity = getBottom();
            if (bottomBlockEntity instanceof DoubleFluidBlockEntity) {
                ((DoubleFluidBlockEntity) bottomBlockEntity).insertFluidImpl(false, fromSide, fluid, amount);
            }
        }
    }

    @Override
    public void extractFluid(Direction fromSide, Fluid fluid, int amount) {
        if (isBottom()) {
            extractFluidImpl(true, fromSide, fluid, amount);
        } else {
            DoubleBlockEntity bottomBlockEntity = getBottom();
            if (bottomBlockEntity instanceof DoubleFluidBlockEntity) {
                ((DoubleFluidBlockEntity) bottomBlockEntity).extractFluidImpl(false, fromSide, fluid, amount);
            }
        }
    }

    @Override
    public void setFluid(Direction fromSide, FluidInstance instance) {
        if (isBottom()) {
            setFluidImpl(true, fromSide, instance);
        } else {
            DoubleBlockEntity bottomBlockEntity = getBottom();
            if (bottomBlockEntity instanceof DoubleFluidBlockEntity) {
                ((DoubleFluidBlockEntity) bottomBlockEntity).setFluidImpl(false, fromSide, instance);
            }
        }
    }

    @Override
    public FluidInstance[] getFluids(Direction fromSide) {
        if (isBottom()) {
            return getFluidsImpl(true, fromSide);
        } else {
            DoubleBlockEntity bottomBlockEntity = getBottom();
            if (bottomBlockEntity instanceof DoubleFluidBlockEntity) {
                return ((DoubleFluidBlockEntity) bottomBlockEntity).getFluidsImpl(false, fromSide);
            }
        }
        return new FluidInstance[]{};
    }

    /**
     * These are called on the bottom block entity when the matching method is called on either
     * This allows the block entity to keep track of everything in a single block
     * whilst having separate fluids in each half if desired
     */
    protected abstract boolean canInsertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount);

    protected abstract boolean canExtractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount);

    protected abstract void insertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount);

    protected abstract void extractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount);

    protected abstract void setFluidImpl(boolean bottom, Direction fromSide, FluidInstance instance);

    protected abstract FluidInstance[] getFluidsImpl(boolean bottom, Direction fromSide);
}
