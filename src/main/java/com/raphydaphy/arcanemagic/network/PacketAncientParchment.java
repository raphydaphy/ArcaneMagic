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

        // First time reading ancient parchment
        if (player.getStatFile().readStat(StatList.OBJECT_USE_STATS.addStat(ArcaneMagic.ANCIENT_PARCHMENT)) == 1)
        {
            // Already read a written parchment
            if (player.getStatFile().readStat(StatList.OBJECT_USE_STATS.addStat(ArcaneMagic.WRITTEN_PARCHMENT)) > 0)
            {
                player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.ANCIENT_PARCHMENT_LATER).setStyle(new Style().setItalic(true)), true);
            }
            // Opened ancient parchment before written
            else
            {
                player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.ANCIENT_PARCHMENT_START).setStyle(new Style().setItalic(true)), false);
            }
        }
    }
}
