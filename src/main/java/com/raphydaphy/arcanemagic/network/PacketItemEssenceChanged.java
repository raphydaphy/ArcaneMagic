package com.raphydaphy.arcanemagic.network;

import java.io.IOException;

import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemEssenceChanged implements IMessage
{
	private NBTTagCompound cap;

	public PacketItemEssenceChanged()
	{
	}

	public PacketItemEssenceChanged(IEssenceStorage cap)
	{
		this.cap = cap.serializeNBT();
	}

	public static class Handler implements IMessageHandler<PacketItemEssenceChanged, IMessage>
	{
		@Override
		public IMessage onMessage(PacketItemEssenceChanged message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketItemEssenceChanged message, MessageContext ctx)
		{
			IEssenceStorage playerCap = Minecraft.getMinecraft().player.getCapability(IEssenceStorage.CAP, null);
			if (playerCap != null)
			{
				playerCap.deserializeNBT(message.cap);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		PacketBuffer pbuf = new PacketBuffer(buf);

		try
		{
			cap = pbuf.readCompoundTag();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		PacketBuffer pbuf = new PacketBuffer(buf);

		pbuf.writeCompoundTag(cap);

	}
}