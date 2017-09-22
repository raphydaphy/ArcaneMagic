package com.raphydaphy.thaumcraft.init;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.item.ItemBase;
import com.raphydaphy.thaumcraft.item.ItemThaumonomicon;
import com.raphydaphy.thaumcraft.item.ItemWand;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems 
{
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":wand_cap_iron")
    public static ItemBase wand_cap_iron;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":wand")
	public static ItemWand wand;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":thaumonomicon")
	public static ItemThaumonomicon thaumonomicon;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":shard")
	public static ItemBase shard;
	
	@SideOnly(Side.CLIENT)
    public static void initModels() {
        wand_cap_iron.initModel();
        wand.initModel();
        thaumonomicon.initModel();
        shard.initModel();
    }
}
