package com.raphydaphy.arcanemagic.common.network;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNotebookToast implements IMessage
{
	private NotebookCategory cat;

	public PacketNotebookToast()
	{
	}

	public PacketNotebookToast(NotebookCategory cat)
	{
		this.cat = cat;
	}

	public static class Handler implements IMessageHandler<PacketNotebookToast, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNotebookToast message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketNotebookToast message, MessageContext ctx)
		{
			ArcaneMagic.proxy.addCategoryUnlockToast(message.cat);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		cat = ArcaneMagicAPI.getNotebookCategories().get(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(ArcaneMagicAPI.getNotebookCategories().indexOf(cat));
	}
}