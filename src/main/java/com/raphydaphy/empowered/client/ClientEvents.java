package com.raphydaphy.empowered.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.empowered.Empowered;
import com.raphydaphy.empowered.init.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ClientEvents
{
	private static boolean prevUsingWand = false;
	private static float wandRotationYaw = 0;
	private static float wandRotationPitch = 0;

	public static void onRenderTick()
	{
		boolean usingWand = false;
		if (MinecraftClient.getInstance().world != null)
		{
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player.isUsingItem())
			{
				ItemStack held = player.getStackInHand(player.getActiveHand());
				if (held.getItem() == ModItems.CHANNELING_ROD)
				{
					usingWand = true;
					if (!prevUsingWand)
					{
						wandRotationPitch = player.pitch;
						wandRotationYaw = player.yaw;
					}

					player.pitch = wandRotationPitch;
					player.yaw = wandRotationYaw;
				}
			}
		}
		prevUsingWand = usingWand;
	}

	public static void onDrawScreenPost(float partialTicks)
	{

		MinecraftClient mc = MinecraftClient.getInstance();
		ItemStack held = mc.player.getMainHandStack();
		if (held.isEmpty())
		{
			held = mc.player.getOffHandStack();
		}

		if (held.getItem() == ModItems.CHANNELING_ROD)
		{
			GlStateManager.pushMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.scaled(1.6, 1.6, 1.6);
			GlStateManager.translated(-28, -18, 0);
			mc.getItemRenderer().renderGuiItem(held, 50, 40);
			GlStateManager.popMatrix();

			GlStateManager.scaled(0.25, 0.25, 0.25);
			mc.getTextureManager().bindTexture(new Identifier(Empowered.DOMAIN, "textures/misc/soul-meter.png"));
			mc.inGameHud.drawTexturedRect(64, 64, 0, 0, 255, 255);

			GlStateManager.popMatrix();
		}


	}
}
