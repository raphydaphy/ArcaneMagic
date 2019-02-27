package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.entity.base.DoubleBlockEntity;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.cooking.BlastingRecipe;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class SmelterBlockEntity extends DoubleBlockEntity implements Tickable
{
	public long ticks = 0;
	private final int[] slots = {0, 1, 2};

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
			bottom = ArcaneMagicUtils.isBottomBlock(world, pos, ModRegistry.SMELTER);
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
	public int getInvMaxStackAmount()
	{
		return 1;
	}

	@Override
	public boolean isValidInvStackBottom(int slot, ItemStack item)
	{
		if (slot == 9)
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
}
