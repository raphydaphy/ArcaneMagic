package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.core.common.RecipeManagerMixin;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.init.ModSounds;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.crafting.CraftingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class DeconstructionStaffItem extends Item
{
	public DeconstructionStaffItem()
	{
		super(new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx)
	{
		BlockPos pos = ctx.getBlockPos();
		Block block = ctx.getWorld().getBlockState(pos).getBlock();
		if (block == Blocks.SOUL_SAND)
		{
			ctx.getWorld().playSound(ctx.getPlayer(), pos, ModSounds.DECONSTRUCT, SoundCategory.BLOCK, 1, 1);

			DataHolder dataPlayer = (DataHolder)ctx.getPlayer();
			if (!ctx.getWorld().isClient && dataPlayer != null)
			{
				if (!dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.DECONSTRUCTED_SOUL_SAND))
				{
					ItemEntity entity = new ItemEntity(ctx.getWorld(), pos.getX() + ArcaneMagic.RANDOM.nextFloat(), pos.getY() + ArcaneMagic.RANDOM.nextFloat(), pos.getZ() + ArcaneMagic.RANDOM.nextFloat(), new ItemStack(ModRegistry.RELIC));
					ctx.getWorld().spawnEntity(entity);

					//TODO: Relic notebook section
					dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.DECONSTRUCTED_SOUL_SAND, true);
					dataPlayer.markAdditionalDataDirty();
				}
			}
			ctx.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			return ActionResult.SUCCESS;
		}
		if (!(block instanceof BedBlock) && !(block instanceof DoorBlock))
		{
			Map<Identifier, Recipe<CraftingInventory>> craftingRecipes =  ((RecipeManagerMixin)ctx.getWorld().getRecipeManager()).getRecipes(RecipeType.CRAFTING);
			for (Map.Entry<Identifier, Recipe<CraftingInventory>> entry : craftingRecipes.entrySet())
			{
				Recipe<CraftingInventory> craftingRecipe = entry.getValue();
				if (craftingRecipe.getOutput().getItem() == block.getItem())
				{
					ctx.getWorld().playSound(ctx.getPlayer(), pos, ModSounds.DECONSTRUCT, SoundCategory.BLOCK, 1, 1);
					if (ctx.getWorld().isClient)
					{
						return ActionResult.SUCCESS;
					}

					Map<ItemProvider, Integer> items = new HashMap<>();
					for (Ingredient ingredient : craftingRecipe.getPreviewInputs())
					{
						ItemStack[] stacks = ingredient.getStackArray();
						if (stacks.length > 0)
						{
							ItemProvider provider = stacks[0].getItem();
							if (!items.containsKey(stacks[0].getItem()))
							{
								items.put(provider, 1);
							} else
							{
								items.put(provider, items.get(provider) + 1);
							}
						}
					}

					for (Map.Entry<ItemProvider, Integer> itemPair : items.entrySet())
					{
						ItemStack item = new ItemStack(itemPair.getKey(), itemPair.getValue());
						int output = craftingRecipe.getOutput().getAmount();
						if (output > 1)
						{
							if (item.getAmount() >= output * 2)
							{
								item.setAmount(item.getAmount() / output);
							} else
							{
								if (ArcaneMagic.RANDOM.nextInt(2) == 0)
								{
									item.setAmount(1);
								} else
								{
									item.setAmount(0);
								}
							}
						}

						if (item.getAmount() > 1)
						{
							item.setAmount((int)Math.ceil(item.getAmount() / 2f));
						}

						if (item.getAmount() > 0)
						{
							ItemEntity entity = new ItemEntity(ctx.getWorld(), pos.getX() + ArcaneMagic.RANDOM.nextFloat(), pos.getY() + ArcaneMagic.RANDOM.nextFloat(), pos.getZ() + ArcaneMagic.RANDOM.nextFloat(), item);
							ctx.getWorld().spawnEntity(entity);
						}
					}

					ctx.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public void onCrafted(ItemStack stack, World world, PlayerEntity player)
	{
		if (!world.isClient && player != null)
		{
			if (!((DataHolder) player).getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_DECONSTRUCTION_STAFF_KEY))
			{
				ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(false), (ServerPlayerEntity) player);
				((DataHolder) player).getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_DECONSTRUCTION_STAFF_KEY, true);
				// TODO: Deconstruction section
				//ArcaneMagicUtils.updateNotebookSection(world, (DataHolder)player, NotebookSectionRegistry.DISCOVERY.getID().toString(), false);
				((DataHolder) player).markAdditionalDataDirty();
			}
		}
	}
}
