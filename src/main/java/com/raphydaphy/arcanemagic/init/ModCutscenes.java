package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.cutsceneapi.api.ClientCutscene;
import com.raphydaphy.cutsceneapi.api.Cutscene;
import com.raphydaphy.cutsceneapi.cutscene.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ModCutscenes
{
	public static Cutscene RELIC_NETHER = new DefaultCutscene(300);

	public static void init()
	{
		CutsceneRegistry.register(new Identifier(ArcaneMagic.DOMAIN, "relic_nether"), RELIC_NETHER);
	}

	@Environment(EnvType.CLIENT)
	public static void preInitClient()
	{
		RELIC_NETHER = new DefaultClientCutscene(RELIC_NETHER.getLength());
	}

	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		ClientCutscene relicNether = (ClientCutscene)RELIC_NETHER;
		relicNether.setIntroTransition(new Transition.DipTo(20, 0, 1, 1, 1));
		relicNether.setOutroTransition(new Transition.DipTo(20, 0, 1, 1, 1));
		relicNether.setShader(new Identifier(ArcaneMagic.DOMAIN, "shaders/cutscene.json"));
		relicNether.setInitCallback((cutscene) ->
		                            {
		                            	ClientCutscene clientCutscene = (ClientCutscene)cutscene;
			                            MinecraftClient client = MinecraftClient.getInstance();
		                            	clientCutscene.setCameraPath(Path.builder().with((int)client.player.x - 20, (int)client.player.y + 30, (int)client.player.z)
			                                                        .with((int)client.player.x + 30, (int)client.player.y + 10, (int)client.player.z).build());
		                            	client.player.playSound(ModSounds.CUTSCENE_START, 1, 1);
		                            });
	}
}
