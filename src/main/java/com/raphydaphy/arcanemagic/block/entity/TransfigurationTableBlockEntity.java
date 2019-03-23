package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.entity.base.InventoryBlockEntity;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.cutsceneapi.network.PacketHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class TransfigurationTableBlockEntity extends InventoryBlockEntity implements SidedInventory
{
	private final int[] slots;

	public TransfigurationTableBlockEntity()
	{
		super(ModRegistry.TRANSFIGURATION_TABLE_TE, 9);
		slots = new int[getInvSize()];

		for (int i = 0; i < getInvSize(); i++)
		{
			slots[i] = i;
		}
	}

	public void clearRecipe(boolean keepPlaceholders)
	{
		if (!world.isClient)
		{
			int placeholders = 0;
			for (int slot = 0; slot < 9; slot++)
			{
				if (!getInvStack(slot).isEmpty())
				{
					if (getInvStack(slot).getItem() == Blocks.GLASS_PANE.getItem())
					{
						placeholders++;
						if (keepPlaceholders)
						{
							continue;
						}
					}
					setInvStack(slot, ItemStack.EMPTY);
				}
			}
			if (placeholders > 0 && !keepPlaceholders)
			{
				ItemEntity placeholderItems = new ItemEntity(world, getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, new ItemStack(Blocks.GLASS_PANE, placeholders));
				placeholderItems.setVelocity(0, 0, 0);
				world.spawnEntity(placeholderItems);
			}
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
	public int getInvMaxStackAmount()
	{
		return 1;
	}

	@Override
	public boolean isValidInvStack(int slot, ItemStack item)
	{
		return getInvStack(slot).isEmpty();
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
		return false;
	}
}
