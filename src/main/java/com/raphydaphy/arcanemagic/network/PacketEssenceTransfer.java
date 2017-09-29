package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.capabilities.EssenceStorage;
import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceStorage;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEssenceTransfer implements IMessage
{
	private BlockPos pos;
	private EssenceStack essence;
	
	public PacketEssenceTransfer()
	{
	}
	
	public PacketEssenceTransfer(BlockPos pos, EssenceStack essence)
	{
		this.pos = pos;
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
			
			TileEntity te = ctx.getServerHandler().player.world.getTileEntity(message.pos);
			if (te != null && te instanceof TileEntityEssenceStorage)
			{
				((TileEntityEssenceStorage)te).getCapability(EssenceStorage.CAP, null).store(message.essence, false);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		essence = EssenceStack.readFromBuf(buf);
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		EssenceStack.writeToBuf(buf, essence);
	}
}