package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.HashMap;

public class PipeBlockEntity extends BlockEntity implements FluidContainer
{
	private static final int MAX_FLUID = DropletValues.NUGGET;

	public PipeBlockEntity()
	{
		super(ModRegistry.PIPE_TE);
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		return tag;
	}

	@Override
	public int getMaxCapacity()
	{
		return MAX_FLUID;
	}

	@Override
	public boolean canInsertFluid(Direction fromSide, Fluid fluid, int amount)
	{
		return false;
	}

	@Override
	public boolean canExtractFluid(Direction fromSide, Fluid fluid, int amount)
	{
		return false;
	}

	@Override
	public void insertFluid(Direction fromSide, Fluid fluid, int amount)
	{

	}

	@Override
	public void extractFluid(Direction fromSide, Fluid fluid, int amount)
	{

	}

	@Override
	public void setFluid(Direction fromSide, FluidInstance instance)
	{

	}

	@Override
	public FluidInstance[] getFluids(Direction fromSide)
	{
		return new FluidInstance[0];
	}
}
