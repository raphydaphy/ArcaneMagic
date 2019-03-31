package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.entity.base.InventoryBlockEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.crochet.network.PacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class AnalyzerBlockEntity extends InventoryBlockEntity implements SidedInventory, Tickable
{
	private final int[] slots = {0};
	public long ticks = 0;

	public AnalyzerBlockEntity()
	{
		super(ModRegistry.ANALYZER_TE, 1);
	}

	@Override
	public void tick()
	{
		if (world.isClient)
		{
			ticks++;

			ItemStack stack = getInvStack(0);
			doParticles(stack.getItem());
		}
	}

	@Environment(EnvType.CLIENT)
	private void doParticles(Item item)
	{
		if (item == Items.STICK || item instanceof ScepterItem || item == Blocks.CRAFTING_TABLE.getItem() || item == Blocks.OBSIDIAN.getItem()
		    || item instanceof SwordItem || item == Blocks.BLAST_FURNACE.getItem() || item == Blocks.ENCHANTING_TABLE.getItem()
		    || item == Blocks.DISPENSER.getItem() || item == Items.REDSTONE || item == Items.WATER_BUCKET
		    || item == Blocks.SOUL_SAND.getItem() || item == ModRegistry.RELIC)
		{
			float inverseSpread = 400;
			for (int i = 0; i < 4; i++)
			{
				ParticleUtil.spawnGlowParticle(world, pos.getX() + 0.4f + ArcaneMagic.RANDOM.nextFloat() / 5f, pos.getY() + 0.7f, pos.getZ() + 0.4f + ArcaneMagic.RANDOM.nextFloat() / 5f,
				                               (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread, -0.01f + ArcaneMagic.RANDOM.nextFloat() * 0 / 20f, (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread,
				                               ArcaneMagic.RANDOM.nextFloat() / 10f, ArcaneMagic.RANDOM.nextFloat() / 10f, item == ModRegistry.GOLDEN_SCEPTER ? 0 : ArcaneMagic.RANDOM.nextFloat() / 10f, 0.5f, true, 0.2f, 100);
			}
		}
		if (item instanceof ScepterItem)
		{
			float inverseRadius = 5f;
			float scale = 0.1f;
			float renderTicks = ticks * 2;

			float particlePosX = (float) (.5 + Math.cos((Math.PI / 180) * (renderTicks * 2)) / inverseRadius);
			float particlePosY = (float) (0.45 - Math.sin((Math.PI / 180) * (renderTicks * 4)) / 8);
			float particlePosZ = (float) (.5 + Math.sin((Math.PI / 180) * (renderTicks * 2)) / inverseRadius);
			ParticleUtil.spawnGlowParticle(world, pos.getX() + particlePosX, pos.getY() + particlePosY, pos.getZ() + particlePosZ,
			                               0, 0, 0, ArcaneMagic.RANDOM.nextFloat() / 5f, ArcaneMagic.RANDOM.nextFloat() / 5f, item == ModRegistry.GOLDEN_SCEPTER ? 0 : ArcaneMagic.RANDOM.nextFloat() / 5f, 0.6f, true, scale, 150);

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
		return true;
	}
}
