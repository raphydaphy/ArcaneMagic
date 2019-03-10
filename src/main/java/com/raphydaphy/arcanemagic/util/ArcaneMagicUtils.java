package com.raphydaphy.arcanemagic.util;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.block.base.DoubleBlockBase;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.command.TagCommand;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ArcaneMagicUtils
{
	/***
	 * Tries to use the specified amount of soul
	 * Automatically refills the scepter from any available pendant if successful
	 * @param user The player using the soul, if there is one
	 * @return True if the soul was consumed
	 ***/
	public static boolean useSoul(World world, ItemStack scepter, PlayerEntity user, int amount)
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
			CompoundTag scepterTag = scepter.getTag();

			if (!pendant.isEmpty())
			{
				pendantSoul = pendant.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);
			}

			if (scepterTag != null && amount <= ((ScepterItem) scepter.getItem()).maxSoul && amount <= scepterSoul + pendantSoul)
			{
				if (!world.isClient)
				{
					CompoundTag pendantTag;
					if (!pendant.isEmpty() && (pendantTag = pendant.getTag()) != null)
					{
						if (pendantSoul >= amount)
						{
							pendantTag.putInt(ArcaneMagicConstants.SOUL_KEY, pendantSoul - amount);
						} else
						{
							amount -= pendantSoul;
							pendantTag.putInt(ArcaneMagicConstants.SOUL_KEY, 0);
							scepterTag.putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul - amount);
						}
					} else
					{
						scepterTag.putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul - amount);
					}
				}
				return true;
			}
		}
		return false;
	}

	public static boolean addSoul(World world, ItemStack scepter, PlayerEntity user, int amount)
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

			CompoundTag scepterTag;
			if (!world.isClient && (scepterTag = scepter.getTag()) != null)
			{
				// All the new soul can fit into the scepter
				if (scepterMax - scepterSoul >= amount)
				{
					scepterTag.putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul + amount);
					return true;
				} else
				{
					amount -= (scepterMax - scepterSoul);
					scepterTag.putInt(ArcaneMagicConstants.SOUL_KEY, scepterMax);
				}

				if (amount > 0 && !pendant.isEmpty())
				{
					int pendantSoul = pendant.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);
					CompoundTag pendantTag = pendant.getTag();
					if (pendantTag != null)
					{
						if (pendantSoul + amount > ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL)
						{
							amount = ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL - pendantSoul;
						}
						pendantTag.putInt(ArcaneMagicConstants.SOUL_KEY, pendantSoul + amount);
					}
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

	public static boolean pedestalInteraction(World world, PlayerEntity player, BlockEntity container, Hand hand, int slot)
	{
		return pedestalInteraction(world, player, container, hand, slot, null);
	}

	/**
	 * Tries to insert or extract an item from a BlockEntity
	 * Items are only extracted if the player is holding shift with an empty hand
	 *
	 * @param slot The container slot to try and interact with
	 * @param onSet This is called if a new stack is added with the ItemStack parameter being the added stack
	 * @return True if an item was either inserted or extracted
	 */
	public static boolean pedestalInteraction(World world, PlayerEntity player, BlockEntity container, Hand hand, int slot, Consumer<ItemStack> onSet)
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

				if (((Inventory) container).isValidInvStack(slot, insertStack))
				{

					if (!world.isClient)
					{
						if (onSet != null)
						{
							onSet.accept(insertStack.copy());
						}
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

	public static Vec3d interpPlayerLook(PlayerEntity player, float partialTicks)
	{
		if (partialTicks == 1.0F)
		{
			return getVectorForRotation(player.pitch, player.headYaw);
		} else
		{
			float f = player.prevPitch + (player.pitch - player.prevPitch) * partialTicks;
			float f1 = player.prevHeadYaw + (player.headYaw - player.prevHeadYaw) * partialTicks;
			return getVectorForRotation(f, f1);
		}
	}

	private static Vec3d getVectorForRotation(float pitch, float yaw)
	{
		float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3d((double) (f1 * f2), (double) f3, (double) (f * f2));
	}

	/**
	 * Used by children of DoubleBlockBase to check if their BlockEntity is on the bottom or top
	 */
	public static boolean isBottomBlock(World world, BlockPos pos, Block match)
	{
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == match)
		{
			return state.get(DoubleBlockBase.HALF) == DoubleBlockHalf.LOWER;
		} else
		{
			return false;
		}
	}

	/**
	 * Used in JSON recipe serializers
	 */
	public static ItemStack deserializeItemStack(JsonObject object)
	{
		String id = JsonHelper.getString(object, "item");
		Item item = Registry.ITEM.getOrEmpty(new Identifier(id)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + id + "'"));
		if (object.has("data"))
		{
			throw new JsonParseException("Disallowed data tag found");
		} else
		{
			int int_1 = JsonHelper.getInt(object, "count", 1);
			return new ItemStack(item, int_1);
		}
	}

	public static void updateNotebookSection(IWorld world, DataHolder dataPlayer, String section, boolean remove)
	{
		if (!world.isClient())
		{
			CompoundTag updates = dataPlayer.getAdditionalData().getCompound(ArcaneMagicConstants.NOTEBOOK_UPDATES_KET);
			if (remove)
			{
				if (updates.containsKey(section))
				{
					updates.remove(section);
				}
			} else
			{
				updates.putBoolean(section, true);
			}
			dataPlayer.getAdditionalData().put(ArcaneMagicConstants.NOTEBOOK_UPDATES_KET, updates);;
			dataPlayer.markAdditionalDataDirty();
		}
	}

	public enum ForgeCrystal
	{
		EMERALD("emerald", 0x08a346), DIAMOND("diamond", 0x30dbbd), GOLD("gold", 0xf1ca0e), REDSTONE("redstone", 0xda2c0b), LAPIS("lapis", 0x0a38cb), COAL("coal", 0x282229);

		public final String id;
		public final Identifier hilt;
		public final Identifier pommel;
		public final float red;
		public final float green;
		public final float blue;

		ForgeCrystal(String id, int rgb)
		{
			this.id = id;
			this.hilt = new Identifier(ArcaneMagic.DOMAIN, "textures/item/weapon_gems/" + id + "_hilt");
			this.pommel = new Identifier(ArcaneMagic.DOMAIN, "textures/item/weapon_gems/" + id + "_pommel");
			this.red = ((rgb>>16)&0xFF) / 255f;
			this.green = ((rgb>>8)&0xFF) / 255f;
			this.blue = ((rgb)&0xFF) / 255f;
		}

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
