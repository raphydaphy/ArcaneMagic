package com.raphydaphy.arcanemagic.init;

import java.util.Random;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.item.ItemScepter;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModEvents
{
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
	@SideOnly(Side.CLIENT)
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == ElementType.ALL)
		{
			EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;

			if ((!player.getHeldItemMainhand().isEmpty()
					&& player.getHeldItemMainhand().getItem().equals(ModRegistry.SCEPTER)))
			{
				ItemScepter.renderHUD(mc, event.getResolution(), player.getHeldItemMainhand());
			}

			else if (!player.getHeldItemOffhand().isEmpty()
					&& player.getHeldItemOffhand().getItem().equals(ModRegistry.SCEPTER))
			{
				ItemScepter.renderHUD(mc, event.getResolution(), player.getHeldItemOffhand());
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event)
	{
		if (event.getEntity() != null && event.getEntity() instanceof EntityWitch)
		{
			if (event.getEntity().world.rand.nextInt(15) == 1)
			{
				event.getDrops().add(new EntityItemFancy(event.getEntity().world, event.getEntity().posX,
						event.getEntity().posY, event.getEntity().posZ, new ItemStack(ModRegistry.ANCIENT_PARCHMENT)));
			}
		}
	}
}
