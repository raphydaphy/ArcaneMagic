package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.CrystalItem;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.sun.istack.internal.Nullable;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class CrystalInfuserBlockEntity extends InventoryBlockEntity implements SidedInventory, Tickable
{
	private static final String STAGE_KEY = "crafting_stage";
	private static final String CRAFTING_TIME_KEY = "crafting_time";
	private static final int[] slots = { 0, 1, 2 };

	// Updated client-side for rendering
	public long ticksExisted = 0;
	// Updated on both sides and synced every second while crafting
	private long craftingTime = 0;

	private CraftingStage stage = CraftingStage.IDLE;

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
		} else
		{
			if (craftingTime >= 4500 && stage == CraftingStage.INFUSING)
			{
				craftingTime = 0;
				stage = CraftingStage.IDLE;
				ItemEntity result = new ItemEntity(world, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, getInvStack(1));
				result.setVelocity(0, 0, 0);
				world.spawnEntity(result);
				clear();
				markDirty();
			} else if (world.getTime() % 20 == 0 && stage != CraftingStage.IDLE)
			{
				markDirty();
			}
		}
		if (stage != CraftingStage.IDLE)
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
		stage = CraftingStage.getFromID(tag.getInt(STAGE_KEY));
		if (stage == null)
		{
			stage = CraftingStage.IDLE;
		}
		craftingTime = tag.getLong(CRAFTING_TIME_KEY);
	}

	@Override
	public void writeContents(CompoundTag tag)
	{
		super.writeContents(tag);
		tag.putInt(STAGE_KEY, stage.id);
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

	public void setStage(CraftingStage stage)
	{
		if (!world.isClient)
		{
			this.stage = stage;
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

	public CraftingStage getStage()
	{
		return stage;
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

	public enum CraftingStage
	{
		IDLE(0), INFUSING(1), FINISHING(2);

		public final int id;

		CraftingStage(int id)
		{
			this.id = id;
		}

		@Nullable
		public static CraftingStage getFromID(int id)
		{
			for (CraftingStage stage : CraftingStage.values())
			{
				if (stage.id == id)
				{
					return stage;
				}
			}
			return null;
		}
	}
}
