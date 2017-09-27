package com.raphydaphy.arcanemagic.init;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.BlockArcaneWorktable;
import com.raphydaphy.arcanemagic.block.BlockOre;
import com.raphydaphy.arcanemagic.block.BlockTable;
import com.raphydaphy.arcanemagic.block.BlockWritingDesk;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.item.ItemBase;
import com.raphydaphy.arcanemagic.item.ItemFoci;
import com.raphydaphy.arcanemagic.item.ItemNotebook;
import com.raphydaphy.arcanemagic.item.ItemScepter;
import com.raphydaphy.arcanemagic.item.ItemScribingTools;
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
	
	public static final BlockOre INFUSED_ORE = new BlockOre("ore_infused", 3.5f);
	public static final BlockTable TABLE = new BlockTable("table", Material.WOOD, 2f, "axe", 0);
	public static final BlockArcaneWorktable WORKTABLE = new BlockArcaneWorktable();
	public static final BlockWritingDesk RESEARCH_TABLE = new BlockWritingDesk();

	public static final ItemBase IRON_TIP = new ItemBase("scepter_tip_iron");
	public static final ItemScepter SCEPTER = new ItemScepter("scepter");
	public static final ItemNotebook NOTEBOOK = new ItemNotebook("notebook");
	public static final ItemBase SHARD = new ItemBase("shard", 6);
	public static final ItemScribingTools SCRIBING_TOOLS = new ItemScribingTools("scribing_tools");
	public static final ItemBase ANCIENT_PARCHMENT = new ItemBase("ancient_parchment");
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
		ArcaneMagicSoundHandler.register("craft_start", registry);
		ArcaneMagicSoundHandler.register("scepter_1", registry);
		ArcaneMagicSoundHandler.register("scepter_2", registry);
		ArcaneMagicSoundHandler.register("scepter_3", registry);
		ArcaneMagicSoundHandler.register("page_1", registry);
		ArcaneMagicSoundHandler.register("page_2", registry);
		ArcaneMagicSoundHandler.register("creak_1", registry);
		ArcaneMagicSoundHandler.register("creak_2", registry);
		ArcaneMagicSoundHandler.register("tool_1", registry);
		ArcaneMagicSoundHandler.register("tool_2", registry);
		ArcaneMagicSoundHandler.register("fly_1", registry);
		ArcaneMagicSoundHandler.register("fly_2", registry);
		ArcaneMagicSoundHandler.register("gore_1", registry);
		ArcaneMagicSoundHandler.register("gore_2", registry);
		ArcaneMagicSoundHandler.register("wind_1", registry);
		ArcaneMagicSoundHandler.register("wind_2", registry);
		ArcaneMagicSoundHandler.register("write_1", registry);
		ArcaneMagicSoundHandler.register("write_2", registry);
		ArcaneMagicSoundHandler.register("shock_1", registry);
		ArcaneMagicSoundHandler.register("shock_2", registry);
		ArcaneMagicSoundHandler.register("swing_1", registry);
		ArcaneMagicSoundHandler.register("swing_2", registry);
		ArcaneMagicSoundHandler.register("squeek_1", registry);
		ArcaneMagicSoundHandler.register("squeek_2", registry);
		ArcaneMagicSoundHandler.register("zap_1", registry);
		ArcaneMagicSoundHandler.register("zap_2", registry);
		ArcaneMagicSoundHandler.register("hh_on", registry);
		ArcaneMagicSoundHandler.register("hh_off", registry);
		ArcaneMagicSoundHandler.register("camera_clack_1", registry);
		ArcaneMagicSoundHandler.register("camera_clack_2", registry);
		ArcaneMagicSoundHandler.register("camera_clack_3", registry);
	}

	public static void registerTiles()
	{
		GameRegistry.registerTileEntity(TileEntityArcaneWorktable.class, ArcaneMagic.MODID + "_arcane_worktable");
	}
}
