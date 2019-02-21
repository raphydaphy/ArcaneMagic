package com.raphydaphy.empowered.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.empowered.Empowered;
import com.raphydaphy.empowered.init.ModRegistry;
import com.raphydaphy.empowered.item.ChannelingRodItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ClientEvents
{
	// Lock camera during drain

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
				if (held.getItem() == ModRegistry.CHANNELING_ROD)
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

	// Soul meter HUD

	private static final int SOUL_HUD_ALPHA_TIME = 15;
	private static int wandPreviousSoul = 0;
	private static long wandHudLastIncrementTick = 0;
	private static int wandSelectedTicks = -SOUL_HUD_ALPHA_TIME;

	private static void drawWandHud(float alpha)
	{
		int row = wandPreviousSoul / 10;
		int col = wandPreviousSoul % 10;
		MinecraftClient mc = MinecraftClient.getInstance();

		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.color4f(1, 1, 1, alpha);
		GlStateManager.scaled(1.5, 1.5, 1.5);
		GlStateManager.translated(8, 8, 0);
		mc.getTextureManager().bindTexture(new Identifier(Empowered.DOMAIN, "textures/item/channeling_rod.png"));
		Drawable.drawTexturedRect(10, 10, 0, 0, 16, 16, 16, 16);

		mc.getTextureManager().bindTexture(new Identifier(Empowered.DOMAIN, "textures/misc/soul-meter.png"));
		Drawable.drawTexturedRect(/* x */ 0, /* y */ 0,
				/* min-u */ 36 * col, /* min-v */ 36 * row,
				/* max-u */ 36, /* max-v */ 36,
				/* width */ 360, /* height */ 324);

		GlStateManager.popMatrix();
	}

	public static void onDrawScreenPost(float partialTicks)
	{

		MinecraftClient mc = MinecraftClient.getInstance();
		ItemStack held = mc.player.getMainHandStack();
		if (held.isEmpty())
		{
			held = mc.player.getOffHandStack();
		}

		if (held.getItem() == ModRegistry.CHANNELING_ROD)
		{
			if (wandSelectedTicks < 0)
			{
				wandSelectedTicks = 0;
			}
			int currentSoul = 0;

			if (held.getTag() != null)
			{
				currentSoul = held.getTag().getInt(ChannelingRodItem.SOUL_KEY);
			}

			if (mc.world.getTime() != wandHudLastIncrementTick && currentSoul != wandPreviousSoul)
			{
				wandSelectedTicks++;

				if (wandSelectedTicks > SOUL_HUD_ALPHA_TIME)
				{
					if (currentSoul < wandPreviousSoul)
					{
						int distance = wandPreviousSoul - currentSoul;
						int change = (Math.round(distance / 3));
						wandPreviousSoul -= change < 1 ? 1 : change > 8 ? 8 : change;
					} else
					{
						int distance = currentSoul - wandPreviousSoul;
						int change = (Math.round(distance / 3));
						wandPreviousSoul += change < 1 ? 1 : change > 8 ? 8 : change;
					}
				}
				else if (wandSelectedTicks > 3)
				{
					int ticksLeft = SOUL_HUD_ALPHA_TIME - wandSelectedTicks;

					if (currentSoul > wandPreviousSoul)
					{
						int distanceLeft = currentSoul - wandPreviousSoul;
						int change = Math.round(distanceLeft / ticksLeft);
						wandPreviousSoul += change < 1 ? 1 : change;
					}
				}
				wandHudLastIncrementTick = mc.world.getTime();
			}
			else if (currentSoul == wandPreviousSoul && wandSelectedTicks <= SOUL_HUD_ALPHA_TIME + 1)
			{
				wandSelectedTicks++;
			}
			drawWandHud(wandSelectedTicks > SOUL_HUD_ALPHA_TIME ? 1 : wandSelectedTicks / (float)SOUL_HUD_ALPHA_TIME);
		}
		else
		{
			if (wandSelectedTicks > 0)
			{
				wandSelectedTicks = 0;
			}

			if (wandSelectedTicks < -SOUL_HUD_ALPHA_TIME)
			{
				wandPreviousSoul = 0;
			}
			else
			{
				drawWandHud(1f - (-wandSelectedTicks / (float)SOUL_HUD_ALPHA_TIME));
				if (mc.world.getTime() != wandHudLastIncrementTick)
				{
					wandSelectedTicks--;
					wandHudLastIncrementTick = mc.world.getTime();
				}
			}
		}


	}
}
