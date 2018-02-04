package com.raphydaphy.arcanemagic.common.network;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNotebookToastExpanded implements IMessage {
	private NotebookCategory cat;
	private String additionalTag;
	private boolean showIfFail;

	public PacketNotebookToastExpanded() {
	}

	public PacketNotebookToastExpanded(NotebookCategory cat, String additionalTag, boolean showIfFail) {
		this.cat = cat;
		this.additionalTag = additionalTag;
		this.showIfFail = showIfFail;
	}

	public static class Handler implements IMessageHandler<PacketNotebookToastExpanded, IMessage> {
		@Override
		public IMessage onMessage(PacketNotebookToastExpanded message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketNotebookToastExpanded message, MessageContext ctx) {
			INotebookInfo cap = Minecraft.getMinecraft().player.getCapability(INotebookInfo.CAP, null);

			if (cap != null) {
				boolean did = false;
				if (message.cat != null) {
					if (!cap.isUnlocked(message.cat.getRequiredTag())) {
						did = true;
						cap.setUnlocked(message.cat.getRequiredTag());
						ArcaneMagic.proxy.addCategoryUnlockToast(message.cat, false);
					}
					if (message.additionalTag != null && !cap.isUnlocked(message.additionalTag)) {
						did = true;
						cap.setUnlocked(message.cat.getRequiredTag());
						ArcaneMagic.proxy.addCategoryUnlockToast(message.cat, true);
					}
				}

				if (!did && message.showIfFail) {
					Minecraft.getMinecraft().ingameGUI.setOverlayMessage(
							TextFormatting.RED + I18n.format("arcanemagic.message.cantlearn"), false);
				}
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		int id = buf.readInt();
		if (id != -1) {
			cat = ArcaneMagicAPI.getNotebookCategories().get(id);
		} else {
			cat = null;
		}
		additionalTag = pbuf.readString(1000);
		showIfFail = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		if (cat != null) {
			buf.writeInt(ArcaneMagicAPI.getNotebookCategories().indexOf(cat));
		} else {
			buf.writeInt(-1);
		}
		pbuf.writeString(additionalTag);
		buf.writeBoolean(showIfFail);
	}
}