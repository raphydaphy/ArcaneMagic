package com.raphydaphy.arcanemagic.util;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;

public class ArcaneMagicUtils
{
	/***
	 * Tries to use the specified amount of soul
	 * Automatically refills the scepter from any available pendant if successful
	 * @param user The player using the soul, if there is one
	 * @return True if the soul was consumed
	 ***/
	public static boolean useSoul(World world, ItemStack scepter, @Nullable PlayerEntity user, int amount)
	{
		if (!scepter.isEmpty() && scepter.getItem() instanceof ScepterItem)
		{
			ItemStack pendant = ItemStack.EMPTY;
			int pendantSlot = findPendant(user);
			if (pendantSlot != -1)
			{
				pendant = user.inventory.getInvStack(pendantSlot);
			}
			int pendantSoul = 0;
			int scepterSoul = scepter.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);

			if (!pendant.isEmpty())
			{
				pendantSoul = pendant.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);
			}

			if (amount <= ((ScepterItem) scepter.getItem()).maxSoul && amount <= scepterSoul + pendantSoul)
			{
				if (!world.isClient)
				{
					if (!pendant.isEmpty() && pendant.getTag() != null)
					{
						if (pendantSoul >= amount)
						{
							pendant.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, pendantSoul - amount);
						} else
						{
							amount -= pendantSoul;
							pendant.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, 0);
							Objects.requireNonNull(scepter.getTag()).putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul - amount);
						}
					} else
					{
						Objects.requireNonNull(scepter.getTag()).putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul - amount);
					}
				}
				return true;
			}
		}
		return false;
	}

	public static boolean addSoul(World world, ItemStack scepter, @Nullable PlayerEntity user, int amount)
	{
		if (!scepter.isEmpty() && scepter.getItem() instanceof ScepterItem)
		{
			ItemStack pendant = ItemStack.EMPTY;
			int pendantSlot = findPendant(user);
			if (pendantSlot != -1)
			{
				pendant = user.inventory.getInvStack(pendantSlot);
			}
			int scepterMax = ((ScepterItem) scepter.getItem()).maxSoul;
			int scepterSoul = scepter.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);

			if (amount > scepterMax)
			{
				amount = scepterMax;
			}

			if (!world.isClient && scepter.getTag() != null)
			{
				// All the new soul can fit into the scepter
				if (scepterMax - scepterSoul >= amount)
				{
					scepter.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul + amount);
					return true;
				} else
				{
					amount -= (scepterMax - scepterSoul);
					scepter.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, scepterMax);
				}

				if (amount > 0 && !pendant.isEmpty())
				{
					int pendantSoul = pendant.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);
					if (pendantSoul + amount > ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL)
					{
						amount = ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL - pendantSoul;
					}
					Objects.requireNonNull(pendant.getTag()).putInt(ArcaneMagicConstants.SOUL_KEY, pendantSoul + amount);
				}
			}
			return true;
		}
		return false;
	}

	public static int findPendant(PlayerEntity player)
	{
		if (player != null)
		{
			for (int searchSlot = 0; searchSlot < player.inventory.getInvSize(); searchSlot++)
			{
				ItemStack stackInSlot = player.inventory.getInvStack(searchSlot);
				if (!stackInSlot.isEmpty() && stackInSlot.getItem() == ModRegistry.SOUL_PENDANT)
				{
					return searchSlot;
				}
			}
		}
		return -1;
	}

	/**
	 * Tries to insert or extract an item from a BlockEntity
	 * Items are only extracted if the player is holding shift with an empty hand
	 *
	 * @param slot The container slot to try and interact with
	 * @return True if an item was either inserted or extracted
	 */
	public static boolean pedestalInteraction(World world, PlayerEntity player, BlockEntity container, Hand hand, int slot)
	{
		ItemStack held = player.getStackInHand(hand);
		ItemStack stackInTable = ((Inventory) container).getInvStack(slot);

		// Try to insert stack
		if (!player.isSneaking())
		{
			if (held.isEmpty())
			{
				return false;
			}
			if (stackInTable.isEmpty())
			{
				ItemStack insertStack = held.copy();

				if (held.getAmount() > 1)
				{
					if (!world.isClient)
					{
						if (!player.isCreative())
						{
							held.subtractAmount(1);
						}
						insertStack.setAmount(1);
					}
				} else if (!world.isClient && !player.isCreative())
				{
					player.setStackInHand(hand, ItemStack.EMPTY);
				}

				// insertStack = 1 item to insert
				// held = remaining items

				if (((Inventory)container).isValidInvStack(slot, insertStack))
				{

					if (!world.isClient)
					{
						((Inventory) container).setInvStack(slot, insertStack);
					}

					world.playSound(player, container.getPos(), SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCK, 1, 1);
					player.swingHand(hand);

					return true;
				}

				return false;
			}
		} else
		{
			if (!stackInTable.isEmpty())
			{
				if (!world.isClient)
				{
					if (!player.method_7270(stackInTable.copy())) // addItemStackToInventory
					{
						ItemEntity result = new ItemEntity(world, container.getPos().getX() + 0.5, container.getPos().getY() + 1, container.getPos().getZ() + 0.5, stackInTable.copy());
						result.setVelocity(0, 0, 0);
						world.spawnEntity(result);
					}
					((Inventory) container).setInvStack(slot, ItemStack.EMPTY);
				}

				world.playSound(player, container.getPos(), SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCK, 1, 1);
				player.swingHand(hand);

				return true;
			}
		}
		return false;
	}

	public static boolean handleTileEntityBroken(Block block, BlockState oldState, World world, BlockPos pos, BlockState newState)
	{
		if (oldState.getBlock() != newState.getBlock())
		{
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory)
			{
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
				world.updateHorizontalAdjacent(pos, block);
			}
			return true;
		}
		return false;
	}

	// Copied from net.minecraft.container.Container#calculateComparatorOutput
	public static int calculateComparatorOutput(World world, BlockPos pos)
	{
		BlockEntity entity = world.getBlockEntity(pos);

		if (entity instanceof Inventory)
		{
			Inventory inventory = (Inventory) entity;
			int count = 0;
			float percent = 0.0F;

			for (int slot = 0; slot < inventory.getInvSize(); ++slot)
			{
				ItemStack stack = inventory.getInvStack(slot);
				if (!stack.isEmpty())
				{
					percent += (float) stack.getAmount() / (float) Math.min(inventory.getInvMaxStackAmount(), stack.getMaxAmount());
					++count;
				}
			}

			percent /= (float) inventory.getInvSize();
			return MathHelper.floor(percent * 14.0F) + (count > 0 ? 1 : 0);
		}
		return 0;
	}

	public static float lerp(float a, float b, float t)
	{
		return (1 - t) * a + t * b;
	}

	public enum ForgeCrystal
	{
		EMERALD(), DIAMOND(), GOLD(), REDSTONE(), LAPIS(), COAL();

		public static final String HILT_KEY = "hilt_crystal";
		public static final String POMMEL_KEY = "pommel_crystal";

		public final String id;
		public final Identifier hilt;
		public final Identifier pommel;

		ForgeCrystal()
		{
			this.id = this.toString().toLowerCase();
			this.hilt = new Identifier(ArcaneMagic.DOMAIN, "textures/item/weapon_gems/" + id + "_hilt");
			this.pommel = new Identifier(ArcaneMagic.DOMAIN, "textures/item/weapon_gems/" + id + "_pommel");
		}

		@Nullable
		public static ForgeCrystal getFromID(String id)
		{
			for (ForgeCrystal crystal : values())
			{
				if (crystal.id.equals(id))
				{
					return crystal;
				}
			}
			return null;
		}

		@Override
		public String toString()
		{
			return id;
		}
	}
}
