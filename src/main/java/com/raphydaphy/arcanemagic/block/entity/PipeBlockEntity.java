package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.PipeBlock;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.function.Function;

public class PipeBlockEntity extends BlockEntity implements FluidContainer, Tickable
{
	private static final int MAX_FLUID = DropletValues.NUGGET;
	private static final int TRANSFER_SPEED = DropletValues.NUGGET;
	private static final int MAX_COOLDOWN = 5;

	private static final String LAST_DIRECTION_KEY = "LastDirection";
	private static final String PULL_COOLDOWN_KEY = "PullCooldown";
	private static final String PUSH_COOLDOWN_KEY = "PushCooldown";

	private static final Direction[] SIDES = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

	private FluidInstance fluid = new FluidInstance(Fluids.EMPTY);
	private Direction lastDirection = Direction.NORTH;
	private int pullCooldown = 0;
	private int pushCooldown = 0;

	public PipeBlockEntity()
	{
		super(ModRegistry.PIPE_TE);
	}


	@Override
	public void tick()
	{
		if (!world.isClient)
		{
			if (pushCooldown <= 0 && !this.fluid.isEmpty() && this.fluid.getAmount() >= TRANSFER_SPEED)
			{
				moveFluid(Direction.DOWN, lastDirection.getOpposite(), this::putFluid);
			}

			// Pipes will only try to pull fluids if they have a redstone signal
			if (world.isReceivingRedstonePower(pos))
			{
				if (pullCooldown <= 0 && this.fluid.getAmount() + TRANSFER_SPEED <= MAX_FLUID)
				{
					moveFluid(Direction.UP, lastDirection, this::pullFluid);
				}
			}

			boolean changed = false;
			if (pullCooldown > 0)
			{
				pullCooldown--;
				changed = true;
			}
			if (pushCooldown > 0)
			{
				pushCooldown--;
				changed = true;
			}
			if (changed) markDirty();
		}
	}

	/**
	 * Try to move fluid from or into an adjacent FluidContainer
	 * Fluid will always try to travel in a prefered direction (down when pushing, up when pulling) first
	 * Second priority is to continue traveling in the direction the fluid traveled last tick (calculated with lastDirection)
	 * If fluid can't travel up or forwards, it will pick randomly between left and right
	 * If fluid cannot travel in any other new direction, it goes up
	 * Fluid will never travel backwards, and if the previous direction is disconnected it will find a new one
	 */
	private void moveFluid(Direction preference, Direction previous, Function<Direction, Boolean> check)
	{
		if (isConnected(preference) && check.apply(preference)) return;

		Direction opposite = previous.getOpposite();

		if (!isConnected(previous))
		{
			for (Direction dir : SIDES)
			{
				if (dir != opposite && isConnected(dir))
				{
					previous = dir;
					break;
				}
			}
		}

		if (previous != preference && isConnected(previous) && check.apply(previous)) return;
		if (previous == Direction.NORTH || previous == Direction.SOUTH)
		{
			boolean east = isConnected(Direction.EAST);
			boolean west = isConnected(Direction.WEST);

			if (east && west)
			{
				if (ArcaneMagic.RANDOM.nextBoolean() && check.apply(Direction.EAST))
				{
					return;
				} else if (check.apply(Direction.WEST))
				{
					return;
				}
			} else if (east && check.apply(Direction.EAST))
			{
				return;
			} else if (west && check.apply(Direction.WEST))
			{
				return;
			}
		} else if (previous == Direction.EAST || previous == Direction.WEST)
		{
			boolean north = isConnected(Direction.NORTH);
			boolean south = isConnected(Direction.SOUTH);

			if (north && south)
			{
				if (ArcaneMagic.RANDOM.nextBoolean() && check.apply(Direction.NORTH)) return;
				else if (check.apply(Direction.SOUTH)) return;
			}
			else if (north && check.apply(Direction.NORTH)) return;
			else if (south && check.apply(Direction.SOUTH)) return;
		}
		if (isConnected(preference.getOpposite())) check.apply(preference.getOpposite());
	}

