package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEssenceTransfer implements IMessage
{
	private Vec3d from;
	private Vec3d to;
	private EssenceStack essence;
	
	public PacketEssenceTransfer()
	{
	}
	
	public PacketEssenceTransfer(EssenceStack essence, Vec3d from, Vec3d to)
	{
		this.from = from;
		this.to = to;
		this.essence = essence;
	}

	public static class Handler implements IMessageHandler<PacketEssenceTransfer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketEssenceTransfer message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketEssenceTransfer message, MessageContext ctx)
		{
			Essence.sendEssence(Minecraft.getMinecraft().world, message.essence, message.from, message.to, false);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		from = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		to = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		essence = EssenceStack.readFromBuf(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{	
		buf.writeDouble(from.x);
		buf.writeDouble(from.y);
		buf.writeDouble(from.z);
		buf.writeDouble(to.x);
		buf.writeDouble(to.y);
		buf.writeDouble(to.z);
		EssenceStack.writeToBuf(buf, essence);
	}
}