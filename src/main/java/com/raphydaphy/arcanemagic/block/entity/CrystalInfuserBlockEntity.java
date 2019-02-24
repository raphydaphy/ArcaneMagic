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
	private static final String ACTIVE_KEY = "active_crafting";
	private static final String CRAFTING_TIME_KEY = "crafting_time";
	private static final int[] slots = { 0, 1, 2 };

	// Updated client-side for rendering
	public long ticksExisted = 0;
	// Updated on both sides and synced every second while crafting
	private long craftingTime = 0;

	private boolean active = false;

	public CrystalInfuserBlockEntity()
	{
		super(ModRegistry.CRYSTAL_INFUSER_TE, 3);
	}

	@Override
	public void tick()
	{
		if (world.isClient)
		{
			ticksExisted++;
		} else if (world.getTime() % 20 == 0 && active)
		{
			markDirty();
		}
		if (active)
		{
			craftingTime++;
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
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		active = tag.getBoolean(ACTIVE_KEY);
		craftingTime = tag.getLong(CRAFTING_TIME_KEY);
	}

	@Override
	public void writeContents(CompoundTag tag)
	{
		super.writeContents(tag);
		tag.putBoolean(ACTIVE_KEY, active);
		tag.putLong(CRAFTING_TIME_KEY, craftingTime);
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

	public void setActive(boolean active)
	{
		if (!world.isClient)
		{
			this.active = active;
			markDirty();
		}
	}

	public void resetCraftingTime()
	{
		if (!world.isClient)
		{
			craftingTime = 0;
			markDirty();
		}
	}

	public boolean isActive()
	{
		return active;
	}

	public long getCraftingTime()
	{
		return craftingTime;
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