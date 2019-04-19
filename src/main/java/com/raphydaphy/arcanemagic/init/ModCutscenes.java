package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.ArcaneMagicClient;
import com.raphydaphy.cutsceneapi.api.ClientCutscene;
import com.raphydaphy.cutsceneapi.api.Cutscene;
import com.raphydaphy.cutsceneapi.cutscene.*;
import com.raphydaphy.cutsceneapi.fakeworld.CutsceneWorld;
import com.raphydaphy.cutsceneapi.fakeworld.storage.CutsceneWorldLoader;
import com.raphydaphy.cutsceneapi.path.RecordedPath;
import com.raphydaphy.cutsceneapi.path.SplinePath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ModCutscenes {
    public static Cutscene RELIC_NETHER = new DefaultCutscene(600);

    public static void init() {
        CutsceneRegistry.register(new Identifier(ArcaneMagic.DOMAIN, "relic_nether"), RELIC_NETHER);
    }

    @Environment(EnvType.CLIENT)
    public static void preInitClient() {
        RELIC_NETHER = new DefaultClientCutscene(RELIC_NETHER.getLength());
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientCutscene relicNether = (ClientCutscene) RELIC_NETHER;
        relicNether.setIntroTransition(new Transition.DipTo(20, 0, 1, 1, 1));
        relicNether.setOutroTransition(new Transition.DipTo(20, 0, 1, 1, 1));
        relicNether.setShader(new Identifier(ArcaneMagic.DOMAIN, "shaders/cutscene.json"));
        relicNether.setWorldType(CutsceneWorldType.CUSTOM);
        relicNether.setInitCallback((cutscene) ->
        {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientCutscene clientCutscene = (ClientCutscene) cutscene;
            clientCutscene.setCameraPath(RecordedPath.fromFile(new Identifier(ArcaneMagic.DOMAIN, "cutscenes/paths/relic_nether_1.cpath")));
            client.player.playSound(ModSounds.CUTSCENE_START, 1, 1);
        });
        relicNether.setWorldInitCallback((cutscene) ->
        {
            MinecraftClient client = MinecraftClient.getInstance();
            CutsceneWorld cutsceneWorld = new CutsceneWorld(client, client.world, null, false);
            CutsceneWorldLoader.addChunks("nether.cworld", cutsceneWorld, 8);
            cutscene.setWorld(cutsceneWorld);
        });
    }
}
