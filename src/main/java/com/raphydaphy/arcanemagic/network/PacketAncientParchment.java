package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;

public class PacketAncientParchment implements Packet<INetHandlerPlayServer>
{
    public PacketAncientParchment() { }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) { }

    @Override
    public void writePacketData(PacketBuffer buf) { }

    @Override
    public void processPacket(INetHandlerPlayServer server)
    {
        EntityPlayerMP player = ((NetHandlerPlayServer)server).player;
        if (player.getStatFile().readStat(StatList.OBJECT_USE_STATS.addStat(ArcaneMagic.ANCIENT_PARCHMENT)) == 1)
        {
            player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.ANCIENT_PARCHMENT_LEARNED).setStyle(new Style().setItalic(true)), false);
        }
    }
}
