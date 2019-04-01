package com.raphydaphy.arcanemagic.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlBuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ClientEvents
{
	/********************************
	 Lock camera during drain
	 ********************************/

	private static boolean prevUsingScepter = false;
	private static float cachedYaw = 0;
	private static float cachedPitch = 0;

	public static void onRenderTick()
	{
		boolean usingWand = false;
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player.isUsingItem())
		{
			ItemStack held = player.getStackInHand(player.getActiveHand());
			if (held.getItem() == ModRegistry.GOLDEN_SCEPTER)
			{
				usingWand = true;
				if (!prevUsingScepter)
				{
					cachedPitch = player.pitch;
					cachedYaw = player.yaw;
				}

				player.pitch = cachedPitch;
				player.yaw = cachedYaw;
			}
		}

		prevUsingScepter = usingWand;
	}
}
