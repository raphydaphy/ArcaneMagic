package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.BlockAltar;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.rift.listener.ItemAdder;

public class ArcaneMagic implements BlockAdder, ItemAdder
{
    public static final Block ALTAR = new BlockAltar();

    @Override
    public void registerBlocks()
    {
        Block.registerBlock(new ResourceLocation("arcanemagic", "altar"), ALTAR);
    }

    @Override
    public void registerItems()
    {
       Item.registerItemBlock(ALTAR, ItemGroup.BUILDING_BLOCKS);
    }
}
