package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.DoubleBlockBase;
import com.raphydaphy.arcanemagic.block.MixerBlock;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class MixerBlockEntity extends DoubleBlockEntity implements SidedInventory, Tickable
{
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
