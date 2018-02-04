package com.raphydaphy.arcanemagic.common.network;

import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

// FOR WHEN THE CLIENT CLICKS A BUTTON IN THE NOTEBOOK
// SENT TO THE SERVER
public class PacketNotebookChanged implements IMessage {
	private int category;
	private int page;
	private int indexPage;
	private boolean usedNotebook;
	private String searchKey;

	public PacketNotebookChanged() {
	}

	public PacketNotebookChanged(INotebookInfo cap) {
		this.category = cap.getCategory();
		this.page = cap.getPage();
		this.indexPage = cap.getIndexPage();
		this.usedNotebook = cap.getUsed();
		this.searchKey = cap.getSearchKey();
	}

	public static class Handler implements IMessageHandler<PacketNotebookChanged, IMessage> {
		@Override
		public IMessage onMessage(PacketNotebookChanged message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler)
					.addScheduledTask(() -> handleNotebookInfo(message, ctx, ctx.side));
			return null;
		}

		public void handleNotebookInfo(PacketNotebookChanged message, MessageContext ctx, Side side) {
			INotebookInfo cap = ctx.getServerHandler().player.getCapability(INotebookInfo.CAP, null);

			if (cap != null) {
				cap.setCategory(message.category);
				cap.setPage(message.page);
				cap.setIndexPage(message.indexPage);
				cap.setUsed(message.usedNotebook);
				cap.setSearchKey(message.searchKey);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		category = pbuf.readInt();
		page = pbuf.readInt();
		indexPage = pbuf.readInt();
		usedNotebook = pbuf.readBoolean();
		if (pbuf.readBoolean()) {
			searchKey = pbuf.readString(1024);
		} else {
			searchKey = "";
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		buf.writeInt(category);
		buf.writeInt(page);
		buf.writeInt(indexPage);
		buf.writeBoolean(usedNotebook);
		if (searchKey != null && !searchKey.isEmpty()) {
			pbuf.writeBoolean(true);
			pbuf.writeString(searchKey);
		} else {
			pbuf.writeBoolean(false);
		}
	}
}