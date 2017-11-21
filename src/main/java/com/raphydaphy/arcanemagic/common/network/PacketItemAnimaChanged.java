package com.raphydaphy.arcanemagic.common.network;

import java.io.IOException;

import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemAnimaChanged implements IMessage
{
	private NBTTagCompound cap;
	private int slot;
	private ItemStack stack;

	public PacketItemAnimaChanged()
	{
	}

	public PacketItemAnimaChanged(IAnimaStorage cap, int slot, ItemStack stack)
	{
		this.cap = cap.serializeNBT();
		this.slot = slot;
		this.stack = stack;
	}

	public static class Handler implements IMessageHandler<PacketItemAnimaChanged, IMessage>
	{
		@Override
		public IMessage onMessage(PacketItemAnimaChanged message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketItemAnimaChanged message, MessageContext ctx)
		{

			//ItemStack inSlot = Minecraft.getMinecraft().player.inventory.getStackInSlot(message.slot);
			IAnimaStorage playerCap = message.stack.getCapability(IAnimaStorage.CAP, null);
			if (playerCap != null)
			{
				playerCap.deserializeNBT(message.cap);
				System.out.println("deserialised successfully!");
			}
			System.out.println("does cap exist? " + message.stack.hasCapability(IAnimaStorage.CAP, null));

			Minecraft.getMinecraft().player.inventory.setInventorySlotContents(message.slot, message.stack);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		PacketBuffer pbuf = new PacketBuffer(buf);
		slot = pbuf.readInt();
		try
		{
			cap = pbuf.readCompoundTag();
			stack = pbuf.readItemStack();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		PacketBuffer pbuf = new PacketBuffer(buf);
		pbuf.writeInt(slot);
		pbuf.writeCompoundTag(cap);
		pbuf.writeItemStack(stack);

	}
}