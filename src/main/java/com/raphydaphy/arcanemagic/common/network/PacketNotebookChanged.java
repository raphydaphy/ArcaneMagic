package com.raphydaphy.arcanemagic.common.network;

import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

// FOR WHEN THE CLIENT CLICKS A BUTTON IN THE NOTEBOOK
// SENT TO THE SERVER
public class PacketNotebookChanged implements IMessage
{
	private int category;
	private int page;
	private int indexPage;
	private boolean usedNotebook;

	public PacketNotebookChanged()
	{
	}

	public PacketNotebookChanged(NotebookInfo cap)
	{
		this.category = cap.getCategory();
		this.page = cap.getPage();
		this.indexPage = cap.getIndexPage();
		this.usedNotebook = cap.getUsed();
	}

	public static class Handler implements IMessageHandler<PacketNotebookChanged, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNotebookChanged message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler)
					.addScheduledTask(() -> handleNotebookInfo(message, ctx, ctx.side));
			return null;
		}

		public void handleNotebookInfo(PacketNotebookChanged message, MessageContext ctx, Side side)
		{
			NotebookInfo cap = ctx.getServerHandler().player.getCapability(NotebookInfo.CAP, null);

			if (cap != null)
			{
				cap.setCategory(message.category);
				cap.setPage(message.page);
				cap.setIndexPage(message.indexPage);
				cap.setUsed(message.usedNotebook);
			}
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