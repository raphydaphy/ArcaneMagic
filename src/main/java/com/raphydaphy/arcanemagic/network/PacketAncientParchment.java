package com.raphydaphy.arcanemagic.network;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.IParchment;
import com.raphydaphy.arcanemagic.parchment.ParchmentDrownedDiscovery;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Objects;

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
                for (ItemStack stack : player.inventoryContainer.getInventory())
                {
                    if (stack.getItem() == ArcaneMagic.WRITTEN_PARCHMENT)
                    {
                        IParchment parchment = ParchmentRegistry.getParchment(stack);
                        if (parchment instanceof ParchmentDrownedDiscovery)
                        {
                            Objects.requireNonNull(stack.getTagCompound()).setBoolean(ParchmentDrownedDiscovery.FOUND_WIZARD_HUT, true);
                            player.openContainer.detectAndSendChanges();
                            return;
                        }
                    }
                }
            }
            // Opened ancient parchment before written
            else
            {
                player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.ANCIENT_PARCHMENT_START).setStyle(new Style().setItalic(true)), false);
            }
        }
        // They needed to re-read the parchment to progress in drowned discovery TODO: use entitydata for this
        else if (player.getStatFile().readStat(StatList.BROKEN.addStat(ArcaneMagic.ANCIENT_PARCHMENT)) == 1)
        {
            player.sendStatusMessage(new TextComponentTranslation(ArcaneMagicResources.ANCIENT_PARCHMENT_LATER).setStyle(new Style().setItalic(true)), true);
            // unlockAchievement = set statistic to a number
            player.getStatFile().unlockAchievement(player, StatList.BROKEN.addStat(ArcaneMagic.ANCIENT_PARCHMENT), 2);
        }
    }
}
