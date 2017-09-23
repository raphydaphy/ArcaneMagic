package com.raphydaphy.thaumcraft.proxy;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.api.ThaumcraftAPI;
import com.raphydaphy.thaumcraft.block.BlockArcaneWorktable;
import com.raphydaphy.thaumcraft.block.BlockBase;
import com.raphydaphy.thaumcraft.block.BlockModLeaves;
import com.raphydaphy.thaumcraft.block.BlockModLog;
import com.raphydaphy.thaumcraft.block.BlockModSlab;
import com.raphydaphy.thaumcraft.block.BlockOre;
import com.raphydaphy.thaumcraft.block.BlockResearchTable;
import com.raphydaphy.thaumcraft.block.BlockTable;
import com.raphydaphy.thaumcraft.handler.ThaumcraftSoundHandler;
import com.raphydaphy.thaumcraft.init.ModBlocks;
import com.raphydaphy.thaumcraft.init.ModEntities;
import com.raphydaphy.thaumcraft.item.ItemBase;
import com.raphydaphy.thaumcraft.item.ItemBlockModSlab;
import com.raphydaphy.thaumcraft.item.ItemScribingTools;
import com.raphydaphy.thaumcraft.item.ItemThaumonomicon;
import com.raphydaphy.thaumcraft.item.ItemWand;
import com.raphydaphy.thaumcraft.research.CategoryBasicInformation;
import com.raphydaphy.thaumcraft.research.CategoryThaumaturgy;
import com.raphydaphy.thaumcraft.tileentity.TileEntityArcaneWorktable;
import com.raphydaphy.thaumcraft.world.WorldGenGreatwood;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class CommonProxy 
{
	public void preInit(FMLPreInitializationEvent event) 
	{
		ModEntities.init();
	}
	
	public void init(FMLInitializationEvent event) 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Thaumcraft.instance, new GuiProxy());
		
		GameRegistry.registerWorldGenerator(new WorldGenGreatwood(false, false), 150);
	}
	
	public void postInit(FMLPostInitializationEvent event) 
	{
		ThaumcraftAPI.registerThaumonomiconCategory(new CategoryBasicInformation());
		ThaumcraftAPI.registerThaumonomiconCategory(new CategoryThaumaturgy());
	}
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
	{
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		ThaumcraftSoundHandler.register("craft_start", registry);
		ThaumcraftSoundHandler.register("wand_1", registry);
		ThaumcraftSoundHandler.register("wand_2", registry);
		ThaumcraftSoundHandler.register("wand_3", registry);
		ThaumcraftSoundHandler.register("page_1", registry);
		ThaumcraftSoundHandler.register("page_2", registry);
		ThaumcraftSoundHandler.register("creak_1", registry);
		ThaumcraftSoundHandler.register("creak_2", registry);
		ThaumcraftSoundHandler.register("tool_1", registry);
		ThaumcraftSoundHandler.register("tool_2", registry);
		ThaumcraftSoundHandler.register("fly_1", registry);
		ThaumcraftSoundHandler.register("fly_2", registry);
		ThaumcraftSoundHandler.register("gore_1", registry);
		ThaumcraftSoundHandler.register("gore_2", registry);
		ThaumcraftSoundHandler.register("wind_1", registry);
		ThaumcraftSoundHandler.register("wind_2", registry);
		ThaumcraftSoundHandler.register("write_1", registry);
		ThaumcraftSoundHandler.register("write_2", registry);
		ThaumcraftSoundHandler.register("shock_1", registry);
		ThaumcraftSoundHandler.register("shock_2", registry);
		ThaumcraftSoundHandler.register("swing_1", registry);
		ThaumcraftSoundHandler.register("swing_2", registry);
		ThaumcraftSoundHandler.register("squeek_1", registry);
		ThaumcraftSoundHandler.register("squeek_2", registry);
		ThaumcraftSoundHandler.register("zap_1", registry);
		ThaumcraftSoundHandler.register("zap_2", registry);
		ThaumcraftSoundHandler.register("hh_on", registry);
		ThaumcraftSoundHandler.register("hh_off", registry);
		ThaumcraftSoundHandler.register("camera_clack_1", registry);
		ThaumcraftSoundHandler.register("camera_clack_2", registry);
		ThaumcraftSoundHandler.register("camera_clack_3", registry);
	}
	
	@SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) 
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		// Trees
		registry.register(new BlockModLog("log_greatwood"));
		registry.register(new BlockModLog("log_silverwood"));
		registry.register(new BlockModLeaves("leaves_greatwood"));
		registry.register(new BlockModLeaves("leaves_silverwood"));
        registry.register(new BlockBase("planks_greatwood", Material.WOOD, 1f));
        registry.register(new BlockBase("planks_silverwood", Material.WOOD, 1f));
        registry.register(new BlockModSlab("slab_greatwood", Material.WOOD));
        registry.register(new BlockModSlab("slab_silverwood", Material.WOOD));
        
        // Ores
        registry.register(new BlockOre("ore_infused", 3.5f));
        
        // Tables
        registry.register(new BlockTable("table", Material.WOOD, 2f, "axe", 0));
        registry.register(new BlockArcaneWorktable());
        registry.register(new BlockResearchTable());
        
        // TileEntities
        GameRegistry.registerTileEntity(TileEntityArcaneWorktable.class, Thaumcraft.MODID + "_arcane_worktable");
       
    }
	
	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) 
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.register(new ItemBase("wand_cap_iron"));
        registry.register(new ItemWand("wand"));
        registry.register(new ItemThaumonomicon("thaumonomicon"));
        registry.register(new ItemBase("shard"));
        registry.register(new ItemScribingTools("scribing_tools"));
        registry.register(new ItemBase("knowledge_fragment"));
        
        // Trees
        registry.register(new ItemBlock(ModBlocks.log_greatwood).setRegistryName(ModBlocks.log_greatwood.getRegistryName()));
        registry.register(new ItemBlock(ModBlocks.log_silverwood).setRegistryName(ModBlocks.log_silverwood.getRegistryName()));
        registry.register(new ItemBlock(ModBlocks.leaves_greatwood).setRegistryName(ModBlocks.leaves_greatwood.getRegistryName()));
        registry.register(new ItemBlock(ModBlocks.leaves_silverwood).setRegistryName(ModBlocks.leaves_silverwood.getRegistryName()));
        registry.register(new ItemBlock(ModBlocks.planks_greatwood).setRegistryName(ModBlocks.planks_greatwood.getRegistryName()));
        registry.register(new ItemBlock(ModBlocks.planks_silverwood).setRegistryName(ModBlocks.planks_silverwood.getRegistryName()));
        registry.register(new ItemBlockModSlab(ModBlocks.slab_greatwood));
        registry.register(new ItemBlockModSlab(ModBlocks.slab_silverwood));
        
        // Ores
        registry.register(new ItemBlock(ModBlocks.ore_infused).setRegistryName(ModBlocks.ore_infused.getRegistryName()));
        
        // Tables
        registry.register(new ItemBlock(ModBlocks.table).setRegistryName(ModBlocks.table.getRegistryName()));
        registry.register(new ItemBlock(ModBlocks.arcane_worktable).setRegistryName(ModBlocks.arcane_worktable.getRegistryName()));
    }
}
