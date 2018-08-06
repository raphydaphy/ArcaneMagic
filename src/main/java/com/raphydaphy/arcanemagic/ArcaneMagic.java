package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.BlockAltar;
import com.raphydaphy.arcanemagic.block.BlockInductor;
import com.raphydaphy.arcanemagic.block.BlockPedestal;

import com.raphydaphy.arcanemagic.client.render.PedestalRenderer;
import com.raphydaphy.arcanemagic.item.ItemLinkingRod;
import com.raphydaphy.arcanemagic.item.ItemNotebook;
import com.raphydaphy.arcanemagic.item.ItemParchment;
import com.raphydaphy.arcanemagic.item.ItemWrittenParchment;
import com.raphydaphy.arcanemagic.network.PacketDeathParticles;
import com.raphydaphy.arcanemagic.structure.WizardHutConfig;
import com.raphydaphy.arcanemagic.structure.WizardHutPieces;
import com.raphydaphy.arcanemagic.structure.WizardHutStructure;
import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.tileentity.TileEntityInductor;
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

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureIO;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.storage.loot.LootTableList;
import org.dimdev.rift.listener.*;
import org.dimdev.rift.listener.client.TileEntityRendererAdder;

import java.util.Map;

public class ArcaneMagic implements BlockAdder, ItemAdder, TileEntityTypeAdder, TileEntityRendererAdder, PacketAdder, WorldChanger, StructureAdder
{
    public static TileEntityType ALTAR_TE;
    public static TileEntityType INDUCTOR_TE;
    public static TileEntityType PEDESTAL_TE;

    public static final Block ALTAR = new BlockAltar(Block.Builder.create(Material.ROCK).hardnessAndResistance(5, 1200).soundType(SoundType.STONE));
    private static final Block INDUCTOR = new BlockInductor(Block.Builder.create(Material.WOOD).hardnessAndResistance(2, 500).soundType(SoundType.STONE));
    private static final Block PEDESTAL = new BlockPedestal(Block.Builder.create(Material.WOOD).hardnessAndResistance(2.0F, 500.0F).soundType(SoundType.WOOD));

    public static final Item PARCHMENT = new ItemParchment();
    public static final Item WRITTEN_PARCHMENT = new ItemWrittenParchment(false);
    private static final Item ANCIENT_PARCHMENT = new ItemWrittenParchment(true);
    private static final Item LINKING_ROD = new ItemLinkingRod();
    private static final Item NOTEBOOK = new ItemNotebook();

    private static final ResourceLocation WIZARD_HUT_CHEST = LootTableList.register(new ResourceLocation(ArcaneMagicResources.MOD_ID, "wizard_hut"));
    private static final Structure<WizardHutConfig> WIZARD_HUT_STRUCTURE = new WizardHutStructure();

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
        Item.registerItem(new ResourceLocation(ArcaneMagicResources.MOD_ID, "linking_rod"), LINKING_ROD);
        Item.registerItem(new ResourceLocation(ArcaneMagicResources.MOD_ID, "notebook"), NOTEBOOK);
    }

    @Override
    public void registerTileEntityTypes()
    {
        ALTAR_TE = TileEntityType.registerTileEntityType("arcanemagic:altar", TileEntityType.Builder.create(TileEntityAltar::new));
        INDUCTOR_TE = TileEntityType.registerTileEntityType("arcanemagic:inductor", TileEntityType.Builder.create(TileEntityInductor::new));
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

    @Override
    public void modifyBiome(int biomeId, String biomeName, Biome biome)
    {
        if (biome.hasStructure(Feature.VILLAGE))
        {
            System.out.println("registered wizard hut");
            biome.addStructure(WIZARD_HUT_STRUCTURE, new WizardHutConfig());
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature(WIZARD_HUT_STRUCTURE, new WizardHutConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG));
        }
    }

    @Override
    public void registerStructureNames()
    {
        StructureIO.registerStructure(WizardHutStructure.Start.class, ArcaneMagicResources.WIZARD_HUT_NAME);
        StructureIO.registerStructureComponent(WizardHutPieces.Piece.class, ArcaneMagicResources.WIZARD_HUT_NAME);
    }

    @Override
    public void addStructuresToMap(Map<String, Structure<?>> map)
    {
        map.put(ArcaneMagicResources.WIZARD_HUT_NAME.toLowerCase(), WIZARD_HUT_STRUCTURE);
    }
}
