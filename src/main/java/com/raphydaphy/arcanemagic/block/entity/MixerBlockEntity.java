package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class MixerBlockEntity extends DoubleBlockEntity implements SidedInventory, Tickable, FluidContainer
{
	private static final String WATER_KEY = "Water";
	private static final String LIQUIFIED_SOUL_KEY = "LiquifiedSoul";
	private static final int MAX_FLUID = DropletValues.BUCKET * 4;

	private FluidInstance water = FluidInstance.EMPTY;
	private FluidInstance liquified_soul = FluidInstance.EMPTY;
	public long ticks = 0;

	private final int[] slots = { 0 };

	public MixerBlockEntity()
	{
		super(ModRegistry.MIXER_TE, 1);
	}

	@Override
	public void tick()
	{
		if (world.isClient)
		{
			ticks++;
		}
		if (!setBottom)
		{
			bottom = ArcaneMagicUtils.isBottomBlock(world, pos, ModRegistry.MIXER);
			setBottom = true;
		}
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		if (tag.containsKey(WATER_KEY))
		{
			water = new FluidInstance((CompoundTag)tag.getTag(WATER_KEY));
		} else
		{
			water = FluidInstance.EMPTY;
		}
		if (tag.containsKey(LIQUIFIED_SOUL_KEY))
		{
			liquified_soul = new FluidInstance((CompoundTag)tag.getTag(LIQUIFIED_SOUL_KEY));
		} else
		{
			liquified_soul = FluidInstance.EMPTY;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		if (!water.isEmpty())
		{
			CompoundTag waterTag = new CompoundTag();
			water.toTag(waterTag);
			tag.put(WATER_KEY, waterTag);
		}
		if (!liquified_soul.isEmpty())
		{
			CompoundTag liquifiedSoulTag = new CompoundTag();
			liquified_soul.toTag(liquifiedSoulTag);
			tag.put(LIQUIFIED_SOUL_KEY, liquifiedSoulTag);
		}
		return tag;
	}

	@Override
	public int getMaxCapacity()
	{
		return MAX_FLUID;
	}

	public boolean canInsertFluidBottom(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		return fluid == Fluids.WATER && !bottom && water.getAmount() + amount <= MAX_FLUID;
	}

	@Override
	public boolean canInsertFluid(Direction fromSide, Fluid fluid, int amount)
	{
		if (!bottom)
		{
			DoubleBlockEntity bottomBlockEntity = getBottom();
			if (bottomBlockEntity instanceof MixerBlockEntity)
			{
				return ((MixerBlockEntity) bottomBlockEntity).canInsertFluidBottom(bottom, fromSide, fluid, amount);
			}
		}
		return false;
	}

	@Override
	public boolean canExtractFluid(Direction fromSide, Fluid fluid, int amount)
	{
		return bottom && fluid == ModRegistry.LIQUIFIED_SOUL && this.liquified_soul.getAmount() + amount <= MAX_FLUID;
	}

	@Override
	public void insertFluid(Direction fromSide, Fluid fluid, int amount)
	{

	}

	@Override
	public void extractFluid(Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && this.liquified_soul.getFluid() == fluid)
		{
			this.liquified_soul.subtractAmount(amount);

			if (this.liquified_soul.getAmount() == 0)
			{
				this.liquified_soul = FluidInstance.EMPTY;
			}
			markDirty();
		}
	}

	@Override
	public void setFluid(Direction fromSide, FluidInstance instance)
	{
		if (!world.isClient)
		{
			this.liquified_soul = instance;
			markDirty();
		}
	}

	@Override
	public FluidInstance[] getFluids(Direction fromSide)
	{
		return new FluidInstance[]{liquified_soul};
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		ArcaneMagicPacketHandler.sendToAllAround(new ClientBlockEntityUpdatePacket(toInitialChunkDataTag()), world, getPos(), 300);
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket()
	{
		CompoundTag tag = super.toInitialChunkDataTag();
		writeContents(tag);
		return new BlockEntityUpdateS2CPacket(getPos(), -1, tag);
	}

	@Override
	public CompoundTag toInitialChunkDataTag()
	{
		CompoundTag tag = super.toInitialChunkDataTag();
		writeContents(tag);
		return tag;
	}

	@Override
	public int getInvMaxStackAmount()
	{
		return 1;
	}

	@Override
	public boolean isValidInvStackBottom(int slot, ItemStack item)
	{
		return getInvStack(slot).isEmpty() && !item.isEmpty() && item.getItem() == ModRegistry.SOUL_PENDANT;
	}

	@Override
	public int[] getInvAvailableSlots(Direction dir)
	{
		return dir == Direction.UP ? new int[0] : slots;
	}

	@Override
	public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir)
	{
		return isValidInvStack(slot, stack);
	}

	@Override
	public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir)
	{
		return true;
	}
}
