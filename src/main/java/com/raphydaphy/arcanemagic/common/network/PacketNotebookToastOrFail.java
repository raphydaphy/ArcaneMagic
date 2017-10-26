package com.raphydaphy.arcanemagic.common.network;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNotebookToastOrFail implements IMessage
{
	private NotebookCategory cat;
	private boolean showIfFail;

	public PacketNotebookToastOrFail()
	{
	}

	public PacketNotebookToastOrFail(NotebookCategory cat, boolean showIfFail)
	{
		this.cat = cat;
		this.showIfFail = showIfFail;

		if (cat != null && cat.equals(NotebookCategories.UNKNOWN_REALMS))
		{
			cat = null;
		}
	}

	public static class Handler implements IMessageHandler<PacketNotebookToastOrFail, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNotebookToastOrFail message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketNotebookToastOrFail message, MessageContext ctx)
		{
			NotebookInfo cap = Minecraft.getMinecraft().player.getCapability(NotebookInfo.CAP, null);

			if (cap != null)
			{
				if (message.cat != null && !cap.isUnlocked(message.cat.getRequiredTag()))
				{
					cap.setUnlocked(message.cat.getRequiredTag());
					ArcaneMagic.proxy.addCategoryUnlockToast(message.cat, false);
				} else if (message.showIfFail)
				{
					Minecraft.getMinecraft().ingameGUI.setOverlayMessage(
							TextFormatting.RED + I18n.format("arcanemagic.message.cantlearn"), false);
				}
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int id = buf.readInt();
		if (id != -1)
		{
			cat = ArcaneMagicAPI.getNotebookCategories().get(id);
		} else
		{
			cat = null;
		}
		showIfFail = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		if (cat != null)
		{
			buf.writeInt(ArcaneMagicAPI.getNotebookCategories().indexOf(cat));
		} else
		{
			buf.writeInt(-1);
		}
		buf.writeBoolean(showIfFail);
	}
}