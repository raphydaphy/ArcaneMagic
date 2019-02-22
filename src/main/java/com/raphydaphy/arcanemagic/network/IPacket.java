package com.raphydaphy.arcanemagic.network;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public interface IPacket
{
	void read(PacketByteBuf buf);
	void write(PacketByteBuf buf);

	Identifier getID();
}
