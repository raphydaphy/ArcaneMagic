package com.raphydaphy.arcanemagic.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.particle.ParticleSource;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.PerlinNoiseGenerator;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ScreenShake
{
	private static PerlinNoiseGenerator noise1 = new PerlinNoiseGenerator(1);
	private static PerlinNoiseGenerator noise2 = new PerlinNoiseGenerator(2);
	private static PerlinNoiseGenerator noise3 = new PerlinNoiseGenerator(3);
	private static PerlinNoiseGenerator noise4 = new PerlinNoiseGenerator(4);
	private static PerlinNoiseGenerator noise5 = new PerlinNoiseGenerator(5);

	private static int delay = 0;
	private static int shakeTime = 0;

	public static void scheduleShake(int delay, int duration)
	{
		ScreenShake.delay += delay;
		ScreenShake.shakeTime += duration;
	}

	public static void update()
	{
		if (delay > 0)
		{
			delay--;
		} else if (shakeTime > 0)
		{
			shakeTime--;
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null && shakeTime > 20)
			{
				float inverseSpread = 0.2f;

				for (int i = 0; i < 16; i++)
				{
					float posX = (float)(player.x + ArcaneMagic.RANDOM.nextGaussian() / inverseSpread);
					float posY = (float)player.y;
					float posZ = (float)(player.z + ArcaneMagic.RANDOM.nextGaussian() / inverseSpread);

					BlockPos gridPos = new BlockPos(posX, Math.round(posY), posZ);

					for (int y = -2; y < 2; y++)
					{
						BlockState state = player.world.getBlockState(gridPos.add(0, y, 0));
						if (state.isAir())
						{
							BlockPos posDown = gridPos.add(0, y - 1, 0);
							BlockState down = player.world.getBlockState(posDown);
							if (down.getRenderType() != BlockRenderType.INVISIBLE)
							{
								int color = MinecraftClient.getInstance().getBlockColorMap().method_1691(down, player.world, posDown);
								if (down.getBlock() instanceof FallingBlock)
								{
									color = ((FallingBlock) down.getBlock()).getColor(down);
								}

								float r = (float) (color >> 16 & 255) / 255.0F;
								float g = (float) (color >> 8 & 255) / 255.0F;
								float b = (float) (color & 255) / 255.0F;

								float actualY = posY + y + 0.01f;
								float inverseSourceSpread = 500;
								ParticleRenderer.INSTANCE.addSource(new ParticleSource((ticks) -> {
									if (ticks % (ArcaneMagic.RANDOM.nextInt(3) + 1) == 0)
									{
										ParticleUtil.addRockParticle(player.world, posX, actualY, posZ,
												(float) ArcaneMagic.RANDOM.nextGaussian() / inverseSourceSpread, ArcaneMagic.RANDOM.nextFloat() / 50f, (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSourceSpread,
												r, g, b, 1, 0.1f, 40);
									}
								}, 20));
								break;
							}
						}
					}
				}
			}
		}
	}

	public static void shake()
	{
		if (delay <= 0 && shakeTime > 0)
		{
			screenShake();
		}
	}

	private static void screenShake()
	{
		if (!MinecraftClient.getInstance().isPaused())
		{
			float intensity = 2;

			World world = MinecraftClient.getInstance().world;
			PlayerEntity player = MinecraftClient.getInstance().player;
			float time = ArcaneMagicUtils.lerp(world.getTime() - 1, world.getTime(), MinecraftClient.getInstance().getTickDelta());
			GlStateManager.rotated(intensity * noise1.noise1(time), 1, 0, 0);
			GlStateManager.rotated(intensity * noise2.noise1(time), 0, 1, 0);
			GlStateManager.rotated(intensity * noise3.noise1(time), 0, 0, 1);
			player.pitch += intensity * noise4.noise1(time);
			player.yaw += intensity * noise5.noise1(time);
		}
	}
}
