package com.raphydaphy.empowered.client;

import com.raphydaphy.empowered.init.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

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
}
