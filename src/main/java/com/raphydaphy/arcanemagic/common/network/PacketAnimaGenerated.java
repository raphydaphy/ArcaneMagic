package com.raphydaphy.arcanemagic.common.network;

import java.util.Random;

import com.raphydaphy.arcanemagic.common.anima.AnimaWorldData;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketAnimaGenerated implements IMessage
{
	public static Random random = new Random();
	int offX = 0, offZ = 0;

	public PacketAnimaGenerated() {
	        super();
	    }

	public PacketAnimaGenerated(int x, int z) {
	        super();
	        this.offX = x;
	        this.offZ = z;
	    }

	@Override
	public void fromBytes(ByteBuf buf)
	{
		offX = buf.readInt();
		offZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(offX);
		buf.writeInt(offZ);
	}

	public static class Handler implements IMessageHandler<PacketAnimaGenerated, IMessage>
	{
		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketAnimaGenerated message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				handle(message, ctx);
			});
			return null;
		}

		private void handle(PacketAnimaGenerated message, MessageContext ctx)
		{
			AnimaWorldData.AnimaGenerator.offsetX = message.offX;
			AnimaWorldData.AnimaGenerator.offsetZ = message.offZ;
		}
	}

}
