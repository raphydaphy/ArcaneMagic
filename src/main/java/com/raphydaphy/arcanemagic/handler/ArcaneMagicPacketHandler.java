package com.raphydaphy.arcanemagic.handler;

import com.raphydaphy.arcanemagic.network.PacketEssenceTransfer;
import com.raphydaphy.arcanemagic.network.PacketItemEssenceChanged;
import com.raphydaphy.arcanemagic.network.PacketNotebookChanged;
import com.raphydaphy.arcanemagic.network.PacketNotebookOpened;

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
		// Client - > Server

		INSTANCE.registerMessage(PacketNotebookChanged.Handler.class, PacketNotebookChanged.class, packetId++, Side.SERVER);

		// Server -> Client

		INSTANCE.registerMessage(PacketEssenceTransfer.Handler.class, PacketEssenceTransfer.class, packetId++,
				Side.CLIENT);
		INSTANCE.registerMessage(PacketItemEssenceChanged.Handler.class, PacketItemEssenceChanged.class, packetId++,
				Side.CLIENT);
		
		INSTANCE.registerMessage(PacketNotebookOpened.Handler.class, PacketNotebookOpened.class, packetId++, Side.CLIENT);
	}
}