package com.raphydaphy.thaumcraft.init;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.block.BlockArcaneWorktable;
import com.raphydaphy.thaumcraft.block.BlockBase;
import com.raphydaphy.thaumcraft.block.BlockModLeaves;
import com.raphydaphy.thaumcraft.block.BlockModLog;
import com.raphydaphy.thaumcraft.block.BlockModSlab;
import com.raphydaphy.thaumcraft.block.BlockOre;
import com.raphydaphy.thaumcraft.block.BlockResearchTable;
import com.raphydaphy.thaumcraft.block.BlockTable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModBlocks
{
	// Trees

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":log_greatwood")
	public static BlockModLog log_greatwood;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":log_silverwood")
	public static BlockModLog log_silverwood;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":leaves_greatwood")
	public static BlockModLeaves leaves_greatwood;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":leaves_silverwood")
	public static BlockModLeaves leaves_silverwood;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":planks_greatwood")
	public static BlockBase planks_greatwood;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":planks_silverwood")
	public static BlockBase planks_silverwood;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":slab_greatwood")
	public static BlockModSlab slab_greatwood;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":slab_silverwood")
	public static BlockModSlab slab_silverwood;

	// Ores

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":ore_infused")
	public static BlockOre ore_infused;

	// Tables

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":table")
	public static BlockTable table;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":arcane_worktable")
	public static BlockArcaneWorktable arcane_worktable;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":research_table")
	public static BlockResearchTable research_table;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		// Trees
		log_greatwood.initModel();
		log_silverwood.initModel();
		leaves_greatwood.initModel();
		leaves_silverwood.initModel();
		planks_greatwood.initModel();
		planks_silverwood.initModel();
		slab_greatwood.initModel();
		slab_silverwood.initModel();

		// Ores
		ore_infused.initModel();

		// Tables
		table.initModel();
		arcane_worktable.initModel();
	}
}
