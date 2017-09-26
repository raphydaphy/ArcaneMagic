package com.raphydaphy.arcanemagic.init;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.block.BlockArcaneWorktable;
import com.raphydaphy.arcanemagic.block.BlockBase;
import com.raphydaphy.arcanemagic.block.BlockModLeaves;
import com.raphydaphy.arcanemagic.block.BlockModLog;
import com.raphydaphy.arcanemagic.block.BlockModSlab;
import com.raphydaphy.arcanemagic.block.BlockOre;
import com.raphydaphy.arcanemagic.block.BlockResearchTable;
import com.raphydaphy.arcanemagic.block.BlockTable;
import com.raphydaphy.arcanemagic.handler.ThaumcraftSoundHandler;
import com.raphydaphy.arcanemagic.item.ItemBase;
import com.raphydaphy.arcanemagic.item.ItemFoci;
import com.raphydaphy.arcanemagic.item.ItemScribingTools;
import com.raphydaphy.arcanemagic.item.ItemThaumonomicon;
import com.raphydaphy.arcanemagic.item.ItemWand;
import com.raphydaphy.arcanemagic.tileentity.TileEntityArcaneWorktable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRegistry
{

	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();

	public static final BlockModLog GREATWOOD_LOG = new BlockModLog("log_greatwood");
	public static final BlockModLog SILVERWOOD_LOG = new BlockModLog("log_silverwood");
	public static final BlockModLeaves GREATWOOD_LEAVES = new BlockModLeaves("leaves_greatwood");
	public static final BlockModLeaves SILVERWOOD_LEAVES = new BlockModLeaves("leaves_silverwood");
	public static final BlockBase GREATWOOD_PLANKS = new BlockBase("planks_greatwood", Material.WOOD, 1f);
	public static final BlockBase SILVERWOOD_PLANKS = new BlockBase("planks_silverwood", Material.WOOD, 1f);
	public static final BlockModSlab GREATWOOD_SLAB = new BlockModSlab("slab_greatwood", Material.WOOD);
	public static final BlockModSlab SILVERWOOD_SLAB = new BlockModSlab("slab_silverwood", Material.WOOD);
	public static final BlockOre INFUSED_ORE = new BlockOre("ore_infused", 3.5f);
	public static final BlockTable TABLE = new BlockTable("table", Material.WOOD, 2f, "axe", 0);
	public static final BlockArcaneWorktable WORKTABLE = new BlockArcaneWorktable();
	public static final BlockResearchTable RESEARCH_TABLE = new BlockResearchTable();

	public static final ItemBase IRON_CAP = new ItemBase("wand_cap_iron");
	public static final ItemWand WAND = new ItemWand("wand");
	public static final ItemThaumonomicon THAUMONOMICON = new ItemThaumonomicon("thaumonomicon");
	public static final ItemBase SHARD = new ItemBase("shard", 6);
	public static final ItemScribingTools SCRIBING_TOOLS = new ItemScribingTools("scribing_tools");
	public static final ItemBase KNOWLEDGE_FRAGMENT = new ItemBase("knowledge_fragment");
	public static final ItemFoci FIRE_FOCUS = new ItemFoci("focus_fire");

	@SubscribeEvent
	public void onBlockRegister(Register<Block> e)
	{
		e.getRegistry().registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
	}

	@SubscribeEvent
	public void onItemRegister(Register<Item> e)
	{
		e.getRegistry().registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event)
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

	public static void registerTiles()
	{
		GameRegistry.registerTileEntity(TileEntityArcaneWorktable.class, Thaumcraft.MODID + "_arcane_worktable");
	}
}
