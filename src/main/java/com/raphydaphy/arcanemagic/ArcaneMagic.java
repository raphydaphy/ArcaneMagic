package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.BlockAltar;
import com.raphydaphy.arcanemagic.block.BlockInductor;
import com.raphydaphy.arcanemagic.block.BlockPedestal;

import com.raphydaphy.arcanemagic.client.render.PedestalRenderer;
import com.raphydaphy.arcanemagic.item.ItemNotebook;
import com.raphydaphy.arcanemagic.item.ItemParchment;
import com.raphydaphy.arcanemagic.item.ItemWrittenParchment;
import com.raphydaphy.arcanemagic.network.PacketDeathParticles;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.tileentity.TileEntityPedestal;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.rift.listener.PacketAdder;
import org.dimdev.rift.listener.TileEntityTypeAdder;
import org.dimdev.rift.listener.client.TileEntityRendererAdder;

import java.util.Map;

public class ArcaneMagic implements BlockAdder, ItemAdder, TileEntityTypeAdder, TileEntityRendererAdder, PacketAdder
{
    public static TileEntityType ALTAR_TE;
    public static TileEntityType PEDESTAL_TE;

    public static final Block ALTAR = new BlockAltar(Block.Builder.create(Material.ROCK).hardnessAndResistance(5, 1200).soundType(SoundType.STONE));
    private static final Block INDUCTOR = new BlockInductor(Block.Builder.create(Material.WOOD).hardnessAndResistance(2, 500).soundType(SoundType.STONE));
    private static final Block PEDESTAL = new BlockPedestal(Block.Builder.create(Material.WOOD).hardnessAndResistance(2.0F, 500.0F).soundType(SoundType.WOOD));

    public static final Item PARCHMENT = new ItemParchment();
    public static final Item WRITTEN_PARCHMENT = new ItemWrittenParchment(false);
    private static final Item ANCIENT_PARCHMENT = new ItemWrittenParchment(true);
    private static final Item NOTEBOOK = new ItemNotebook();

    @Override
    public void registerBlocks()
    {
        Block.registerBlock(new ResourceLocation(ArcaneMagicResources.MOD_ID, "altar"), ALTAR);
        Block.registerBlock(new ResourceLocation(ArcaneMagicResources.MOD_ID, "inductor"), INDUCTOR);
        Block.registerBlock(new ResourceLocation(ArcaneMagicResources.MOD_ID, "pedestal"), PEDESTAL);
    }

    @Override
    public void registerItems()
    {
        Item.registerItemBlock(ALTAR, ItemGroup.MISC);
        Item.registerItemBlock(INDUCTOR, ItemGroup.MISC);
        Item.registerItemBlock(PEDESTAL, ItemGroup.MISC);

        Item.registerItem(new ResourceLocation(ArcaneMagicResources.MOD_ID, "parchment"), PARCHMENT);
        Item.registerItem(new ResourceLocation(ArcaneMagicResources.MOD_ID, "parchment_written"), WRITTEN_PARCHMENT);
        Item.registerItem(new ResourceLocation(ArcaneMagicResources.MOD_ID, "parchment_ancient"), ANCIENT_PARCHMENT);
        Item.registerItem(new ResourceLocation(ArcaneMagicResources.MOD_ID, "notebook"), NOTEBOOK);
    }

    @Override
    public void registerTileEntityTypes()
    {
        ALTAR_TE = TileEntityType.registerTileEntityType("arcanemagic:altar", TileEntityType.Builder.create(TileEntityAltar::new));
        PEDESTAL_TE = TileEntityType.registerTileEntityType("arcanemagic:pedestal", TileEntityType.Builder.create(TileEntityPedestal::new));
    }

    @Override
    public void addTileEntityRenderers(Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers)
    {
        renderers.put(TileEntityPedestal.class, new PedestalRenderer());
    }

    @Override
    public void registerHandshakingPackets(PacketRegistrationReceiver receiver)
    {

    }

    @Override
    public void registerPlayPackets(PacketRegistrationReceiver receiver)
    {
        receiver.registerPacket(EnumPacketDirection.CLIENTBOUND, PacketDeathParticles.class);
    }

    @Override
    public void registerStatusPackets(PacketRegistrationReceiver receiver)
    {

    }

    @Override
    public void registerLoginPackets(PacketRegistrationReceiver receiver)
    {

    }
}
