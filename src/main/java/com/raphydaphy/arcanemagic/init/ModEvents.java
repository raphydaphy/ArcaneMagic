package com.raphydaphy.arcanemagic.init;

import java.lang.reflect.Field;

import com.raphydaphy.arcanemagic.client.gui.GuiScepter;
import com.raphydaphy.arcanemagic.item.ItemScepter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModEvents
{
	public static boolean hasRenderedParticles = false;
	static EntityPlayer clientPlayer = null;
	
	protected static Field Field_ItemRenderer_equippedProgressMainhand = ReflectionHelper.findField(ItemRenderer.class, "equippedProgressMainHand", "field_187469_f");
    protected static Field Field_ItemRenderer_equippedProgressOffhand = ReflectionHelper.findField(ItemRenderer.class, "equippedProgressOffHand", "field_187471_h");
    protected static Field Field_ItemRenderer_prevEquippedProgressMainhand = ReflectionHelper.findField(ItemRenderer.class, "prevEquippedProgressMainHand", "field_187470_g");
    protected static Field Field_ItemRenderer_prevEquippedProgressOffhand = ReflectionHelper.findField(ItemRenderer.class, "prevEquippedProgressOffHand", "field_187472_i");
    
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == ElementType.ALL)
		{
			EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;

			if ((!player.getHeldItemMainhand().isEmpty()
					&& player.getHeldItemMainhand().getItem().equals(ModRegistry.SCEPTER))
					|| !player.getHeldItemOffhand().isEmpty()
							&& player.getHeldItemOffhand().getItem().equals(ModRegistry.SCEPTER))
			{
				GuiScepter.renderWandHUD(mc, event.getResolution());
			}
		}
	}

	// Disabled hand swinging animation - use once up to wand mechanics
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderHand(RenderHandEvent event)
	{
		/*
		float t = 1.0f;

		EntityPlayer ply = Minecraft.getMinecraft().player;
		ItemStack stack = ply.getActiveItemStack();

		if (!stack.isEmpty() && (stack.getItem() instanceof ItemScepter))
		{
			EnumHand hand = ply.getActiveHand();

			ItemRenderer itemrenderer = Minecraft.getMinecraft().getItemRenderer();
			try
			{
				if (hand == EnumHand.MAIN_HAND)
				{
					if (Field_ItemRenderer_equippedProgressMainhand.getFloat(itemrenderer) < t)
					{
						Field_ItemRenderer_equippedProgressMainhand.setFloat(itemrenderer, t);
						Field_ItemRenderer_prevEquippedProgressMainhand.setFloat(itemrenderer, t);
					}
				} else
				{
					if (Field_ItemRenderer_equippedProgressOffhand.getFloat(itemrenderer) < t)
					{
						Field_ItemRenderer_equippedProgressOffhand.setFloat(itemrenderer, t);
						Field_ItemRenderer_prevEquippedProgressOffhand.setFloat(itemrenderer, t);
					}
				}
			} catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}

		}*/
	}
}
