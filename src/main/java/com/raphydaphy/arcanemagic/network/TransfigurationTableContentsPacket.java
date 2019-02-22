package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransfigurationTableContentsPacket implements IPacket
{
	public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "transfiguration_table_contents");

	private CompoundTag tag;

	public TransfigurationTableContentsPacket()
	{
	}

	public TransfigurationTableContentsPacket(CompoundTag tag)
	{
		this.tag = tag;
	}

	@Override
	public void read(PacketByteBuf buf)
	{
		tag = buf.readCompoundTag();
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeCompoundTag(tag);
	}

	@Override
	public Identifier getID()
	{
		return ID;
	}

	public static class Handler extends MessageHandler<TransfigurationTableContentsPacket>
	{
		@Override
		protected TransfigurationTableContentsPacket create()
		{
			return new TransfigurationTableContentsPacket();
		}

		@Override
		public void handle(PacketContext ctx, TransfigurationTableContentsPacket message)
		{
			BlockPos pos = new BlockPos(message.tag.getInt("x"), message.tag.getInt("y"), message.tag.getInt("z"));
			World world = MinecraftClient.getInstance().player.world;

			if (world != null)
			{
				BlockEntity blockEntity = world.getBlockEntity(pos);

				if (blockEntity instanceof TransfigurationTableBlockEntity)
				{
					blockEntity.fromTag(message.tag);
				}
			}
		}
	}
}
