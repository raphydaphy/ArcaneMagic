package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.PumpBlock;
import com.raphydaphy.arcanemagic.block.SmelterBlock;
import com.raphydaphy.arcanemagic.block.entity.base.DoubleFluidBlockEntity;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public class PumpBlockEntity extends DoubleFluidBlockEntity implements Tickable, FluidContainer
{
	private static final String WATER_KEY = "Water";
	private static final int MAX_FLUID = DropletValues.BUCKET * 4;

	private FluidInstance water = new FluidInstance(Fluids.WATER);
	public int ticks = 0;
	public int prevTicks = 0;

	public PumpBlockEntity()
	{
		super(ModRegistry.PUMP_TE, 0);
	}

	@Override
	public int getMaxCapacity()
	{
		return MAX_FLUID;
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		if (isBottom() || tag.containsKey(WATER_KEY))
		{
			if (tag.containsKey(WATER_KEY))
			{
				water = new FluidInstance((CompoundTag) tag.getTag(WATER_KEY));
			} else
			{
				water = new FluidInstance(Fluids.WATER);
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		if (isBottom() && !water.isEmpty())
		{
			CompoundTag waterTag = new CompoundTag();
			water.toTag(waterTag);
			tag.put(WATER_KEY, waterTag);
		}
		return tag;
	}

	@Override
	protected boolean canInsertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		return false;
	}

	@Override
	protected boolean canExtractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		return bottom && fluid == Fluids.WATER && this.water.getAmount() - amount >= 0;
	}

	@Override
	protected void insertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{

	}

	@Override
	protected void extractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && bottom && this.water.getFluid() == fluid && this.water.getAmount() - amount >= 0)
		{
			this.water.subtractAmount(amount);
			markDirty();
		}
	}

	@Override
	protected void setFluidImpl(boolean bottom, Direction fromSide, FluidInstance instance)
	{
		if (!world.isClient && bottom)
		{
			this.water = instance;
			markDirty();
		}
	}

	@Override
	protected FluidInstance[] getFluidsImpl(boolean bottom, Direction fromSide)
	{
		Direction facing = world.getBlockState(pos).get(SmelterBlock.FACING);
		return bottom && facing != fromSide && facing != fromSide.getOpposite() ? new FluidInstance[]{water} : new FluidInstance[]{};
	}

	@Override
	public void tick()
	{
		if (isBottom())
		{
			prevTicks = ticks;
			BlockState blockState = world.getBlockState(pos);
			FluidState fluidState = world.getFluidState(pos);
			if (blockState.get(PumpBlock.WATERLOGGED) && !fluidState.isEmpty() && fluidState.isStill() && fluidState.getFluid() == Fluids.WATER && water.getAmount() + DropletValues.BUCKET <= MAX_FLUID)
			{
				ticks++;
				if (!world.isClient && world.getTime() % 160 == 0)
				{
					world.setBlockState(pos, blockState.with(PumpBlock.WATERLOGGED, false));
					water.addAmount(DropletValues.BUCKET);
					markDirty();

					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCK, 1, 1);
					ArcaneMagicPacketHandler.sendToAllAround(new ClientBlockEntityUpdatePacket(toTag(new CompoundTag())),world, getPos(), 64);
				}
			}
		}
	}

	@Override
	public int[] getInvAvailableSlots(Direction var1)
	{
		return new int[]{};
	}

	@Override
	public boolean canInsertInvStack(int var1, ItemStack var2, @Nullable Direction var3)
	{
		return false;
	}

	@Override
	public boolean canExtractInvStack(int var1, ItemStack var2, Direction var3)
	{
		return false;
	}
}
