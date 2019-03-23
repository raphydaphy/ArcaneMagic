package com.raphydaphy.arcanemagic.cutscene;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModSounds;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.CutsceneStartPacket;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class CutsceneManager
{
	private static Cutscene currentCutscene;

	public static boolean hideHud(PlayerEntity player)
	{
		return isActive(player) && currentCutscene != null && currentCutscene.hideHud();
	}

	public static boolean isActive(PlayerEntity player)
	{
		return ((DataHolder) player).getAdditionalData().getBoolean(ArcaneMagicConstants.WATCHING_CUTSCENE_KEY);
	}

	@Environment(EnvType.CLIENT)
	public static void updateLook()
	{
		if (currentCutscene != null)
		{
			currentCutscene.updateLook();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void renderHud()
	{
		if (currentCutscene != null)
		{
			currentCutscene.renderTransitions();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void startClient()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		client.player.playSound(ModSounds.CUTSCENE_START, 1, 1);
		currentCutscene = new Cutscene(client.player, new Path().withPoint(100, 100, 100))
				.withDipTo(20, 255, 255, 255).withShader(new Identifier(ArcaneMagic.DOMAIN, "shaders/cutscene.json"));
	}

	@Environment(EnvType.CLIENT)
	public static void updateClient()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		if (isActive(client.player) && currentCutscene != null)
		{
			currentCutscene.updateClient();
		}
	}

	public static void startServer(ServerPlayerEntity player, int duration)
	{
		player.stopRiding();
		DataHolder dataPlayer = (DataHolder) player;
		dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.WATCHING_CUTSCENE_KEY, true);
		dataPlayer.getAdditionalData().putInt(ArcaneMagicConstants.CUTSCENE_LENGTH, duration);
		dataPlayer.getAdditionalData().putInt(ArcaneMagicConstants.CUTSCENE_TIME, 0);
		dataPlayer.markAdditionalDataDirty();

		ArcaneMagicPacketHandler.sendToClient(new CutsceneStartPacket(), player);
	}

	public static void finishServer(PlayerEntity player)
	{
		DataHolder dataPlayer = (DataHolder) player;
		dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.WATCHING_CUTSCENE_KEY, false);
		dataPlayer.markAdditionalDataDirty();
	}

	public static void updateServer(Iterable<ServerWorld> worlds)
	{
		for (ServerWorld world : worlds)
		{
			for (ServerPlayerEntity player : world.getPlayers())
			{
				DataHolder dataPlayer = (DataHolder) player;

				if (dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.WATCHING_CUTSCENE_KEY))
				{
					int time = dataPlayer.getAdditionalData().getInt(ArcaneMagicConstants.CUTSCENE_TIME);
					int length = dataPlayer.getAdditionalData().getInt(ArcaneMagicConstants.CUTSCENE_LENGTH);
					if (time >= length)
					{
						dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.WATCHING_CUTSCENE_KEY, false);
					} else
					{
						dataPlayer.getAdditionalData().putInt(ArcaneMagicConstants.CUTSCENE_TIME, time + 1);
					}
					dataPlayer.markAdditionalDataDirty();
				}
			}
		}
	}
}
