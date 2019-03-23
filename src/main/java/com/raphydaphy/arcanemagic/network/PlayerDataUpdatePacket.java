package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class PlayerDataUpdatePacket implements IPacket
{
	public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "player_data_update");

	private CompoundTag data;

	private PlayerDataUpdatePacket()
	{
	}

	public PlayerDataUpdatePacket(CompoundTag data)
	{
		this.data = data;
	}

	@Override
	public void read(PacketByteBuf buf)
	{
		data = buf.readCompoundTag();
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeCompoundTag(data);
	}

	@Override
	public Identifier getID()
	{
		return ID;
	}

	public static class Handler extends MessageHandler<PlayerDataUpdatePacket>
	{
		@Override
		protected PlayerDataUpdatePacket create()
		{
			return new PlayerDataUpdatePacket();
		}

		@Override
		public void handle(PacketContext ctx, PlayerDataUpdatePacket message)
		{
			((DataHolder) MinecraftClient.getInstance().player).setAdditionalData(message.data);
		}
	}
}
