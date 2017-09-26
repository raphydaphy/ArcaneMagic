package com.raphydaphy.arcanemagic.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketArcaneCraftingSync implements IMessage
{
	public PacketArcaneCraftingSync() 
	{
    }

    public static class Handler implements IMessageHandler<PacketArcaneCraftingSync, IMessage> 
    {
        @Override
        public IMessage onMessage(PacketArcaneCraftingSync message, MessageContext ctx) 
        {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketArcaneCraftingSync message, MessageContext ctx) 
        {
        	Container container = ctx.getServerHandler().player.openContainer;
    		if (container != null)
    		{
    			container.onCraftMatrixChanged(null);
    		}
        }
    }

	@Override
	public void fromBytes(ByteBuf buf)
	{
		// no data, yay
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		// no data, yay
	}
}