package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.PipeBlock;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PipeBlockEntity extends BlockEntity implements FluidContainer, Tickable
{
	private static final int MAX_FLUID = DropletValues.NUGGET;
	private static final int TRANSFER_SPEED = DropletValues.NUGGET;
	private static final int MAX_COOLDOWN = 5;

	private static final String FROM_KEY = "From";
	private static final String PULL_COOLDOWN_KEY = "PullCooldown";
	private static final String PUSH_COOLDOWN_KEY = "PushCooldown";

	private static final List<Direction> SIDES = new ArrayList<>(Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));

	private FluidInstance fluid = new FluidInstance(Fluids.EMPTY);
	private Direction from = Direction.NORTH;
	private int pullCooldown = 0;
	private int pushCooldown = 0;

	public PipeBlockEntity()
	{
		super(ModRegistry.PIPE_TE);
	}

	public Direction getFrom()
	{
		return from;
	}

	@Override
	public void tick()
	{
		if (!world.isClient)
		{
			if (pushCooldown <= 0 && !this.fluid.isEmpty() && this.fluid.getAmount() >= TRANSFER_SPEED)
			{
				moveFluid(Direction.DOWN, from.getOpposite(), this::putFluid);
			}

			// Pipes will only try to pull fluids if they have a redstone signal
			if (world.isReceivingRedstonePower(pos))
			{
				if (pullCooldown <= 0 && this.fluid.getAmount() + TRANSFER_SPEED <= MAX_FLUID)
				{
					moveFluid(Direction.UP, from, this::pullFluid);
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
	 * Fluid will always try to travel in a preferred direction (down when pushing, up when pulling) first
	 * Second priority is to continue traveling in the direction the fluid traveled last tick (calculated with lastDirection)
	 * If fluid can't travel up or forwards, it will pick randomly between left and right
	 * If fluid cannot travel in any other new direction, it goes up
	 * Fluid will never travel backwards, and if the previous direction is disconnected it will find a new one
	 */
	private void moveFluid(Direction preference, Direction previous, Function<Direction, Boolean> check)
	{
		BlockState state = world.getBlockState(pos);

		Direction backwards = previous.getOpposite();

		// If we can go the preferred direction without going backwards, do it
		if (backwards != preference && isConnected(state, preference) && check.apply(preference)) return;

		List<Direction> sideOrder = new ArrayList<>(SIDES);
		Collections.shuffle(sideOrder, ArcaneMagic.RANDOM);

		// The direction which the fluids should flow for in this tick
		Direction path = previous;

		// If the forward direction has no connection, we need to pick a new forward direction
		if (!isConnected(state, previous))
		{
			for (Direction dir : sideOrder)
			{
				// Make sure we are not changing the direction to go backwards
				if (dir != backwards && isConnected(state, dir))
				{
					// Select a new direction if it is suitable
					path = dir;
					break;
				}
			}
		}

		// This will only be false now if the only way to go is the opposite of the preferred way
		if (isConnected(state, path))
		{
			// Try to travel in selected 'forward' direction
			if (check.apply(path))
			{
				return;
			}

			// These directions should be to the left and right of the ideal path
			Direction[] leftRight = new Direction[] { Direction.EAST, Direction.WEST };

			// If they are not, correct it
			if (path == Direction.EAST || path == Direction.WEST)
			{
				leftRight = new Direction[] { Direction.NORTH, Direction.SOUTH };
			}

			// Chose either left or right at random
			boolean left = ArcaneMagic.RANDOM.nextBoolean();

			boolean leftValid = leftRight[0] != backwards && isConnected(leftRight[0]);
			boolean rightValid = leftRight[1] != backwards && isConnected(leftRight[1]);

			// Try to push liquid to the left if that direction was chosen
			if (left && leftValid && check.apply(leftRight[0]))
			{
				return;
			}

			// If that failed or left was not chosen, try to push liquid right
			if (!left && rightValid && check.apply(leftRight[1]))
			{
				return;
			}

			// Try both sides again in case the randomly chosen side did not work
			if (leftValid && check.apply(leftRight[0])) return;
			if (rightValid && check.apply(leftRight[1])) return;
		}

		// If everything else fails, we try to travel in the opposite direction of the preference if it is not backwards
		if (preference.getOpposite() != backwards && isConnected(preference.getOpposite()))
		{
			check.apply(preference.getOpposite());
		}
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
		from = Direction.byId(tag.getInt(FROM_KEY));
		pullCooldown = tag.getInt(PULL_COOLDOWN_KEY);
		pushCooldown = tag.getInt(PUSH_COOLDOWN_KEY);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		fluid.toTag(tag);
		tag.putInt(FROM_KEY, from.getId());
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
			this.from = fromSide;
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
			this.from = fromSide.getOpposite();
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
		return isConnected(world.getBlockState(pos), side);
	}

	private boolean isConnected(BlockState state, Direction side)
	{
		return side == null || state.get(PipeBlock.getProp(side));
	}

	@Override
	public FluidInstance[] getFluids(Direction fromSide)
	{
		return isConnected(fromSide) ? new FluidInstance[]{fluid} : new FluidInstance[]{};
	}
}
