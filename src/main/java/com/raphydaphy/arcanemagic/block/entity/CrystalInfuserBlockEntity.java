package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.CrystalItem;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class CrystalInfuserBlockEntity extends InventoryBlockEntity implements SidedInventory, Tickable
{
	public long ticks = 0;

	private final int[] slots = { 0, 1, 2 };

	public CrystalInfuserBlockEntity()
	{
		super(ModRegistry.CRYSTAL_INFUSER_TE, 3);
	}

	@Override
	public void tick()
	{
		if (world.isClient)
		{
			ticks++;
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

	public int getSlotForItem(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return -1;
		} else if (stack.getItem() instanceof ICrystalEquipment)
		{
			return 0;
		} else if (stack.getItem() == Items.REDSTONE || stack.getItem() == Items.LAPIS_LAZULI)
		{
			return 1;
		} else if (stack.getItem() instanceof CrystalItem)
		{
			return 2;
		}
		return -1;
	}

	@Override
	public int getInvMaxStackAmount()
	{
		return 1;
	}

	@Override
	public boolean isValidInvStack(int slot, ItemStack item)
	{
		if (!getInvStack(slot).isEmpty())
		{
			return false;
		}
		return getSlotForItem(item) == slot;
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
