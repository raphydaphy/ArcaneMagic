package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.PumpBlock;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class PumpBlockEntity extends BlockEntity implements Tickable, FluidContainer
{
	private static final String WATER_KEY = "Water";
	private static final int MAX_FLUID = DropletValues.BUCKET * 4;

	private FluidInstance water = new FluidInstance(Fluids.WATER);

	public PumpBlockEntity()
	{
		super(ModRegistry.PUMP_TE);
	}

	@Override
	public int getMaxCapacity()
	{
		return MAX_FLUID;
	}

	@Override
	public boolean canInsertFluid(Direction direction, Fluid fluid, int amount)
	{
		return false;
	}

	@Override
	public boolean canExtractFluid(Direction direction, Fluid fluid, int amount)
	{
		return fluid == ModRegistry.LIQUIFIED_SOUL && this.water.getAmount() - amount >= 0;
	}

	@Override
	public void insertFluid(Direction direction, Fluid fluid, int amount)
	{

	}

	@Override
	public void extractFluid(Direction direction, Fluid fluid, int amount)
	{
		if (!world.isClient && this.water.getFluid() == fluid && this.water.getAmount() - amount >= 0)
		{
			this.water.subtractAmount(amount);
			markDirty();
		}
	}

	@Override
	public void setFluid(Direction direction, FluidInstance fluidInstance)
	{
		if(!world.isClient)
		{
			this.water = fluidInstance;
			markDirty();
		}
	}

	@Override
	public FluidInstance[] getFluids(Direction direction)
	{
		return new FluidInstance[] { water };
	}

	@Override
	public void tick()
	{
		if (!world.isClient && world.getTime() % 160 == 0)
		{
			BlockState blockState = world.getBlockState(pos);
			FluidState fluidState = world.getFluidState(pos);
			if (blockState.get(PumpBlock.WATERLOGGED) && !fluidState.isEmpty() && fluidState.isStill() && fluidState.getFluid() == Fluids.WATER && water.getAmount() + DropletValues.BUCKET <= MAX_FLUID)
			{
				world.setBlockState(pos, blockState.with(PumpBlock.WATERLOGGED, false));
				water.addAmount(DropletValues.BUCKET);
				markDirty();

				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCK, 1, 1);
			}
		}
	}
}
