package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class PumpBlockEntity extends BlockEntity implements FluidContainer
{
	private static final int MAX_FLUID = DropletValues.BUCKET * 4;
	private FluidInstance fluid = FluidInstance.EMPTY;

	public PumpBlockEntity()
	{
		super(ModRegistry.PUMP_TE);
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		if (tag.containsKey(FluidInstance.FLUID_KEY))
		{
			fluid = new FluidInstance(tag);
		} else
		{
			fluid = FluidInstance.EMPTY;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		if (!fluid.isEmpty())
		{
			fluid.toTag(tag);
		}
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
		if (this.fluid.isEmpty() || this.fluid.getFluid() == fluid)
		{
			return this.fluid.getAmount() + amount <= MAX_FLUID;
		}
		return false;
	}

	@Override
	public void insertFluid(Direction fromSide, Fluid fluid, int amount)
	{

	}

	@Override
	public void extractFluid(Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && this.fluid.getFluid() == fluid)
		{
			this.fluid.subtractAmount(amount);

			if (this.fluid.getAmount() == 0)
			{
				this.fluid = FluidInstance.EMPTY;
			}
			markDirty();
		}
	}

	@Override
	public void setFluid(Direction fromSide, FluidInstance instance)
	{
		if (!world.isClient)
		{
			this.fluid = instance;
			markDirty();
		}
	}

	@Override
	public FluidInstance[] getFluids(Direction fromSide)
	{
		return new FluidInstance[]{fluid};
	}
}
