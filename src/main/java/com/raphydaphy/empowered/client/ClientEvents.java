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

	// Soul meter HUD

	private static final int SOUL_HUD_ALPHA_TIME = 15;
	private static int wandPreviousSoul = 0;
	private static long wandHudLastIncrementTick = 0;
	private static int wandSelectedTicks = 0;

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
			if (wandSelectedTicks < 0)
			{
				wandSelectedTicks = 0;
			}
			int currentSoul = 0;

			if (held.getTag() != null)
			{
				currentSoul = held.getTag().getInt("soul");
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

			float alpha = wandSelectedTicks > SOUL_HUD_ALPHA_TIME ? 1 : wandSelectedTicks / (float)SOUL_HUD_ALPHA_TIME;

			GlStateManager.pushMatrix();
			GlStateManager.pushMatrix();

			GlStateManager.color4f(1, 1, 1, alpha);
			GlStateManager.scaled(0.1, 0.1, 0.1);
			GlStateManager.translated(350, 350, 0);
			mc.getTextureManager().bindTexture(new Identifier(Empowered.DOMAIN, "textures/item/channeling_rod.png"));
			mc.inGameHud.drawTexturedRect(0, 0, 0, 0, 255, 255);

			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();

			GlStateManager.color4f(1, 1, 1, alpha);
			GlStateManager.scaled(0.25, 0.25, 0.25);
			mc.getTextureManager().bindTexture(new Identifier(Empowered.DOMAIN, "textures/misc/soul-meter-individual/soul-meter-" + wandPreviousSoul + ".png"));
			mc.inGameHud.drawTexturedRect(64, 64, 0, 0, 255, 255);

			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
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
				GlStateManager.pushMatrix();

				GlStateManager.color4f(1, 1, 1, 1 - (-wandSelectedTicks / (float)SOUL_HUD_ALPHA_TIME));
				GlStateManager.scaled(0.25, 0.25, 0.25);
				mc.getTextureManager().bindTexture(new Identifier(Empowered.DOMAIN, "textures/misc/soul-meter-individual/soul-meter-" + wandPreviousSoul + ".png"));
				mc.inGameHud.drawTexturedRect(64, 64, 0, 0, 255, 255);

				GlStateManager.popMatrix();
				wandSelectedTicks--;
			}
		}


	}
}
