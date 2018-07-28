package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.BlockAltar;

import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.rift.listener.TileEntityTypeAdder;
import org.dimdev.rift.listener.client.TileEntityRendererAdder;

import java.util.Map;

public class ArcaneMagic implements BlockAdder, ItemAdder, TileEntityTypeAdder, TileEntityRendererAdder
{
    public static TileEntityType ALTAR_TE;
    public static final Block ALTAR = new BlockAltar();

    @Override
    public void registerBlocks()
    {
        Block.registerBlock(new ResourceLocation("arcanemagic", "altar"), ALTAR);
    }

    @Override
    public void registerItems()
    {
       Item.registerItemBlock(ALTAR, ItemGroup.MISC);
    }

    @Override
    public void registerTileEntityTypes()
    {
        ALTAR_TE = TileEntityType.registerTileEntityType("arcanemagic:altar", TileEntityType.Builder.create(TileEntityAltar::new));
    }

    @Override
    public void addTileEntityRenderers(Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers)
    {

    }
}
