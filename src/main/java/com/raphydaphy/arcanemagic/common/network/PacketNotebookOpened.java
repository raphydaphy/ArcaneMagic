package com.raphydaphy.arcanemagic.common.network;

import java.io.IOException;

import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

// TO CLIENT PACKET SENT WHEN THE NOTEBOOK IS OPENED
// USED TO SYNC THE SERVER INFO TO THE CLIENT
public class PacketNotebookOpened implements IMessage
{
	private NBTTagCompound notebookInfo;

	public PacketNotebookOpened()
	{
	}

	public PacketNotebookOpened(INotebookInfo cap)
	{
		this.notebookInfo = cap.serializeNBT();
	}

	public static class Handler implements IMessageHandler<PacketNotebookOpened, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNotebookOpened message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler)
					.addScheduledTask(() -> handleNotebookInfo(message, ctx, ctx.side));
			return null;
		}

		public void handleNotebookInfo(PacketNotebookOpened message, MessageContext ctx, Side side)
		{
			INotebookInfo cap = Minecraft.getMinecraft().player.getCapability(INotebookInfo.CAP, null);
			if (cap != null)
			{
				cap.deserializeNBT(message.getCompound());
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		PacketBuffer pbuf = new PacketBuffer(buf);

		try
		{
			notebookInfo = pbuf.readCompoundTag();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		PacketBuffer pbuf = new PacketBuffer(buf);

		pbuf.writeCompoundTag(notebookInfo);
	}

	public NBTTagCompound getCompound()
	{
		return notebookInfo;
	}
}