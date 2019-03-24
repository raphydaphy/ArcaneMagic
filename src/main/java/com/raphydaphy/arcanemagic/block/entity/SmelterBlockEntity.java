package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.SmelterBlock;
import com.raphydaphy.arcanemagic.block.entity.base.DoubleFluidBlockEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.init.ModSounds;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.crochet.network.PacketHandler;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.cooking.BlastingRecipe;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class SmelterBlockEntity extends DoubleFluidBlockEntity implements Tickable
{
	public static final int TOTAL_SMELTING_TIME = 150;
	private static final int MAX_FLUID = DropletValues.BLOCK;
	private static final String SMELT_TIME_KEY = "SmeltTime";
	private final int[] slots = {0, 1, 2};

	private FluidInstance liquified_soul = new FluidInstance(Fluids.EMPTY);
	private int smeltTime = 0;

	public SmelterBlockEntity()
	{
		super(ModRegistry.SMELTER_TE, 3);
	}

	@Override
	public void tick()
	{
		if (isBottom())
		{
			if (smeltTime > 0)
			{
				smeltTime++;

				if (world.isClient)
				{
					if (smeltTime < TOTAL_SMELTING_TIME - 4)
					{
						doParticles();
					}
				} else
				{
					if (world.getTime() % 10 == 0)
					{
						markDirty();
					} else
					{
						super.markDirty();
					}

					if (smeltTime == TOTAL_SMELTING_TIME - 4)
					{
						world.playSound(null, pos, ModSounds.SLIDE, SoundCategory.BLOCK, 0.5f, 1);
					}

					if (smeltTime >= TOTAL_SMELTING_TIME)
					{
						Optional<BlastingRecipe> optionalRecipe = this.world.getRecipeManager().get(RecipeType.BLASTING, new BasicInventory(getInvStack(0)), this.world);

						if (optionalRecipe.isPresent())
						{
							BlastingRecipe recipe = optionalRecipe.get();
							setInvStack(0, ItemStack.EMPTY);
							setInvStack(1, recipe.getOutput().copy());
							setInvStack(2, recipe.getOutput().copy());
						}

						smeltTime = 0;
						markDirty();
					}
				}
			} else if (!world.isClient && this.liquified_soul.getAmount() >= ArcaneMagicConstants.LIQUIFIED_SOUL_RATIO * ArcaneMagicConstants.SOUL_PER_SMELT && startSmelting(false))
			{
				this.liquified_soul.subtractAmount(ArcaneMagicConstants.LIQUIFIED_SOUL_RATIO * ArcaneMagicConstants.SOUL_PER_SMELT);
				if (liquified_soul.getAmount() <= 0)
				{
					this.liquified_soul.setFluid(Fluids.EMPTY);
				}
				markDirty();
			}
		}
	}

	@Environment(EnvType.CLIENT)
	private void doParticles()
	{
		Direction dir = world.getBlockState(pos).get(SmelterBlock.FACING);

		float offsetX = 0;
		float offsetZ = 0;

		if (dir == Direction.SOUTH)
		{
			offsetZ = -0.3f;
		} else if (dir == Direction.EAST || dir == Direction.WEST)
		{
			offsetZ -= 0.15f;
			offsetX -= 0.15f;
			if (dir == Direction.WEST)
			{
				offsetX += 0.3f;
			}
		}

		float inverseSpread = 400;
		for (int i = 0; i < 4; i++)
		{
			ParticleUtil.spawnSmokeParticle(world, pos.getX() + 0.4f + offsetX + ArcaneMagic.RANDOM.nextFloat() / 5f, pos.getY() + 1.55f, pos.getZ() + offsetZ + 0.55f + ArcaneMagic.RANDOM.nextFloat() / 5f,
					(float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread, 0.03f + ArcaneMagic.RANDOM.nextFloat() * 0 / 20f, (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread,
					1, 1, 1, 0.2f, 0.2f, 100);
		}
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		PacketHandler.sendToAllAround(new ClientBlockEntityUpdatePacket(toInitialChunkDataTag()), world, getPos(), 300);
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
	public void writeContents(CompoundTag tag)
	{
		super.writeContents(tag);
		if (isBottom())
		{
			tag.putInt(SMELT_TIME_KEY, smeltTime);
			liquified_soul.toTag(tag);
		}
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		if (tag.getBoolean(ArcaneMagicConstants.IS_BOTTOM_KEY))
		{
			smeltTime = tag.getInt(SMELT_TIME_KEY);
			liquified_soul = new FluidInstance(tag);
		}
	}

	public int getSmeltTime()
	{
		return smeltTime;
	}

	@Override
	public int getInvMaxStackAmount()
	{
		return 1;
	}

	@Override
	public boolean isValidInvStackBottom(int slot, ItemStack item)
	{
		if (slot == 0)
		{
			if (!getInvStack(1).isEmpty() || !getInvStack(2).isEmpty())
			{
				return false;
			}
		}
		return getInvStack(slot).isEmpty() && this.world.getRecipeManager().get(RecipeType.BLASTING, new BasicInventory(item), this.world).isPresent();
	}

	public boolean startSmelting(boolean simulate)
	{
		if (smeltTime <= 0 && !getInvStack(0).isEmpty() && getInvStack(1).isEmpty() && getInvStack(2).isEmpty())
		{
			Optional<BlastingRecipe> optionalRecipe = this.world.getRecipeManager().get(RecipeType.BLASTING, new BasicInventory(getInvStack(0)), this.world);
			if (optionalRecipe.isPresent())
			{
				if (!simulate)
				{
					world.playSound(null, pos, ModSounds.SLIDE, SoundCategory.BLOCK, 0.5f, 1);
					world.playSound(null, pos, ModSounds.BURN, SoundCategory.BLOCK, 0.5f, 1);
					if (!world.isClient)
					{
						smeltTime = 1;
						markDirty();
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int[] getInvAvailableSlots(Direction dir)
	{
		return dir == Direction.UP ? new int[0] : slots;
	}

	@Override
	public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir)
	{
		return isBottom() && getInvStack(1).isEmpty() && getInvStack(2).isEmpty() && slot == 0 && isValidInvStack(slot, stack);
	}

	@Override
	public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir)
	{
		return isBottom() && slot != 0;
	}

	@Override
	protected boolean canInsertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		return getFluidsImpl(bottom, fromSide).length > 0 && fluid == ModRegistry.LIQUIFIED_SOUL && this.liquified_soul.getAmount() + amount <= MAX_FLUID;
	}

	@Override
	protected boolean canExtractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		return false;
	}

	@Override
	protected void insertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{
		if (!world.isClient && canInsertFluidImpl(bottom, fromSide, fluid, amount))
		{
			if (this.liquified_soul.getFluid() == Fluids.EMPTY)
			{
				this.liquified_soul.setFluid(fluid);
			}
			this.liquified_soul.addAmount(amount);
		}
	}

	@Override
	protected void extractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount)
	{

	}

	@Override
	protected void setFluidImpl(boolean bottom, Direction fromSide, FluidInstance instance)
	{
		if (!world.isClient)
		{
			this.liquified_soul = instance;
		}
	}

	@Override
	protected FluidInstance[] getFluidsImpl(boolean bottom, Direction fromSide)
	{
		Direction facing = world.getBlockState(pos).get(SmelterBlock.FACING);
		return (bottom && (fromSide == facing.getOpposite() || fromSide == Direction.DOWN || fromSide == Direction.UP)) ? new FluidInstance[]{liquified_soul} : new FluidInstance[]{};
	}

	@Override
	public int getMaxCapacity()
	{
		return MAX_FLUID;
	}
}
