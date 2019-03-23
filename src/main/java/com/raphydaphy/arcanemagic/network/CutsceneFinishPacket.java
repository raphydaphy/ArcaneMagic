package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.cutscene.CutsceneManager;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class CutsceneFinishPacket implements IPacket
{
	public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "cutscene_finish");

	public CutsceneFinishPacket()
	{

	}

	@Override
	public void read(PacketByteBuf buf)
	{
	}

	@Override
	public void write(PacketByteBuf buf)
	{
	}

	@Override
	public Identifier getID()
	{
		return ID;
	}

	public static class Handler extends MessageHandler<CutsceneFinishPacket>
	{
		@Override
		protected CutsceneFinishPacket create()
		{
			return new CutsceneFinishPacket();
		}

		@Override
		public void handle(PacketContext ctx, CutsceneFinishPacket message)
		{
			CutsceneManager.finishServer(ctx.getPlayer());
		}
	}
}
