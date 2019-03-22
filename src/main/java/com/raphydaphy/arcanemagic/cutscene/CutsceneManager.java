package com.raphydaphy.arcanemagic.cutscene;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class CutsceneManager
{
	@Environment(EnvType.CLIENT)
	public static void render()
	{
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		int timeLeft = ((DataHolder)player).getAdditionalData().getInt(ArcaneMagicConstants.CUTSCENE_TIME_LEFT_KEY);
	}

	public static void update(Iterable<ServerWorld> worlds)
	{
		for (ServerWorld world : worlds)
		{
			for (ServerPlayerEntity player : world.getPlayers())
			{
				DataHolder dataPlayer = (DataHolder) player;

				int time = dataPlayer.getAdditionalData().getInt(ArcaneMagicConstants.CUTSCENE_TIME_LEFT_KEY);
				if (time == 1)
				{
					dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.WATCHING_CUTSCENE_KEY, false);
				}
				dataPlayer.getAdditionalData().putInt(ArcaneMagicConstants.CUTSCENE_TIME_LEFT_KEY, time - 1);
				dataPlayer.markAdditionalDataDirty();
			}
		}
	}
}
