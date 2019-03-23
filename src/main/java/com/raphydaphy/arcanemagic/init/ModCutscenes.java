package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.cutsceneapi.cutscene.Cutscene;
import com.raphydaphy.cutsceneapi.cutscene.CutsceneRegistry;
import com.raphydaphy.cutsceneapi.cutscene.Path;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModCutscenes
{
	public static Identifier RELIC_NETHER;

	public static void init()
	{
		RELIC_NETHER = CutsceneRegistry.register(new Identifier(ArcaneMagic.DOMAIN, "relic"), (player) ->
		{
			float pX = (float) player.x;
			float pY = (float) player.y;
			float pZ = (float) player.z;
			return new Cutscene(player, new Path().withPoint(pX + 50, pY + 30, pZ).withPoint(pX, pY + 10, pZ - 10).withPoint(pX - 20, pY + 10, pZ - 10))
					.withDuration(150).withStartSound(ModSounds.CUTSCENE_START).withDipTo(20, 255, 255, 255).withShader(new Identifier(ArcaneMagic.DOMAIN, "shaders/cutscene.json"));
		});
	}
}
