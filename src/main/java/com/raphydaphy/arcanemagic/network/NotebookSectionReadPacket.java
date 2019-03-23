package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import com.raphydaphy.cutsceneapi.network.IPacket;
import com.raphydaphy.cutsceneapi.network.MessageHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class NotebookSectionReadPacket implements IPacket
{
	public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "notebook_section_read");

	private String sectionID;

	private NotebookSectionReadPacket()
	{
	}

	public NotebookSectionReadPacket(INotebookSection section)
	{
		this.sectionID = section.getID().toString();
	}

	@Override
	public void read(PacketByteBuf buf)
	{
		sectionID = buf.readString(buf.readInt());
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(sectionID.length());
		buf.writeString(sectionID);
	}

	@Override
	public Identifier getID()
	{
		return ID;
	}

	public static class Handler extends MessageHandler<NotebookSectionReadPacket>
	{
		@Override
		protected NotebookSectionReadPacket create()
		{
			return new NotebookSectionReadPacket();
		}

		@Override
		public void handle(PacketContext ctx, NotebookSectionReadPacket message)
		{
			ArcaneMagicUtils.updateNotebookSection(ctx.getPlayer().world, (DataHolder) ctx.getPlayer(), message.sectionID, true);
		}
	}
}
