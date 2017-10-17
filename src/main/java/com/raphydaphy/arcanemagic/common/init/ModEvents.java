package com.raphydaphy.arcanemagic.common.init;

import java.util.Random;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookToast;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class ModEvents
{

	@SubscribeEvent
	public static void onAttachCapability(AttachCapabilitiesEvent<Entity> ev)
	{
		if (ev.getObject() instanceof EntityPlayer)
		{
			ev.addCapability(new ResourceLocation(ArcaneMagic.MODID, "notebook_storage"), new NotebookInfo());
		}
	}

	@SubscribeEvent
	public static void onItemCrafted(ItemCraftedEvent ev)
	{

	}

	@SubscribeEvent
	public static void onItemPickup(ItemPickupEvent ev)
	{
		if (ev.pickedUp instanceof EntityItemFancy)
		{
			if (ev.pickedUp.world.getBlockState(ev.pickedUp.getPosition()).getBlock() == ModRegistry.FANCY_LIGHT)
			{
				ev.pickedUp.world.setBlockToAir(ev.pickedUp.getPosition());
			}
		}
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent ev)
	{
		if (ev.player.world.getTotalWorldTime() % 50 == 0 && !ev.player.world.isRemote)
		{
			NotebookInfo info = ev.player.getCapability(NotebookInfo.CAP, null);

			if (info != null)
			{
				for (int i = 0; i < ev.player.inventory.getSizeInventory(); i++)
				{
					Item item = ev.player.inventory.getStackInSlot(i).getItem();

					// Check for all item-based notebook category unlocks here

					if (item.equals(ModRegistry.ANCIENT_PARCHMENT)
							&& !info.isUnlocked(NotebookCategories.ANCIENT_RELICS.getRequiredTag()))
					{
						info.setUnlocked(NotebookCategories.ANCIENT_RELICS.getRequiredTag());

						ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToast(NotebookCategories.ANCIENT_RELICS), (EntityPlayerMP)ev.player);
					}

					if (item.equals(ModRegistry.NOTEBOOK)
							&& !info.isUnlocked(NotebookCategories.FORGOTTEN_KNOWLEDGE.getRequiredTag()))
					{
						info.setUnlocked(NotebookCategories.FORGOTTEN_KNOWLEDGE.getRequiredTag());

						ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToast(NotebookCategories.FORGOTTEN_KNOWLEDGE), (EntityPlayerMP)ev.player);
					}

					if (item.equals(Item.getItemFromBlock(ModRegistry.ANALYZER))
							&& !info.isUnlocked(NotebookCategories.ANALYATION.getRequiredTag()))
					{
						info.setUnlocked(NotebookCategories.ANALYATION.getRequiredTag());

						ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToast(NotebookCategories.ANALYATION), (EntityPlayerMP)ev.player);
					}

					if (item.equals(Item.getItemFromBlock(ModRegistry.ESSENCE_CONCENTRATOR))
							&& !info.isUnlocked(NotebookCategories.ESSENCE_COLLECTION.getRequiredTag()))
					{
						info.setUnlocked(NotebookCategories.ESSENCE_COLLECTION.getRequiredTag());

						ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToast(NotebookCategories.ESSENCE_COLLECTION), (EntityPlayerMP)ev.player);
					}

					if (item.equals(Item.getItemFromBlock(ModRegistry.CRYSTALLIZER))
							&& !info.isUnlocked(NotebookCategories.CRYSTALLIZATION.getRequiredTag()))
					{
						info.setUnlocked(NotebookCategories.CRYSTALLIZATION.getRequiredTag());

						ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToast(NotebookCategories.CRYSTALLIZATION), (EntityPlayerMP)ev.player);
					}

					if (item.equals(Item.getItemFromBlock(ModRegistry.ELEMENTAL_CRAFTING_TABLE))
							&& !info.isUnlocked(NotebookCategories.MANIPULATING_MAGIC.getRequiredTag()))
					{
						info.setUnlocked(NotebookCategories.MANIPULATING_MAGIC.getRequiredTag());

						ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToast(NotebookCategories.MANIPULATING_MAGIC), (EntityPlayerMP)ev.player);
					}
				}
			}

		}

		Random rand = ev.player.world.rand;
		if (ev.phase == TickEvent.Phase.START)
		{
			if (ev.player.getHeldItemMainhand().getItem() == ModRegistry.ANCIENT_PARCHMENT
					|| ev.player.getHeldItemOffhand().getItem() == ModRegistry.ANCIENT_PARCHMENT)
			{
				World world = ev.player.world;
				if (world.isRemote)
				{
					for (int x = (int) ev.player.posX - 10; x < (int) ev.player.posX + 10; x++)
					{
						for (int y = (int) ev.player.posY - 5; y < (int) ev.player.posY + 5; y++)
						{
							for (int z = (int) ev.player.posZ - 10; z < (int) ev.player.posZ + 10; z++)
							{
								if (rand.nextInt(600) == 1)
								{
									BlockPos here = new BlockPos(x, y, z);
									if (world.getBlockState(here).getBlock().equals(Blocks.BEDROCK))
									{
										// client side only, these particles are just for looks!
										ArcaneMagic.proxy.spawnEssenceParticles(world,
												new Vec3d(x + 0.5, y + 0.5, z + 0.5), new Vec3d(0, 0, 0),
												Essence.getFromBiome(world.getBiome(here)),
												ev.player.getPositionVector().addVector(0, 1, 0), true);

									}
								}
							}
						}
					}
				}

			}

			if (ev.player.isHandActive() && !ev.player.activeItemStack.isEmpty())
			{
				ItemStack held = ev.player.getHeldItem(ev.player.getActiveHand());
				if (ev.player.activeItemStack.getItem() == ModRegistry.SCEPTER && held.getItem() == ModRegistry.SCEPTER)
				{
					if (ev.player.activeItemStack != held)
					{// TODO please check proper (anything that won't change)
						ev.player.activeItemStack = held;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event)
	{
		if (event.getEntity() != null && event.getEntity() instanceof EntityWitch)
		{
			//NotebookInfo info = ev.getEntityPlayer().getCapability(NotebookInfo.CAP, null);
			if (event.getEntity().world.rand.nextInt(2) == 1)
			{
				event.getDrops().add(new EntityItemFancy(event.getEntity().world, event.getEntity().posX,
						event.getEntity().posY, event.getEntity().posZ, new ItemStack(ModRegistry.ANCIENT_PARCHMENT)));
			}
		}
	}
}
