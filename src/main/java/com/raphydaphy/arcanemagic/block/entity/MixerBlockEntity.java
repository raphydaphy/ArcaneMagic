package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.entity.base.DoubleBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.base.DoubleFluidBlockEntity;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
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
import net.minecraft.util.InventoryUtil;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class MixerBlockEntity extends DoubleFluidBlockEntity implements SidedInventory, Tickable
{
	private static final String WATER_KEY = "Water";
	private static final String LIQUIFIED_SOUL_KEY = "LiquifiedSoul";
	private static final int WATER_USE = DropletValues.NUGGET;
	private static final int LIQUIFIED_SOUL_PRODUCTION = DropletValues.NUGGET + 5;
	private static final int MAX_FLUID = DropletValues.BUCKET * 4;

	private FluidInstance water = new FluidInstance(Fluids.WATER);
	private FluidInstance liquified_soul = new FluidInstance(ModRegistry.LIQUIFIED_SOUL);
	public long ticks = 0;

	private final int[] slots = { 0 };

	public MixerBlockEntity()
	{
		super(ModRegistry.MIXER_TE, 1);
	}

	@Override
	public void tick()
	{
		if (!setBottom)
		{
			bottom = ArcaneMagicUtils.isBottomBlock(world, pos, ModRegistry.MIXER);
			setBottom = true;
		}
		if (world.isClient)
		{
			ticks++;
		} else if (bottom && world.getTime() % 10 == 0 && (world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.add(0, 1, 0))))
		{
			ItemStack pendant = getInvStack(0);
			if (!pendant.isEmpty() && pendant.getItem() == ModRegistry.SOUL_PENDANT)
			{
				if (liquified_soul.getAmount() + LIQUIFIED_SOUL_PRODUCTION <= MAX_FLUID && water.getAmount() >= WATER_USE && pendant.getTag() != null )
				{
					int pendantSoul = pendant.getTag().getInt(ArcaneMagicConstants.SOUL_KEY);
					if (pendantSoul >= 1)
					{
						water.subtractAmount(WATER_USE);
						liquified_soul.addAmount(LIQUIFIED_SOUL_PRODUCTION);
						pendant.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, pendantSoul - 1);
						markDirty();
					}
				}
			}
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
			water = new FluidInstance(Fluids.WATER);
		}
		if (tag.containsKey(LIQUIFIED_SOUL_KEY))
		{
			liquified_soul = new FluidInstance((CompoundTag)tag.getTag(LIQUIFIED_SOUL_KEY));
		} else
		{
			liquified_soul = new FluidInstance(ModRegistry.LIQUIFIED_SOUL);
		}
	}

	@Override
	public void writeContents(CompoundTag tag)
	{
		if (bottom)
		{
			InventoryUtil.serialize(tag, contents);
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
		}
	}

	@Override
	public int getMaxCapacity()
	{
		return MAX_FLUID;
	}

	@Override
	protected boolean canInsertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		return !bottom && fluid == Fluids.WATER && this.water.getAmount() + amount <= MAX_FLUID;
	}

	@Override
	protected boolean canExtractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		return bottom && fluid == ModRegistry.LIQUIFIED_SOUL && this.liquified_soul.getAmount() - amount >= 0;
	}

	@Override
	protected void insertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && fluid == Fluids.WATER && this.water.getAmount() + amount <= MAX_FLUID)
		{
			this.water.addAmount(amount);

			if (this.water.getFluid() != fluid)
			{
				this.water.setFluid(fluid);
			}
			markDirty();
		}
	}

	@Override
	protected void extractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && this.liquified_soul.getFluid() == fluid && this.liquified_soul.getAmount() - amount >= 0)
		{
			this.liquified_soul.subtractAmount(amount);
			markDirty();
		}
	}

	@Override
	protected void setFluidImpl(boolean bottom, Direction fromSide, FluidInstance instance)
	{
		if (!world.isClient)
		{
			if (bottom)
			{
				this.liquified_soul = instance;
			} else
			{
				this.water = instance;
			}
			markDirty();
		}
	}

	@Override
	protected FluidInstance[] getFluidsImpl(boolean bottom, Direction fromSide)
	{
		if (bottom)
		{
			return new FluidInstance[] {liquified_soul};
		} else
		{
			return new FluidInstance[] {water};
		}
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
