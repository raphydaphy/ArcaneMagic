package com.raphydaphy.arcanemagic.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ClientEvents
{
	/********************************
	     Lock camera during drain
	 ********************************/

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
				if (held.getItem() == ModRegistry.GOLDEN_SCEPTER)
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

	/**********************
	     Soul meter HUD
	 **********************/

	private enum SoulMeterMode
	{
		EMPTY(0), GOLDEN_SCEPTER(1), GOLDEN_SCEPTER_WITH_PENDANT(2);

		public final int id;
		SoulMeterMode(int id)
		{
			this.id = id;
		}
	}

	// How long the soul meter takes to fade in and out
	private static final int SOUL_HUD_ALPHA_TIME = 15;

	// The amount of soul currently visible in the soul meter
	private static int soulMeterAmount = 0;

	// The world time when the soul meter last changed
	private static long soulMeterLastIncrementTick = 0;

	// How long has the meter been displayed for?
	// Caps at SOUL_HUD_ALPHA_TIME + 1 and goes down to -SOUL_HUD_ALPHA_TIME - 1 when deselected
	private static int soulMeterActiveTicks = -SOUL_HUD_ALPHA_TIME;

	// The mode the soul meter was in last frame
	private static SoulMeterMode lastSoulMeterMode = SoulMeterMode.EMPTY;

	// Texture with all the soul meter levels, centers and outlines
	private static Identifier SOUL_METER_LEVELS = new Identifier(ArcaneMagic.DOMAIN, "textures/misc/soul-meter.png");

	// Active soul pendant slot, if any
	private static int pendant = -1;

	private static void drawWandHud(float alpha)
	{
		float percent = ((float)soulMeterAmount / (ArcaneMagicConstants.SOUL_METER_MAX));
		int stage = Math.round(percent * ArcaneMagicConstants.SOUL_METER_STAGES);
		MinecraftClient mc = MinecraftClient.getInstance();

		int row = stage / 10;
		int col = stage % 10;

		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.color4f(1, 1, 1, alpha);
		GlStateManager.scaled(1.5, 1.5, 1.5);
		GlStateManager.translated(8, 8, 0);

		mc.getTextureManager().bindTexture(SOUL_METER_LEVELS);

		// Outline & Center
		DrawableHelper.drawTexturedRect(/* x */ 0, /* y */ 0,
				/* min-u */ 36 * lastSoulMeterMode.id, /* min-v */ 0,
				/* max-u */ 36, /* max-v */ 36,
				/* width */ 360, /* height */ 360);

		// Filled area
		DrawableHelper.drawTexturedRect(/* x */ 0, /* y */ 0,
				/* min-u */ 36 * col, /* min-v */ 36 + 36 * row,
				/* max-u */ 36, /* max-v */ 36,
				/* width */ 360, /* height */ 360);

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

		if (held.getItem() == ModRegistry.GOLDEN_SCEPTER)
		{
			if (soulMeterActiveTicks < 0)
			{
				soulMeterActiveTicks = 0;
			}
			int currentSoul = 0;

			if (held.getTag() != null)
			{
				currentSoul = held.getTag().getInt(ArcaneMagicConstants.SOUL_KEY);
			}

			if (lastSoulMeterMode == SoulMeterMode.EMPTY)
			{
				lastSoulMeterMode = SoulMeterMode.GOLDEN_SCEPTER;
			}



			// Runs every second to check for a soul pendant in the players inventory
			if (mc.world.getTime() != soulMeterLastIncrementTick && mc.world.getTime() % 20 == 0)
			{
				pendant = ArcaneMagicUtils.findPendant(mc.player);
				if (pendant != -1)
				{
					lastSoulMeterMode = SoulMeterMode.GOLDEN_SCEPTER_WITH_PENDANT;
				} else
				{
					lastSoulMeterMode = SoulMeterMode.GOLDEN_SCEPTER;
				}
			}
			boolean validPendant = false;

			if (pendant != -1)
			{
				ItemStack pendantItem = mc.player.inventory.getInvStack(pendant);
				if (!pendantItem.isEmpty() && pendantItem.getItem() == ModRegistry.SOUL_PENDANT)
				{
					if (pendantItem.getTag() != null)
					{
						currentSoul = currentSoul + pendantItem.getTag().getInt(ArcaneMagicConstants.SOUL_KEY);
					}
					lastSoulMeterMode = SoulMeterMode.GOLDEN_SCEPTER_WITH_PENDANT;
					validPendant = true;
				}
			}

			if (!validPendant)
			{
				lastSoulMeterMode = SoulMeterMode.GOLDEN_SCEPTER;
			}

			if (mc.world.getTime() != soulMeterLastIncrementTick && currentSoul != soulMeterAmount)
			{
				soulMeterActiveTicks++;

				if (soulMeterActiveTicks > SOUL_HUD_ALPHA_TIME)
				{
					if (currentSoul < soulMeterAmount)
					{
						int distance = soulMeterAmount - currentSoul;
						int change = (Math.round(distance / 3));
						soulMeterAmount -= change < 1 ? 1 : change > 8 ? 8 : change;
					} else
					{
						int distance = currentSoul - soulMeterAmount;
						int change = (Math.round(distance / 3));
						soulMeterAmount += change < 1 ? 1 : change > 8 ? 8 : change;
					}
				}
				else if (soulMeterActiveTicks > 3)
				{
					int ticksLeft = SOUL_HUD_ALPHA_TIME - soulMeterActiveTicks;

					if (currentSoul > soulMeterAmount)
					{
						int distanceLeft = currentSoul - soulMeterAmount;
						int change = distanceLeft;
						if (ticksLeft > 0)
						{
							change = Math.round(distanceLeft / ticksLeft);
						}
						soulMeterAmount += change < 1 ? 1 : change;
					}
				}
				soulMeterLastIncrementTick = mc.world.getTime();
			}
			else if (mc.world.getTime() != soulMeterLastIncrementTick)
			{
				soulMeterActiveTicks++;
				soulMeterLastIncrementTick = mc.world.getTime();
			}

			float alpha = 1;
			if (soulMeterActiveTicks <= SOUL_HUD_ALPHA_TIME)
			{
				float interpolatedTicks = ArcaneMagicUtils.lerp(soulMeterActiveTicks - 1, soulMeterActiveTicks, partialTicks);
				alpha = interpolatedTicks / (float)SOUL_HUD_ALPHA_TIME;
			}
			drawWandHud(alpha);
		}
		else
		{
			if (soulMeterActiveTicks > 0)
			{
				soulMeterActiveTicks = 0;
			}

			if (soulMeterActiveTicks < -SOUL_HUD_ALPHA_TIME)
			{
				soulMeterAmount = 0;
			}
			else
			{
				float activeTicksInterpolated = ArcaneMagicUtils.lerp(-soulMeterActiveTicks + 1, -soulMeterActiveTicks, partialTicks);
				drawWandHud(1f - (activeTicksInterpolated / (float)SOUL_HUD_ALPHA_TIME));
				if (mc.world.getTime() != soulMeterLastIncrementTick)
				{
					soulMeterActiveTicks--;
					soulMeterLastIncrementTick = mc.world.getTime();
				}
			}
		}


	}
}
