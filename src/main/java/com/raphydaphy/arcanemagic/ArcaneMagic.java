package com.raphydaphy.arcanemagic;

import org.apache.logging.log4j.Logger;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.Essence.EssenceSubscriber;
import com.raphydaphy.arcanemagic.api.notebook.CategoryRegistry;
import com.raphydaphy.arcanemagic.api.recipe.ElementalCraftingRecipe;
import com.raphydaphy.arcanemagic.capabilities.Capabilities;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.init.ArcaneMagicCreativeTab;
import com.raphydaphy.arcanemagic.init.ModEntities;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.notebook.NotebookCategories;
import com.raphydaphy.arcanemagic.proxy.CommonProxy;
import com.raphydaphy.arcanemagic.proxy.GuiProxy;

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

	@SidedProxy(clientSide = "com.raphydaphy.arcanemagic.proxy.ClientProxy", serverSide = "com.raphydaphy.arcanemagic.proxy.ServerProxy")
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
		MinecraftForge.EVENT_BUS.register(new EssenceSubscriber());
		ModRegistry.registerTiles();
		Capabilities.register();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.post(
				new Register<Essence>(new ResourceLocation(ArcaneMagic.MODID, "essence_registry"), Essence.REGISTRY));
		NetworkRegistry.INSTANCE.registerGuiHandler(ArcaneMagic.instance, new GuiProxy());
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		NotebookCategories.register();

		// TODO: make this a less rubbish registeration system
		// TODO: add actual recipes
		
		ItemStack[][] quillRecipe = {
				{ new ItemStack(Blocks.STONE), ItemStack.EMPTY, new ItemStack(Blocks.STONE) },
				{ new ItemStack(Blocks.STONE), ItemStack.EMPTY, new ItemStack(Blocks.STONE) },
				{ new ItemStack(Blocks.STONE), ItemStack.EMPTY, new ItemStack(Blocks.STONE) } };
		ArcaneMagicAPI.registerElementalCraftingRecipe(new ElementalCraftingRecipe(quillRecipe.clone(), new ItemStack(ModRegistry.SCRIBING_TOOLS)));
		
		ItemStack[][] quartzRecipe = {
				{ new ItemStack(Blocks.GLOWSTONE), ItemStack.EMPTY, new ItemStack(Blocks.GLOWSTONE) },
				{ ItemStack.EMPTY, new ItemStack(Blocks.GLOWSTONE), ItemStack.EMPTY },
				{ new ItemStack(Blocks.GLOWSTONE), ItemStack.EMPTY, new ItemStack(Blocks.GLOWSTONE) } };
		ArcaneMagicAPI.registerElementalCraftingRecipe(new ElementalCraftingRecipe(quartzRecipe.clone(), new ItemStack(Items.QUARTZ)));
		
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void complete(FMLLoadCompleteEvent e)
	{
		CategoryRegistry.sortCategories();
	}
}
