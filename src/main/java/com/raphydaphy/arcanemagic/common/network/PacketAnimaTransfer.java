package com.raphydaphy.arcanemagic.common.network;

import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAnimaTransfer implements IMessage
{
	private Vec3d from;
	private Vec3d to;
	private Vec3d toCosmetic;
	private AnimaStack anima;
	private boolean spawnParticles;

	public PacketAnimaTransfer()
	{
	}

	public PacketAnimaTransfer(AnimaStack anima, Vec3d from, Vec3d to, Vec3d toCosmetic, boolean spawnParticles)
	{
		this.from = from;
		this.to = to;
		this.toCosmetic = toCosmetic;
		this.anima = anima;
	}

	public static class Handler implements IMessageHandler<PacketAnimaTransfer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketAnimaTransfer message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketAnimaTransfer message, MessageContext ctx)
		{
			ArcaneMagic.proxy.sendAnimaSafe(message.anima, message.from, message.to, message.toCosmetic,
					message.spawnParticles);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		from = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		to = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		toCosmetic = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		anima = AnimaStack.readFromBuf(buf);
		spawnParticles = buf.readBoolean();
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
		buf.writeDouble(toCosmetic.x);
		buf.writeDouble(toCosmetic.y);
		buf.writeDouble(toCosmetic.z);
		AnimaStack.writeToBuf(buf, anima);
		buf.writeBoolean(spawnParticles);
	}
}