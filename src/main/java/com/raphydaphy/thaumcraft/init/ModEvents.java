package com.raphydaphy.thaumcraft.init;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.gui.GuiWand;
import com.raphydaphy.thaumcraft.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
			
			if ((!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem().equals(ModItems.wand)) ||
					!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem().equals(ModItems.wand))
			{
				GuiWand.renderWandHUD(mc, event.getResolution());
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent event)
	{
		ResourceLocation particleSparkle = new ResourceLocation(Thaumcraft.MODID + ":misc/particle_star");
		event.getMap().registerSprite(particleSparkle);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTick(TickEvent.ClientTickEvent event){
		if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START){
			ClientProxy.particleRenderer.updateParticles();
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderAfterWorld(RenderWorldLastEvent event)
	{
		if (Thaumcraft.proxy instanceof ClientProxy){
			GlStateManager.pushMatrix();
			ClientProxy.particleRenderer.renderParticles(clientPlayer, event.getPartialTicks());
			GlStateManager.popMatrix();
		}
	}
}
