package com.raphydaphy.arcanemagic.handler;

import com.raphydaphy.arcanemagic.network.PacketArcaneCraftingSync;
import com.raphydaphy.arcanemagic.network.PacketEssenceTransfer;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ArcaneMagicPacketHandler
{
	private static int packetId = 0;

	public static SimpleNetworkWrapper INSTANCE = null;

	public ArcaneMagicPacketHandler()
	{
	}

	public static void registerMessages(String channelName)
	{
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}

	public static void registerMessages()
	{
		// Register messages which are sent from the client to the server here:
		INSTANCE.registerMessage(PacketArcaneCraftingSync.Handler.class, PacketArcaneCraftingSync.class, packetId++,
				Side.SERVER);
		
		INSTANCE.registerMessage(PacketEssenceTransfer.Handler.class, PacketEssenceTransfer.class, packetId++, Side.CLIENT);
	}
}