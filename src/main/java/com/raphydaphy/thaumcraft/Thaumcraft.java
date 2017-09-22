package com.raphydaphy.thaumcraft;

import com.raphydaphy.thaumcraft.init.ThaumcraftCreativeTab;
import com.raphydaphy.thaumcraft.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Thaumcraft.MODID, name = Thaumcraft.MODNAME, version = Thaumcraft.VERSION, useMetadata = true)
public class Thaumcraft
{
    public static final String MODID = "thaumcraft";
    public static final String MODNAME = "Thaumcraft";
    public static final String VERSION = "0.1";
    
    @SidedProxy(clientSide = "com.raphydaphy.thaumcraft.proxy.ClientProxy", serverSide = "com.raphydaphy.thaumcraft.proxy.ServerProxy")
    public static CommonProxy proxy;
    
    @Mod.Instance
    public static Thaumcraft instance;
    
    public static final ThaumcraftCreativeTab creativeTab = new ThaumcraftCreativeTab();
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
