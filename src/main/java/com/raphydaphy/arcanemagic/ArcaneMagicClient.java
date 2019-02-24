package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.entity.AltarBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.client.render.AltarRenderer;
import com.raphydaphy.arcanemagic.client.render.CrystalInfuserRenderer;
import com.raphydaphy.arcanemagic.client.render.TransfigurationTableRenderer;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

public class ArcaneMagicClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		BlockEntityRendererRegistry.INSTANCE.register(AltarBlockEntity.class, new AltarRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(CrystalInfuserBlockEntity.class, new CrystalInfuserRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(TransfigurationTableBlockEntity.class, new TransfigurationTableRenderer());

		ClientSidePacketRegistry.INSTANCE.register(ClientBlockEntityUpdatePacket.ID, new ClientBlockEntityUpdatePacket.Handler());
	}
}
