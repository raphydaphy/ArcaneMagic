package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.ArcaneMagicClient;
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
		relicNether.setWorldType(CutsceneWorldType.CUSTOM);
		relicNether.setInitCallback((cutscene) ->
		                            {
			                            MinecraftClient client = MinecraftClient.getInstance();
		                            	ClientCutscene clientCutscene = (ClientCutscene)cutscene;
		                            	ArcaneMagicClient.OLD_NETHER_WORLD.setupFrom(client.world);
		                            	clientCutscene.setWorld(ArcaneMagicClient.OLD_NETHER_WORLD);
		                            	clientCutscene.setCameraPath(Path.builder().with(-200, 70, 0).with(100, 80, 20).with(100, 80, 200).build());
		                            	client.player.playSound(ModSounds.CUTSCENE_START, 1, 1);
		                            });
	}
}
