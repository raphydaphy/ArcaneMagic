package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.SmelterBlock;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.cooking.BlastingRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.InventoryUtil;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.Optional;

public class SmelterBlockEntity extends InventoryBlockEntity implements SidedInventory, Tickable
{
	public long ticks = 0;
	private final int[] slots = {0, 1, 2};
	public boolean bottom;
	private boolean setBottom = false;

	public SmelterBlockEntity()
	{
		super(ModRegistry.SMELTER_TE, 3);
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
			BlockState state = world.getBlockState(pos);
			if (state.getBlock() == ModRegistry.SMELTER)
			{
				bottom = state.get(SmelterBlock.HALF) == DoubleBlockHalf.LOWER;
			} else
			{
				bottom = false;
			}

			setBottom = true;
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
	public void writeContents(CompoundTag tag)
	{
		if (bottom)
		{
			InventoryUtil.serialize(tag, contents);
		}
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		if (bottom)
		{
			contents = DefaultedList.create(getInvSize(), ItemStack.EMPTY);
			InventoryUtil.deserialize(tag, contents);
		}
	}

	@Override
	public int getInvMaxStackAmount()
	{
		return 1;
	}

	@Override
	public boolean isValidInvStack(int slot, ItemStack item)
	{
		if (bottom)
		{
			if (slot == 9)
			{
				if (!getInvStack(1).isEmpty() || !getInvStack(2).isEmpty())
				{
					return false;
				}
			}
			return getInvStack(slot).isEmpty() && this.world.getRecipeManager().get(RecipeType.BLASTING, new BasicInventory(item), this.world).isPresent();
		} else
		{
			SmelterBlockEntity bottomBlockEntity = getBottom();
			if (bottomBlockEntity != null)
			{
				return bottomBlockEntity.isValidInvStack(slot, item);
			}
		}
		return false;
	}

	public boolean startSmelting(boolean simulate)
	{
		if (!getInvStack(0).isEmpty() && getInvStack(1).isEmpty() && getInvStack(2).isEmpty())
		{
			Optional<BlastingRecipe> optionalRecipe = this.world.getRecipeManager().get(RecipeType.BLASTING, new BasicInventory(getInvStack(0)), this.world);
			if (optionalRecipe.isPresent())
			{
				if (!simulate)
				{
					BlastingRecipe recipe = optionalRecipe.get();
					if (!world.isClient)
					{
						setInvStack(0, ItemStack.EMPTY);
						setInvStack(1, recipe.getOutput().copy());
						setInvStack(2, recipe.getOutput().copy());
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
		return bottom && getInvStack(1).isEmpty() && getInvStack(2).isEmpty() && slot == 0 && isValidInvStack(slot, stack);
	}

	@Override
	public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir)
	{
		return bottom && slot != 0;
	}

	private SmelterBlockEntity getBottom()
	{
		if (bottom)
		{
			return this;
		} else
		{
			BlockEntity below = world.getBlockEntity(pos.add(0, -1, 0));
			if (below instanceof SmelterBlockEntity)
			{
				return (SmelterBlockEntity) below;
			}
		}
		return null;
	}

	@Override
	public boolean isInvEmpty()
	{
		if (bottom)
		{
			return super.isInvEmpty();
		} else
		{
			SmelterBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.isInvEmpty();
			}
		}
		return true;
	}

	@Override
	public ItemStack getInvStack(int slot)
	{
		if (bottom)
		{
			return super.getInvStack(slot);
		} else
		{
			SmelterBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.getInvStack(slot);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack takeInvStack(int slot, int count)
	{
		if (bottom)
		{
			return super.takeInvStack(slot, count);
		} else
		{
			SmelterBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.takeInvStack(slot, count);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeInvStack(int slot)
	{
		if (bottom)
		{
			return super.removeInvStack(slot);
		} else
		{
			SmelterBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				return blockEntityBottom.removeInvStack(slot);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInvStack(int slot, ItemStack stack)
	{
		if (bottom)
		{
			super.setInvStack(slot, stack);
		} else
		{
			SmelterBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				blockEntityBottom.setInvStack(slot, stack);
			}
		}
	}

	@Override
	public void clear()
	{
		if (bottom)
		{
			super.clear();
		} else
		{
			SmelterBlockEntity blockEntityBottom = getBottom();
			if (blockEntityBottom != null)
			{
				blockEntityBottom.clear();
			}
		}
	}
}
