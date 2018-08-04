package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemParchment extends Item
{
    public ItemParchment()
    {
        super(new Item.Builder().group(ItemGroup.MISC));
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            onCreatedServer(player);
        }
    }

    private void onCreatedServer(EntityPlayer player)
    {
        int total = ((EntityPlayerMP)player).getStatFile().readStat(StatList.CRAFTS_STATS.addStat(this));
        if (total == 1)
        {
            player.sendMessage(new TextComponentTranslation(ArcaneMagicResources.PARCHMENT_FIRST_CRAFT).setStyle(new Style().setItalic(true)));
        }
    }
}
