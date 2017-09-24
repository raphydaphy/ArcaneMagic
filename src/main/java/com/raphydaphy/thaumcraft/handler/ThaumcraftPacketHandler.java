package com.raphydaphy.thaumcraft.handler;

import com.raphydaphy.thaumcraft.network.PacketArcaneCraftingSync;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ThaumcraftPacketHandler
{
	private static int packetId = 0;

	public static SimpleNetworkWrapper INSTANCE = null;

	public ThaumcraftPacketHandler()
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
	}
}