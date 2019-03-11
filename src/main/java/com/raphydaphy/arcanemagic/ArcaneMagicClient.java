package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.entity.*;
import com.raphydaphy.arcanemagic.client.model.ArcaneModelLoader;
import com.raphydaphy.arcanemagic.client.model.IronDaggerModel;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.render.*;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.network.PlayerDataUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.util.ModelIdentifier;

public class ArcaneMagicClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient()
	{
		BlockEntityRendererRegistry.INSTANCE.register(AltarBlockEntity.class, new AltarRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(AnalyzerBlockEntity.class, new AnalyzerRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(CrystalInfuserBlockEntity.class, new CrystalInfuserRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(MixerBlockEntity.class, new MixerRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(PipeBlockEntity.class, new PipeRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(SmelterBlockEntity.class, new SmelterRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(TransfigurationTableBlockEntity.class, new TransfigurationTableRenderer());

		ClientSidePacketRegistry.INSTANCE.register(ClientBlockEntityUpdatePacket.ID, new ClientBlockEntityUpdatePacket.Handler());
		ClientSidePacketRegistry.INSTANCE.register(PlayerDataUpdatePacket.ID, new PlayerDataUpdatePacket.Handler());
		ClientSidePacketRegistry.INSTANCE.register(ProgressionUpdateToastPacket.ID, new ProgressionUpdateToastPacket.Handler());

		ClientSpriteRegistryCallback.registerBlockAtlas((atlaxTexture, registry) ->
		{
			registry.register(ArcaneMagicConstants.GLOW_PARTICLE_TEXTURE);
			registry.register(ArcaneMagicConstants.SMOKE_PARTICLE_TEXTURE);
		});

		ClientTickCallback.EVENT.register(client -> ParticleRenderer.INSTANCE.update());

		ArcaneModelLoader.registerModel(new ModelIdentifier(ModRegistry.IRON_DAGGER_IDENTIFIER, "inventory"), IronDaggerModel::new);
	}
}
