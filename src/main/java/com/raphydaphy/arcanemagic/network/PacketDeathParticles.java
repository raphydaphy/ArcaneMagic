package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.client.particle.ParticleAnimaDeath;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class PacketDeathParticles implements Packet<INetHandlerPlayClient>
{
    private double x;
    private double y;
    private double z;
    private float width;
    private float height;
    private BlockPos altar;

    public PacketDeathParticles() { }

    public PacketDeathParticles(double x, double y, double z, float width, float height, BlockPos pos)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.altar = pos;
    }

    @Override
    public void readPacketData(PacketBuffer buf)
    {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        width = buf.readFloat();
        height = buf.readFloat();
        if (buf.readBoolean())
        {
            altar = BlockPos.fromLong(buf.readLong());
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf)
    {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(width);
        buf.writeFloat(height);
        buf.writeBoolean(altar != null);
        if (altar != null)
        {
            buf.writeLong(altar.toLong());
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayServer)
    {
        System.out.println(x + ", " + y + ", " + z + ", " + width + ", " + height + ", " + altar);
        particles();
    }

    private void particles()
    {
        Random rand = Minecraft.getMinecraft().world.rand;
        for(int i = 0; i < 20; i++)
        {
            double speedX = -rand.nextGaussian() * 0.02D;
            double speedY = -rand.nextGaussian() * 0.02D;
            double speedZ = -rand.nextGaussian() * 0.02D;
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleAnimaDeath(Minecraft.getMinecraft().world, altar, x + (double)(rand.nextFloat() * width * 2.0F) - (double)width, y + (double)(rand.nextFloat() * height), z + (double)(rand.nextFloat() * width * 2.0F) - (double)width, speedX, speedY, speedZ));
        }
    }
}
