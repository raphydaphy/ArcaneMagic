package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class NotebookUpdatePacket implements IPacket
{
	public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "notebook_update");

	private String section;
	private int page;
	private int contentsPage;

	private NotebookUpdatePacket()
	{
	}

	public NotebookUpdatePacket(String section, int page, int contentsPage)
	{
		this.section = section;
		this.page = page;
		this.contentsPage = contentsPage;
	}

	@Override
	public void read(PacketByteBuf buf)
	{
		section = buf.readString(buf.readInt());
		page = buf.readInt();
		contentsPage = buf.readInt();
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(section.length());
		buf.writeString(section);
		buf.writeInt(page);
		buf.writeInt(contentsPage);
	}

	@Override
	public Identifier getID()
	{
		return ID;
	}

	public static class Handler extends MessageHandler<NotebookUpdatePacket>
	{
		@Override
		protected NotebookUpdatePacket create()
		{
			return new NotebookUpdatePacket();
		}

		@Override
		public void handle(PacketContext ctx, NotebookUpdatePacket message)
		{
			ItemStack stack = ctx.getPlayer().getMainHandStack();
			if (stack.getItem() != ModRegistry.NOTEBOOK)
			{
				stack = ctx.getPlayer().getOffHandStack();
			}

			if (stack.getItem() == ModRegistry.NOTEBOOK)
			{
				stack.getOrCreateTag().putString(ArcaneMagicConstants.NOTEBOOK_SECTION_KEY, message.section);
				stack.getOrCreateTag().putInt(ArcaneMagicConstants.NOTEBOOK_PAGE_KEY, message.page);
				stack.getOrCreateTag().putInt(ArcaneMagicConstants.NOTEBOOK_CONTENTS_PAGE_KEY, message.contentsPage);
			}
		}
	}
}
