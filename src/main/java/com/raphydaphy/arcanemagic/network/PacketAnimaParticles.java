package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.tileentity.TileEntityInductor;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class PacketAnimaParticles implements Packet<INetHandlerPlayClient>
{
    private BlockPos from;
    private BlockPos to;

    public PacketAnimaParticles() { }

    public PacketAnimaParticles(BlockPos from, BlockPos to)
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public void readPacketData(PacketBuffer buf)
    {
        from = BlockPos.fromLong(buf.readLong());
        to = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void writePacketData(PacketBuffer buf)
    {
        buf.writeLong(from.toLong());
        buf.writeLong(to.toLong());
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayServer)
    {
        TileEntity te = Minecraft.getMinecraft().world.getTileEntity(to);
        if (te instanceof TileEntityInductor)
        {
            ((TileEntityInductor)te).animaParticlePacket(from);
        }
    }
}
