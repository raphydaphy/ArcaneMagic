package com.raphydaphy.arcanemagic.common.handler;

import com.raphydaphy.arcanemagic.common.network.PacketAnimaTransfer;
import com.raphydaphy.arcanemagic.common.network.PacketItemAnimaChanged;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookChanged;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookOpened;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookToastExpanded;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookToastOrFail;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ArcaneMagicPacketHandler {
	private static int packetId = 0;

	public static SimpleNetworkWrapper INSTANCE = null;

	public ArcaneMagicPacketHandler() {
	}

	public static void registerMessages(String channelName) {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}

	public static void registerMessages() {
		// Client - > Server

		INSTANCE.registerMessage(PacketNotebookChanged.Handler.class, PacketNotebookChanged.class, packetId++,
				Side.SERVER);

		// Server -> Client

		INSTANCE.registerMessage(PacketAnimaTransfer.Handler.class, PacketAnimaTransfer.class, packetId++, Side.CLIENT);
		INSTANCE.registerMessage(PacketItemAnimaChanged.Handler.class, PacketItemAnimaChanged.class, packetId++,
				Side.CLIENT);
		INSTANCE.registerMessage(PacketNotebookOpened.Handler.class, PacketNotebookOpened.class, packetId++,
				Side.CLIENT);
		INSTANCE.registerMessage(PacketNotebookToastOrFail.Handler.class, PacketNotebookToastOrFail.class, packetId++,
				Side.CLIENT);
		INSTANCE.registerMessage(PacketNotebookToastExpanded.Handler.class, PacketNotebookToastExpanded.class,
				packetId++, Side.CLIENT);
	}
}