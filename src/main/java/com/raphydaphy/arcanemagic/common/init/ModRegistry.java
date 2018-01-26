package com.raphydaphy.arcanemagic.common.init;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.block.BlockAnalyzer;
import com.raphydaphy.arcanemagic.common.block.BlockAnimaConjurer;
import com.raphydaphy.arcanemagic.common.block.BlockAnimusMaterializer;
import com.raphydaphy.arcanemagic.common.block.BlockArcaneTransfigurationTable;
import com.raphydaphy.arcanemagic.common.block.BlockFancyLight;
import com.raphydaphy.arcanemagic.common.block.BlockInfernalSmelter;
import com.raphydaphy.arcanemagic.common.block.BlockAltar;
import com.raphydaphy.arcanemagic.common.block.BlockWritingDesk;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.item.ItemAnimaCrystal;
import com.raphydaphy.arcanemagic.common.item.ItemArcaneDagger;
import com.raphydaphy.arcanemagic.common.item.ItemBase;
import com.raphydaphy.arcanemagic.common.item.ItemCreationCrystal;
import com.raphydaphy.arcanemagic.common.item.ItemEssenceChannelingRod;
import com.raphydaphy.arcanemagic.common.item.ItemIlluminator;
import com.raphydaphy.arcanemagic.common.item.ItemNotebook;
import com.raphydaphy.arcanemagic.common.item.ItemParchment;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnalyzer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimaConjurer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimusMaterializer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneTransfigurationTable;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityInfernalSmelter;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityWritingDesk;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRegistry
{
	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();
	public static final List<IRecipe> RECIPES = new ArrayList<>();

	public static final BlockWritingDesk WRITING_DESK = new BlockWritingDesk();
	public static final BlockAnalyzer ANALYZER = new BlockAnalyzer();
	public static final BlockAltar ALTAR = new BlockAltar();
	public static final BlockArcaneTransfigurationTable ARCANE_TRANSFIGURATION_TABLE = new BlockArcaneTransfigurationTable();
	public static final BlockAnimusMaterializer ANIMUS_MATERIALIZER = new BlockAnimusMaterializer();
	public static final BlockAnimaConjurer ANIMA_CONJURER = new BlockAnimaConjurer();
	public static final BlockFancyLight FANCY_LIGHT = new BlockFancyLight();
	public static final BlockInfernalSmelter INFERNAL_SMELTER = new BlockInfernalSmelter();

	public static final ItemNotebook NOTEBOOK = new ItemNotebook("notebook");
	public static final ItemAnimaCrystal ANIMA = new ItemAnimaCrystal();
	public static final ItemCreationCrystal CREATION = new ItemCreationCrystal();
	public static final ItemBase BLANK_PARCHMENT = new ItemBase("blank_parchment");
	public static final ItemParchment ANCIENT_PARCHMENT = new ItemParchment("ancient_parchment");
	public static final ItemParchment WRITTEN_PARCHMENT = new ItemParchment("written_parchment");
	public static final ItemIlluminator MYSTICAL_ILLUMINATOR = new ItemIlluminator();
	public static final ItemEssenceChannelingRod ESSENCE_CHANNELING_ROD = new ItemEssenceChannelingRod();
	public static final ItemArcaneDagger ARCANE_DAGGER = new ItemArcaneDagger();

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
	public void onRecipeRegister(Register<IRecipe> e)
	{
		// TODO: shadows will probably make this better.. hopefully :-)
		OreDictionary.registerOre("formationAnima",
				new ItemStack(ModRegistry.ANIMA, 1, OreDictionary.WILDCARD_VALUE));

		for (Item i : ModRegistry.ITEMS)
			if (i instanceof IHasRecipe)
				((IHasRecipe) i).initRecipes(e);
		for (Block b : ModRegistry.BLOCKS)
			if (b instanceof IHasRecipe)
				((IHasRecipe) b).initRecipes(e);

		RecipeHelper.addShaped(BLANK_PARCHMENT, 3, 3, null, "paper", null, "paper", "dye", "paper", null, "paper",
				null);

		// @Shadows: The day I work with JSON recipes is the day the world ends.
		// @raphy: uhoh
		e.getRegistry().registerAll(RECIPES.toArray(new IRecipe[RECIPES.size()]));
	}

	@SubscribeEvent
	public void registerSounds(Register<SoundEvent> event)
	{
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		ArcaneMagicSoundHandler.register("spell", registry);
		ArcaneMagicSoundHandler.register("scepter_1", registry);
		ArcaneMagicSoundHandler.register("scepter_2", registry);
		ArcaneMagicSoundHandler.register("scepter_3", registry);
		ArcaneMagicSoundHandler.register("page_1", registry);
		ArcaneMagicSoundHandler.register("page_2", registry);
		ArcaneMagicSoundHandler.register("arcane_transfiguration_success", registry);
		ArcaneMagicSoundHandler.register("write_1", registry);
		ArcaneMagicSoundHandler.register("write_2", registry);
		ArcaneMagicSoundHandler.register("learn_1", registry);
		ArcaneMagicSoundHandler.register("learn_2", registry);
		ArcaneMagicSoundHandler.register("reconstruct", registry);
		ArcaneMagicSoundHandler.register("clack", registry);
	}

	public static void registerTiles()
	{
		GameRegistry.registerTileEntity(TileEntityArcaneTransfigurationTable.class,
				ArcaneMagic.MODID + "_arcane_transfiguration_table");
		GameRegistry.registerTileEntity(TileEntityAnimusMaterializer.class, ArcaneMagic.MODID + "_animus_materializer");
		GameRegistry.registerTileEntity(TileEntityAnimaConjurer.class,
				ArcaneMagic.MODID + "_anima_conjurer");
		GameRegistry.registerTileEntity(TileEntityWritingDesk.class, ArcaneMagic.MODID + "_writing_desk");
		GameRegistry.registerTileEntity(TileEntityAltar.class, ArcaneMagic.MODID + "_infusion_altar");
		GameRegistry.registerTileEntity(TileEntityAnalyzer.class, ArcaneMagic.MODID + "_analyzer");
		GameRegistry.registerTileEntity(TileEntityInfernalSmelter.class, ArcaneMagic.MODID + "_infernal_smelter");
	}
}
