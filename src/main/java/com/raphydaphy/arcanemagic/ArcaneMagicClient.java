package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.entity.*;
import com.raphydaphy.arcanemagic.client.model.ArcaneModelLoader;
import com.raphydaphy.arcanemagic.client.model.IronDaggerModel;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.render.*;
import com.raphydaphy.arcanemagic.fluid.ModFluidRenderHandler;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModCutscenes;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.network.TremorPacket;
import com.raphydaphy.arcanemagic.util.TremorTracker;
import com.raphydaphy.cutsceneapi.fakeworld.storage.CutsceneWorldLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class ArcaneMagicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(AltarBlockEntity.class, new AltarRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(AnalyzerBlockEntity.class, new AnalyzerRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(CrystalInfuserBlockEntity.class, new CrystalInfuserRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(MixerBlockEntity.class, new MixerRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(PipeBlockEntity.class, new PipeRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(SmelterBlockEntity.class, new SmelterRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(TransfigurationTableBlockEntity.class, new TransfigurationTableRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(PumpBlockEntity.class, new PumpRenderer());

        ClientSidePacketRegistry.INSTANCE.register(ClientBlockEntityUpdatePacket.ID, new ClientBlockEntityUpdatePacket.Handler());
        ClientSidePacketRegistry.INSTANCE.register(ProgressionUpdateToastPacket.ID, new ProgressionUpdateToastPacket.Handler());
        ClientSidePacketRegistry.INSTANCE.register(TremorPacket.ID, new TremorPacket.Handler());

        ClientSpriteRegistryCallback.registerBlockAtlas((atlaxTexture, registry) ->
        {
            CutsceneWorldLoader.copyCutsceneWorld(new Identifier(ArcaneMagic.DOMAIN, "cutscenes/worlds/nether.cworld"), "nether.cworld");
            registry.register(ArcaneMagicConstants.GLOW_PARTICLE_TEXTURE);
            registry.register(ArcaneMagicConstants.SMOKE_PARTICLE_TEXTURE);
            registry.register(ArcaneMagicConstants.FLOWING_LIQUID_SOUL_TEXTURE);
        });

        ClientTickCallback.EVENT.register((client) ->
        {
            if (!MinecraftClient.getInstance().isPaused()) {
                ParticleRenderer.INSTANCE.update();
                TremorTracker.updateClient();
                HudRenderer.update();
            }
        });

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ModFluidRenderHandler());

        ArcaneModelLoader.registerModel(new ModelIdentifier(ModRegistry.IRON_DAGGER_IDENTIFIER, "inventory"), (loader) -> new IronDaggerModel());

        ModCutscenes.initClient();

    }
}
