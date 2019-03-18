package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ModSounds;
import com.raphydaphy.arcanemagic.util.TremorTracker;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class TremorPacket implements IPacket
{
	public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "tremor");

	private int delay;
	private int duration;

	private TremorPacket()
	{
	}

	public TremorPacket(int delay, int duration)
	{
		this.delay = delay;
		this.duration = duration;
	}

	@Override
	public void read(PacketByteBuf buf)
	{
		delay = buf.readInt();
		duration = buf.readInt();
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(delay);
		buf.writeInt(duration);
	}

	@Override
	public Identifier getID()
	{
		return ID;
	}

	public static class Handler extends MessageHandler<TremorPacket>
	{
		@Override
		protected TremorPacket create()
		{
			return new TremorPacket();
		}

		@Override
		public void handle(PacketContext ctx, TremorPacket message)
		{
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (message.duration > 0)
			{
				TremorTracker.scheduleTremor(message.delay, message.duration);
				player.world.playSound(player, player.getPos(), ModSounds.VOID_GROWL, SoundCategory.AMBIENT, 1, 1);
			} else
			{
				player.world.playSound(player, player.getPos(), ModSounds.VOID_ATMOSPHERE, SoundCategory.AMBIENT, 1, 1);
			}
		}
	}
}
