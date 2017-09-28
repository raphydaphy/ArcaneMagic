package com.raphydaphy.arcanemagic.init;

import java.lang.reflect.Field;
import java.util.Random;

import com.raphydaphy.arcanemagic.client.particle.ParticleEssence;
import com.raphydaphy.arcanemagic.item.ItemScepter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModEvents
{
	public static boolean hasRenderedParticles = false;
	static EntityPlayer clientPlayer = null;

	protected static Field Field_ItemRenderer_equippedProgressMainhand = ReflectionHelper.findField(ItemRenderer.class,
			"equippedProgressMainHand", "field_187469_f");
	protected static Field Field_ItemRenderer_equippedProgressOffhand = ReflectionHelper.findField(ItemRenderer.class,
			"equippedProgressOffHand", "field_187471_h");
	protected static Field Field_ItemRenderer_prevEquippedProgressMainhand = ReflectionHelper
			.findField(ItemRenderer.class, "prevEquippedProgressMainHand", "field_187470_g");
	protected static Field Field_ItemRenderer_prevEquippedProgressOffhand = ReflectionHelper
			.findField(ItemRenderer.class, "prevEquippedProgressOffHand", "field_187472_i");

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
									Minecraft.getMinecraft().effectRenderer
											.addEffect(new ParticleEssence(ev.player.getEntityWorld(), x + 0.5, y + 0.5,
													z + 0.5, 0, 0, 0, 0xFFFFFF, ev.player.getPositionVector().addVector(0,1,0)));
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

	// Disabled hand swinging animation - use once up to wand mechanics
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderHand(RenderHandEvent event)
	{
		/* float t = 1.0f;
		 * 
		 * EntityPlayer ply = Minecraft.getMinecraft().player; ItemStack stack =
		 * ply.getActiveItemStack();
		 * 
		 * if (!stack.isEmpty() && (stack.getItem() instanceof ItemScepter)) { EnumHand
		 * hand = ply.getActiveHand();
		 * 
		 * ItemRenderer itemrenderer = Minecraft.getMinecraft().getItemRenderer(); try {
		 * if (hand == EnumHand.MAIN_HAND) { if
		 * (Field_ItemRenderer_equippedProgressMainhand.getFloat(itemrenderer) < t) {
		 * Field_ItemRenderer_equippedProgressMainhand.setFloat(itemrenderer, t);
		 * Field_ItemRenderer_prevEquippedProgressMainhand.setFloat(itemrenderer, t); }
		 * } else { if
		 * (Field_ItemRenderer_equippedProgressOffhand.getFloat(itemrenderer) < t) {
		 * Field_ItemRenderer_equippedProgressOffhand.setFloat(itemrenderer, t);
		 * Field_ItemRenderer_prevEquippedProgressOffhand.setFloat(itemrenderer, t); } }
		 * } catch (IllegalArgumentException e) { e.printStackTrace(); } catch
		 * (IllegalAccessException e) { e.printStackTrace(); }
		 * 
		 * } */
	}
}
