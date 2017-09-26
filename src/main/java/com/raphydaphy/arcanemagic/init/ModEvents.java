package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.gui.GuiWand;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModEvents
{
	public static boolean hasRenderedParticles = false;
	static EntityPlayer clientPlayer = null;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == ElementType.ALL)
		{
			EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;

			if ((!player.getHeldItemMainhand().isEmpty()
					&& player.getHeldItemMainhand().getItem().equals(ModRegistry.WAND))
					|| !player.getHeldItemOffhand().isEmpty()
							&& player.getHeldItemOffhand().getItem().equals(ModRegistry.WAND))
			{
				GuiWand.renderWandHUD(mc, event.getResolution());
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
		 * EntityPlayer ply = ClientProxy.get().getPlayerClient(); ItemStack stack =
		 * ply.getActiveItemStack();
		 * 
		 * if(!stack.isEmpty() && (stack.getItem() instanceof GenericGunCharge ||
		 * stack.getItem() instanceof GenericGrenade)) { EnumHand hand =
		 * ply.getActiveHand();
		 * 
		 * ItemRenderer itemrenderer = Minecraft.getMinecraft().getItemRenderer(); try {
		 * if(hand==EnumHand.MAIN_HAND) {
		 * if(Field_ItemRenderer_equippedProgressMainhand.getFloat(itemrenderer)<t) {
		 * Field_ItemRenderer_equippedProgressMainhand.setFloat(itemrenderer, t);
		 * Field_ItemRenderer_prevEquippedProgressMainhand.setFloat(itemrenderer, t); }
		 * } else {
		 * if(Field_ItemRenderer_equippedProgressOffhand.getFloat(itemrenderer)<t) {
		 * Field_ItemRenderer_equippedProgressOffhand.setFloat(itemrenderer, t);
		 * Field_ItemRenderer_prevEquippedProgressOffhand.setFloat(itemrenderer, t); } }
		 * } catch (IllegalArgumentException e) { e.printStackTrace(); } catch
		 * (IllegalAccessException e) { e.printStackTrace(); }
		 * 
		 * } */
	}
}
