package com.raphydaphy.arcanemagic.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.particle.ParticleSource;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.network.TremorPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TremorTracker
{
	private static PerlinNoiseGenerator noise1 = new PerlinNoiseGenerator(1);
	private static PerlinNoiseGenerator noise2 = new PerlinNoiseGenerator(2);
	private static PerlinNoiseGenerator noise3 = new PerlinNoiseGenerator(3);
	private static PerlinNoiseGenerator noise4 = new PerlinNoiseGenerator(4);
	private static PerlinNoiseGenerator noise5 = new PerlinNoiseGenerator(5);

	private static int delay = 0;
	private static int tremorTime = 0;

	@Environment(EnvType.CLIENT)
	public static void scheduleTremor(int delay, int duration)
	{
		TremorTracker.delay += delay;
		TremorTracker.tremorTime += duration;
	}

	public static void updateServer(ServerWorld world)
	{
		for (ServerPlayerEntity player : world.getPlayers())
		{
			DataHolder dataPlayer = (DataHolder) player;

			if (dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_MIXER_KEY) && player.y < 25)
			{
				if (world.getTime() % 100 == 0)
				{
					int timeSinceTremor = dataPlayer.getAdditionalData().getInt(ArcaneMagicConstants.TIME_SINCE_TREMOR_KEY);
					int timeSinceVoidSound = dataPlayer.getAdditionalData().getInt(ArcaneMagicConstants.TIME_SINCE_VOID_SOUND_KEY);
					int rand = ArcaneMagic.RANDOM.nextInt((int)(player.y * 3));
					System.out.println("Time since tremor: " + timeSinceTremor + " Time since last noise: " + timeSinceVoidSound + " Random: " + rand);
					if (rand == 0)
					{
						if (!dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.EXPERIENCED_TREMOR_KEY))
						{
							dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.EXPERIENCED_TREMOR_KEY, true);
							dataPlayer.getAdditionalData().putInt(ArcaneMagicConstants.TIME_SINCE_TREMOR_KEY, 0);
							ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.TREMORS.getID().toString(), false);
							dataPlayer.markAdditionalDataDirty();
							ArcaneMagicPacketHandler.sendToClient(new TremorPacket(5, ArcaneMagic.RANDOM.nextInt(20) + 30), player);
							ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), player);
						} else
						{
							int tremorRand = ArcaneMagic.RANDOM.nextInt(Math.max(150 - timeSinceTremor, 1));
							System.out.println("Tremor random: " + tremorRand);
							if (timeSinceTremor > 50 && tremorRand == 0)
							{
								timeSinceTremor = -1;
								ArcaneMagicPacketHandler.sendToClient(new TremorPacket(5, ArcaneMagic.RANDOM.nextInt(20) + 50), player);
							}
						}
					} else if (rand < 100 - timeSinceVoidSound)
					{
						int soundRand = ArcaneMagic.RANDOM.nextInt(Math.max(50 - timeSinceVoidSound, 1));
						if (timeSinceVoidSound > 20 && soundRand == 0)
						{
							System.out.println("magic the beetle bard");
							ArcaneMagicPacketHandler.sendToClient(new TremorPacket(0, 0), player);
							timeSinceVoidSound = -1;
						}
					}
					dataPlayer.getAdditionalData().putInt(ArcaneMagicConstants.TIME_SINCE_TREMOR_KEY, timeSinceTremor + 1);
					dataPlayer.getAdditionalData().putInt(ArcaneMagicConstants.TIME_SINCE_VOID_SOUND_KEY, timeSinceVoidSound + 1);
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static void updateClient()
	{
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		if (player != null)
		{
			if (delay > 0)
			{
				delay--;
			} else if (tremorTime > 0)
			{
				tremorTime--;
				if (tremorTime > 20)
				{
					float inverseSpread = 0.2f;

					for (int i = 0; i < 16; i++)
					{
						float posX = (float) (player.x + ArcaneMagic.RANDOM.nextGaussian() / inverseSpread);
						float posY = (float) player.y;
						float posZ = (float) (player.z + ArcaneMagic.RANDOM.nextGaussian() / inverseSpread);

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
									float inverseSourceSpread = 1000;
									ParticleRenderer.INSTANCE.addSource(new ParticleSource((ticks) ->
									{
										if (ticks % (ArcaneMagic.RANDOM.nextInt(3) + 1) == 0)
										{
											ParticleUtil.spawnGlowParticle(player.world, posX, actualY, posZ,
													(float) ArcaneMagic.RANDOM.nextGaussian() / inverseSourceSpread, ArcaneMagic.RANDOM.nextFloat() / 100f, (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSourceSpread,
													r, g, b, 1, false, 0.05f + ArcaneMagic.RANDOM.nextFloat() / 20, 40);
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
	}

	@Environment(EnvType.CLIENT)
	public static void renderTremors()
	{
		if (delay <= 0 && tremorTime > 0)
		{
			doTremor();
		}
	}

	@Environment(EnvType.CLIENT)
	private static void doTremor()
	{
		if (!MinecraftClient.getInstance().isPaused())
		{
			float intensity = 4f;

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
