package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.entity.AltarBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.SmelterBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.client.ClientEvents;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.render.AltarRenderer;
import com.raphydaphy.arcanemagic.client.render.CrystalInfuserRenderer;
import com.raphydaphy.arcanemagic.client.render.SmelterRenderer;
import com.raphydaphy.arcanemagic.client.render.TransfigurationTableRenderer;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

public class ArcaneMagicClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		BlockEntityRendererRegistry.INSTANCE.register(AltarBlockEntity.class, new AltarRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(CrystalInfuserBlockEntity.class, new CrystalInfuserRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(SmelterBlockEntity.class, new SmelterRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(TransfigurationTableBlockEntity.class, new TransfigurationTableRenderer());

		ClientSidePacketRegistry.INSTANCE.register(ClientBlockEntityUpdatePacket.ID, new ClientBlockEntityUpdatePacket.Handler());

		ClientSpriteRegistryCallback.EVENT.register((atlaxTexture, registry) -> {
			registry.register(new Identifier(ArcaneMagic.DOMAIN, "misc/glow_particle"));
		});

		ClientTickCallback.EVENT.register((client) -> {
			ParticleRenderer.INSTANCE.update();
		});
	}
}
