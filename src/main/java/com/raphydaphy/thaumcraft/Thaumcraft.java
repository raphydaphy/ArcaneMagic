package com.raphydaphy.thaumcraft;

import com.raphydaphy.thaumcraft.api.ThaumcraftAPI;
import com.raphydaphy.thaumcraft.handler.ThaumcraftPacketHandler;
import com.raphydaphy.thaumcraft.init.ModEntities;
import com.raphydaphy.thaumcraft.init.ModRegistry;
import com.raphydaphy.thaumcraft.init.ThaumcraftCreativeTab;
import com.raphydaphy.thaumcraft.init.VanillaThaumcraftParts;
import com.raphydaphy.thaumcraft.proxy.CommonProxy;
import com.raphydaphy.thaumcraft.proxy.GuiProxy;
import com.raphydaphy.thaumcraft.research.CategoryAlchemy;
import com.raphydaphy.thaumcraft.research.CategoryBasicInformation;
import com.raphydaphy.thaumcraft.research.CategoryThaumaturgy;
import com.raphydaphy.thaumcraft.world.WorldGenGreatwood;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Thaumcraft.MODID, name = Thaumcraft.MODNAME, version = Thaumcraft.VERSION, useMetadata = true)
public class Thaumcraft {
	public static final String MODID = "thaumcraft";
	public static final String MODNAME = "Thaumcraft";
	public static final String VERSION = "0.1";

	@SidedProxy(clientSide = "com.raphydaphy.thaumcraft.proxy.ClientProxy", serverSide = "com.raphydaphy.thaumcraft.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static Thaumcraft instance;

	public static final ThaumcraftCreativeTab creativeTab = new ThaumcraftCreativeTab();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModEntities.init();
		ThaumcraftPacketHandler.registerMessages(Thaumcraft.MODID);
		proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(new ModRegistry());
		ModRegistry.registerTiles();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		VanillaThaumcraftParts.registerWands();
		NetworkRegistry.INSTANCE.registerGuiHandler(Thaumcraft.instance, new GuiProxy());
		GameRegistry.registerWorldGenerator(new WorldGenGreatwood(false, false), 150);
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ThaumcraftAPI.registerThaumonomiconCategory(new CategoryBasicInformation());
		ThaumcraftAPI.registerThaumonomiconCategory(new CategoryThaumaturgy());
		ThaumcraftAPI.registerThaumonomiconCategory(new CategoryAlchemy());
		proxy.postInit(event);
	}
}
