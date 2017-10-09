package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.capabilities.NotebookInfo;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * There's really no reason to validate this info, 
 * the book will be reset to the first category 
 * if the player tires to choose a category that 
 * dosen't exist or hasn't been unlocked yet
*/
public class PacketNotebookInfo implements IMessage
{
	private int category;
	private int page;
	private int indexPage;
	private boolean usedNotebook;

	public PacketNotebookInfo()
	{
	}

	public PacketNotebookInfo(NotebookInfo cap)
	{
		this.category = cap.getCategory();
		this.page = cap.getPage();
		this.indexPage = cap.getIndexPage();
		this.usedNotebook = cap.getUsed();
	}

	public static class Handler implements IMessageHandler<PacketNotebookInfo, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNotebookInfo message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketNotebookInfo message, MessageContext ctx)
		{
			// Server-side only
			NotebookInfo cap = ctx.getServerHandler().player.getCapability(NotebookInfo.CAP, null);
			
			cap.setCategory(message.category);
			cap.setPage(message.page);
			cap.setIndexPage(message.indexPage);
			cap.setUsed(message.usedNotebook);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		category = buf.readInt();
		page = buf.readInt();
		indexPage = buf.readInt();
		usedNotebook = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(category);
		buf.writeInt(page);
		buf.writeInt(indexPage);
		buf.writeBoolean(usedNotebook);
	}
}