package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.entity.base.InventoryBlockEntity;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientBlockEntityUpdatePacket implements IPacket
{
	public static final Identifier ID = new Identifier(ArcaneMagic.DOMAIN, "client_block_entity_update");

	private CompoundTag tag;

	private ClientBlockEntityUpdatePacket()
	{
	}

	public ClientBlockEntityUpdatePacket(CompoundTag tag)
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

	public static class Handler extends MessageHandler<ClientBlockEntityUpdatePacket>
	{
		@Override
		protected ClientBlockEntityUpdatePacket create()
		{
			return new ClientBlockEntityUpdatePacket();
		}

		@Override
		public void handle(PacketContext ctx, ClientBlockEntityUpdatePacket message)
		{
			BlockPos pos = new BlockPos(message.tag.getInt("x"), message.tag.getInt("y"), message.tag.getInt("z"));
			World world = MinecraftClient.getInstance().player.world;

			if (world != null)
			{
				BlockEntity blockEntity = world.getBlockEntity(pos);

				if (blockEntity instanceof InventoryBlockEntity)
				{
					blockEntity.fromTag(message.tag);
				}
			}
		}
	}
}
