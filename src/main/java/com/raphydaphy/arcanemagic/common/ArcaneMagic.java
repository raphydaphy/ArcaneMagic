package com.raphydaphy.arcanemagic.common;

import org.apache.logging.log4j.Logger;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.Anima.AnimaSubscriber;
import com.raphydaphy.arcanemagic.api.notebook.CategoryRegistry;
import com.raphydaphy.arcanemagic.client.proxy.GuiProxy;
import com.raphydaphy.arcanemagic.common.capabilities.Capabilities;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.init.ArcaneMagicCreativeTab;
import com.raphydaphy.arcanemagic.common.init.ModEntities;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.common.proxy.CommonProxy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = ArcaneMagic.MODID, name = ArcaneMagic.MODNAME, version = ArcaneMagic.VERSION, useMetadata = true)
public class ArcaneMagic
{
	public static final String MODID = "arcanemagic";
	public static final String MODNAME = "Arcane Magic";
	public static final String VERSION = "0.1";

	@SidedProxy(clientSide = "com.raphydaphy.arcanemagic.client.proxy.ClientProxy", serverSide = "com.raphydaphy.arcanemagic.server.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static ArcaneMagic instance;

	public static Logger LOGGER;

	public static final ArcaneMagicCreativeTab creativeTab = new ArcaneMagicCreativeTab();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER = event.getModLog();
		ModEntities.init();
		ArcaneMagicPacketHandler.registerMessages(ArcaneMagic.MODID);
		proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(new ModRegistry());
		MinecraftForge.EVENT_BUS.register(new AnimaSubscriber());
		ModRegistry.registerTiles();
		Capabilities.register();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.post(
				new Register<Anima>(new ResourceLocation(ArcaneMagic.MODID, "essence_registry"), Anima.REGISTRY));
		NetworkRegistry.INSTANCE.registerGuiHandler(ArcaneMagic.instance, new GuiProxy());
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		NotebookCategories.register();

		// TODO: a class for this 
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.REDSTONE_ORE,
				NotebookCategories.MAGICAL_INSIGHTS);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysis(new ItemStack(Items.REDSTONE),
				NotebookCategories.MAGICAL_INSIGHTS);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysis("plant", NotebookCategories.FOUNDATIONS_OF_MAGIC);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.DOUBLE_PLANT,
				NotebookCategories.FOUNDATIONS_OF_MAGIC);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.RED_FLOWER,
				NotebookCategories.FOUNDATIONS_OF_MAGIC);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.YELLOW_FLOWER,
				NotebookCategories.FOUNDATIONS_OF_MAGIC);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.RED_MUSHROOM,
				NotebookCategories.FOUNDATIONS_OF_MAGIC);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.BROWN_MUSHROOM,
				NotebookCategories.FOUNDATIONS_OF_MAGIC);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.LEAVES, NotebookCategories.NATURAL_DIVISIONS);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.LEAVES2, NotebookCategories.NATURAL_DIVISIONS);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.TALLGRASS, NotebookCategories.HORIZON_CATEGORY);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.SNOW, NotebookCategories.OZONE_CATEGORY);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.MAGMA, NotebookCategories.INFERNO_CATEGORY);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.PRISMARINE, NotebookCategories.DEPTH_CATEGORY);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.SEA_LANTERN, NotebookCategories.DEPTH_CATEGORY);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.HARDENED_CLAY,
				NotebookCategories.CHAOS_CATEGORY);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.STAINED_HARDENED_CLAY,
				NotebookCategories.CHAOS_CATEGORY);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.MYCELIUM, NotebookCategories.PEACE_CATEGORY);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.BROWN_MUSHROOM_BLOCK,
				NotebookCategories.PEACE_CATEGORY);
		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.RED_MUSHROOM_BLOCK,
				NotebookCategories.PEACE_CATEGORY);

		ArcaneMagicAPI.getAnalyzer().registerForAnalysisWithItem(Blocks.GLOWSTONE,
				NotebookCategories.ESSENCE_MANIPULATION);

		// TODO: also a class for this
		ArcaneMagicAPI.registerOre("oreEmerald", Anima.HORIZON);
		ArcaneMagicAPI.registerOre("oreDiamond", Anima.DEPTH);
		ArcaneMagicAPI.registerOre("oreGold", Anima.INFERNO);
		ArcaneMagicAPI.registerOre("oreIron", Anima.OZONE);
		ArcaneMagicAPI.registerOre("oreCoal", Anima.CHAOS);
		ArcaneMagicAPI.registerOre("oreLapis", Anima.DEPTH);
		ArcaneMagicAPI.registerOre("oreQuartz", Anima.OZONE);

		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void complete(FMLLoadCompleteEvent e)
	{
		CategoryRegistry.sortCategories();
	}
}
