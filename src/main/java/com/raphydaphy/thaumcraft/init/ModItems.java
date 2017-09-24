package com.raphydaphy.thaumcraft.init;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.item.ItemBase;
import com.raphydaphy.thaumcraft.item.ItemFoci;
import com.raphydaphy.thaumcraft.item.ItemScribingTools;
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

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":scribing_tools")
	public static ItemScribingTools scribing_tools;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":knowledge_fragment")
	public static ItemBase knowledge_fragment;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":focus_fire")
	public static ItemFoci focus_fire;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		wand_cap_iron.initModel();
		wand.initModel();
		thaumonomicon.initModel();
		shard.initModel();
		scribing_tools.initModel();
		knowledge_fragment.initModel();
		focus_fire.initModel();
	}
}