	/**
	 * Tries to put fluid into an adjacent FluidContainer
	 * Assumes that TRANSFER_SPEED fluid is available to extract from this pipe
	 *
	 * @param toSide The direction that the container is located in
	 * @return True if the fluid could be moved
	 */
	private boolean putFluid(Direction toSide)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos.add(toSide.getVector()));
		if (blockEntity instanceof FluidContainer)
		{
			if (((FluidContainer) blockEntity).tryInsertFluid(toSide.getOpposite(), this.fluid.getFluid(), TRANSFER_SPEED, ActionType.PERFORM))
			{
				extractFluid(toSide, this.fluid.getFluid(), this.fluid.getAmount());
				return true;
			}
		}
		return false;
	}

	/**
	 * Tries to pull fluid from an adjacent FluidContainer that isn't a pipe
	 * Assumes that at least TRANSFER_SPEED fluid can fit in the pipe
	 *
	 * @param fromSide The side that the FluidContainer is located on
	 * @return True if the fluid is extracted
	 */
	private boolean pullFluid(Direction fromSide)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos.add(fromSide.getVector()));
		// Make sure that we aren't pulling fluid from another pipe, because that would double the speed
		if (blockEntity instanceof FluidContainer && !(blockEntity instanceof PipeBlockEntity))
		{
			for (FluidInstance instance : ((FluidContainer) blockEntity).getFluids(fromSide.getOpposite()))
			{
				// Make sure the fluid we are trying to extract is not empty and that it can go into this pipe
				if (!instance.isEmpty() && (this.fluid.isEmpty() || instance.getFluid() == this.fluid.getFluid()))
				{
					if (((FluidContainer) blockEntity).tryExtractFluid(fromSide.getOpposite(), instance.getFluid(), TRANSFER_SPEED, ActionType.PERFORM))
					{
						insertFluid(fromSide, instance.getFluid(), TRANSFER_SPEED);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		fluid = new FluidInstance(tag);
		lastDirection = Direction.byId(tag.getInt(LAST_DIRECTION_KEY));
		pullCooldown = tag.getInt(PULL_COOLDOWN_KEY);
		pushCooldown = tag.getInt(PUSH_COOLDOWN_KEY);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		fluid.toTag(tag);
		tag.putInt(LAST_DIRECTION_KEY, lastDirection.getId());
		tag.putInt(PULL_COOLDOWN_KEY, pullCooldown);
		tag.putInt(PUSH_COOLDOWN_KEY, pushCooldown);
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
		return isConnected(fromSide) && (this.fluid.isEmpty() || this.fluid.getFluid() == fluid) && this.fluid.getAmount() + amount <= MAX_FLUID;
	}

	@Override
	public boolean canExtractFluid(Direction fromSide, Fluid fluid, int amount)
	{
		return isConnected(fromSide) && this.fluid.getFluid() == fluid && this.fluid.getAmount() >= amount;
	}

	@Override
	public void insertFluid(Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && canInsertFluid(fromSide, fluid, amount))
		{
			if (this.fluid.isEmpty())
			{
				this.fluid = new FluidInstance(fluid, amount);
			} else
			{
				this.fluid.addAmount(amount);
			}
			this.lastDirection = fromSide;
			this.pullCooldown = MAX_COOLDOWN;

			markDirty();
		}
	}

	@Override
	public void extractFluid(Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && canExtractFluid(fromSide, fluid, amount))
		{
			this.fluid.subtractAmount(amount);
			if (this.fluid.getAmount() <= 0)
			{
				this.fluid.setFluid(Fluids.EMPTY);
			}
			this.pushCooldown = MAX_COOLDOWN;
			this.lastDirection = fromSide.getOpposite();
			this.markDirty();
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

	private boolean isConnected(Direction side)
	{
		return world.getBlockState(pos).get(PipeBlock.getProp(side));
	}

	@Override
	public FluidInstance[] getFluids(Direction fromSide)
	{
		return isConnected(fromSide) ? new FluidInstance[]{fluid} : new FluidInstance[]{};
	}
}
