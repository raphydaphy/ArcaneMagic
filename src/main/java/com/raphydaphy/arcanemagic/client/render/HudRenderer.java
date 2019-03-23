package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.DaggerItem;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class HudRenderer
{
	// How long the soul meter takes to fade in and out
	private static final int ALPHA_TIME = 15;
	// Texture with all the soul meter levels, centers and outlines
	private static Identifier MAIN_TEX = new Identifier(ArcaneMagic.DOMAIN, "textures/misc/circle_hud.png");

	private static int displayedLevel = 0;
	private static int displayedMax = 0;
	private static Center lastCenter = Center.EMPTY;
	private static Mode mode = Mode.EMPTY;
	private static ArcaneMagicUtils.ForgeCrystal passiveCrystal = ArcaneMagicUtils.ForgeCrystal.EMPTY;
	private static ArcaneMagicUtils.ForgeCrystal activeCrystal = ArcaneMagicUtils.ForgeCrystal.EMPTY;
	private static float displayedRed = 1;
	private static float displayedGreen = 1;
	private static float displayedBlue = 1;

	// How long has the hud been displayed for?
	// Caps at ALPHA_TIME + 1 and goes down to -ALPHA_TIME - 1 when deselected
	private static int activeTicks = -ALPHA_TIME;

	// Active soul pendant slot, if any
	private static int pendantSlot = -1;

	private static void drawHud(float alpha)
	{
		float percent = ((float) (displayedLevel) / displayedMax);
		int stage = Math.round(percent * ArcaneMagicConstants.SOUL_METER_STAGES);
		MinecraftClient mc = MinecraftClient.getInstance();

		int row = stage / 10;
		int col = stage % 10;

		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.color4f(1, 1, 1, alpha);
		GlStateManager.scaled(1.5, 1.5, 1.5);
		GlStateManager.translated(8, 8, 0);

		mc.getTextureManager().bindTexture(MAIN_TEX);

		// Outline & Center
		DrawableHelper.drawTexturedRect(/* x */ 0, /* y */ 0,
				/* min-u */ 36 * lastCenter.id, /* min-v */ 0,
				/* max-u */ 36, /* max-v */ 36,
				/* width */ 360, /* height */ 360);

		// Filled area
		GlStateManager.color4f(displayedRed, displayedGreen, displayedBlue, alpha);
		DrawableHelper.drawTexturedRect(/* x */ 0, /* y */ 0,
				/* min-u */ 36 * col, /* min-v */ 36 + 36 * row,
				/* max-u */ 36, /* max-v */ 36,
				/* width */ 360, /* height */ 360);

		GlStateManager.color4f(1, 1, 1, alpha);
		if (mode == Mode.DAGGER)
		{
			if (passiveCrystal != ArcaneMagicUtils.ForgeCrystal.EMPTY)
			{
				mc.getTextureManager().bindTexture(new Identifier(ArcaneMagic.DOMAIN, "textures/" + passiveCrystal.pommel.getPath() + ".png"));
				RenderUtils.drawTexturedRect(11, 9, 0, 0, 16, 16, 16, 16, 16, 16);
			}
			if (activeCrystal != ArcaneMagicUtils.ForgeCrystal.EMPTY)
			{
				mc.getTextureManager().bindTexture(new Identifier(ArcaneMagic.DOMAIN, "textures/" + activeCrystal.hilt.getPath() + ".png"));
				RenderUtils.drawTexturedRect(11, 9, 0, 0, 16, 16, 16, 16, 16, 16);
			}
		}

		GlStateManager.popMatrix();
	}

	public static void update()
	{
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc != null && mc.player != null)
		{
			ItemStack held = mc.player.getMainHandStack();
			boolean goldenScepter = held.getItem() == ModRegistry.GOLDEN_SCEPTER;
			boolean pureScepter = held.getItem() == ModRegistry.PURE_SCEPTER;
			boolean dagger = held.getItem() == ModRegistry.IRON_DAGGER;

			mode = (goldenScepter || pureScepter) ? Mode.SCEPTER : dagger ? Mode.DAGGER : Mode.EMPTY;

			if (!mode.isEmpty())
			{
				if (activeTicks < 0)
				{
					activeTicks = 0;
				}

				int fillAmount = 0;
				int currentMax = 0;

				float r = 1;
				float g = 1;
				float b = 1;

				CompoundTag tag = held.getTag();
				if (tag != null)
				{
					if (mode == Mode.SCEPTER)
					{
						currentMax = ArcaneMagicConstants.SOUL_METER_MAX;
						fillAmount = tag.getInt(ArcaneMagicConstants.SOUL_KEY);
					} else
					{
						currentMax = DaggerItem.activeDuration(held);
						passiveCrystal = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_PASSIVE_CRYSTAL_KEY));
						activeCrystal = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));
						if (activeCrystal != ArcaneMagicUtils.ForgeCrystal.EMPTY)
						{
							r = activeCrystal.red;
							g = activeCrystal.green;
							b = activeCrystal.blue;
						}
						if (currentMax > 0)
						{
							fillAmount = tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY);
							if (fillAmount == 0)
							{
								fillAmount = currentMax;
							}
						} else return;
					}
				}

				if (lastCenter == Center.EMPTY || (lastCenter == Center.DAGGER) != dagger)
				{
					if (dagger) lastCenter = Center.DAGGER;
					else if (pureScepter) lastCenter = Center.PURE_SCEPTER;
					else lastCenter = Center.GOLDEN_SCEPTER;
				}

				if (mode == Mode.SCEPTER)
				{
					boolean validPendant = false;

					if (pendantSlot != -1)
					{
						ItemStack pendantItem = mc.player.inventory.getInvStack(pendantSlot);
						if (!pendantItem.isEmpty() && pendantItem.getItem() == ModRegistry.SOUL_PENDANT)
						{
							CompoundTag pendantTag = pendantItem.getTag();
							if (pendantTag != null)
							{
								fillAmount = fillAmount + pendantTag.getInt(ArcaneMagicConstants.SOUL_KEY);
							}
							if (pureScepter)
							{
								lastCenter = Center.PURE_SCEPTER_WITH_PENDANT;
							} else
							{
								lastCenter = Center.GOLDEN_SCEPTER_WITH_PENDANT;
							}
							validPendant = true;
						}
					}

					if (!validPendant)
					{
						if (pureScepter)
						{
							lastCenter = Center.PURE_SCEPTER;
						} else
						{
							lastCenter = Center.GOLDEN_SCEPTER;
						}
					}

					// Runs every second to check for a soul pendant in the players inventory
					if (mc.world.getTime() % 20 == 0)
					{
						pendantSlot = ArcaneMagicUtils.findPendant(mc.player);
						if (pendantSlot != -1)
						{
							if (pureScepter)
							{
								lastCenter = Center.PURE_SCEPTER_WITH_PENDANT;
							} else
							{
								lastCenter = Center.GOLDEN_SCEPTER_WITH_PENDANT;
							}
						} else
						{
							if (pureScepter)
							{
								lastCenter = Center.PURE_SCEPTER;
							} else
							{
								lastCenter = Center.GOLDEN_SCEPTER;
							}
						}
					}
				}
				activeTicks++;

				if (fillAmount != displayedLevel)
				{
					if (activeTicks > ALPHA_TIME)
					{
						displayedLevel = getAdjustedInt(displayedLevel, fillAmount);
					} else if (activeTicks > 3)
					{
						int ticksLeft = ALPHA_TIME - activeTicks;

						if (fillAmount > displayedLevel)
						{
							int distanceLeft = fillAmount - displayedLevel;
							int change = distanceLeft;
							if (ticksLeft > 0)
							{
								change = Math.round(distanceLeft / ticksLeft);
							}
							displayedLevel += change < 1 ? 1 : change;
						}
					}
				}

				if (displayedMax != currentMax)
				{
					if (activeTicks > ALPHA_TIME)
					{
						displayedMax = getAdjustedInt(displayedMax, currentMax);
					} else
					{
						displayedMax = currentMax;
					}
				}
				displayedRed = getAdjustedFloat(displayedRed, r);
				displayedGreen = getAdjustedFloat(displayedGreen, g);
				displayedBlue = getAdjustedFloat(displayedBlue, b);
			} else
			{
				activeTicks--;
			}
		}
	}

	private static int getAdjustedInt(int displayed, int real)
	{
		if (real < displayed)
		{
			int distance = displayed - real;
			int change = (Math.round(distance / 3));
			return displayed - (change < 1 ? 1 : change > 8 ? 8 : change);
		} else
		{
			int distance = real - displayed;
			int change = (Math.round(distance / 3));
			return displayed + (change < 1 ? 1 : change > 8 ? 8 : change);
		}
	}

	private static float getAdjustedFloat(float displayed, float real)
	{
		if (displayed != real)
		{
			if (activeTicks > ALPHA_TIME)
			{
				if (real < displayed)
				{
					float change = (displayed - real) / 9;
					displayed -= (change > 0.05f ? 0.05f : change);
					return displayed;
				} else
				{
					float change = (real - displayed) / 9;
					displayed += (change > 0.05f ? 0.05f : change);
					return displayed;
				}
			}
			return real;
		}
		return real;
	}

	public static void render(float partialTicks)
	{
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc != null && mc.player != null)
		{
			if (!mode.isEmpty())
			{
				float alpha = 1;
				if (activeTicks <= ALPHA_TIME)
				{
					float interpolatedTicks = ArcaneMagicUtils.lerp(activeTicks - 1, activeTicks, partialTicks);
					alpha = interpolatedTicks / (float) ALPHA_TIME;
				}
				drawHud(alpha);
			} else if (displayedMax > 0)
			{
				if (activeTicks > 0)
				{
					activeTicks = 0;
				}

				if (activeTicks < -ALPHA_TIME)
				{
					displayedLevel = 0;
				} else
				{
					float activeTicksInterpolated = ArcaneMagicUtils.lerp(-activeTicks + 1, -activeTicks, partialTicks);
					drawHud(1f - (activeTicksInterpolated / (float) ALPHA_TIME));
				}
			}
		}
	}

	private enum Center
	{
		EMPTY(0), GOLDEN_SCEPTER(1), GOLDEN_SCEPTER_WITH_PENDANT(2), PURE_SCEPTER(3), PURE_SCEPTER_WITH_PENDANT(4), DAGGER(5);

		public final int id;

		Center(int id)
		{
			this.id = id;
		}
	}

	private enum Mode
	{
		EMPTY, SCEPTER, DAGGER;

		public boolean isEmpty()
		{
			return this == EMPTY;
		}
	}
}
