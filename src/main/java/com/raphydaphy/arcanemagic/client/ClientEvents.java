package com.raphydaphy.arcanemagic.client;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

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
