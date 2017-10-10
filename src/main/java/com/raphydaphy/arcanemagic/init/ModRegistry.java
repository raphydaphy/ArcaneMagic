package com.raphydaphy.arcanemagic.init;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.BlockCrystallizer;
import com.raphydaphy.arcanemagic.block.BlockElementalCraftingTable;
import com.raphydaphy.arcanemagic.block.BlockEssenceConcentrator;
import com.raphydaphy.arcanemagic.block.BlockFancyLight;
import com.raphydaphy.arcanemagic.block.BlockWritingDesk;
import com.raphydaphy.arcanemagic.data.EnumBasicEssence;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.item.ItemAncientParchment;
import com.raphydaphy.arcanemagic.item.ItemBase;
import com.raphydaphy.arcanemagic.item.ItemCore;
import com.raphydaphy.arcanemagic.item.ItemEnum;
import com.raphydaphy.arcanemagic.item.ItemNotebook;
import com.raphydaphy.arcanemagic.item.ItemScepter;
import com.raphydaphy.arcanemagic.item.ItemTip;
import com.raphydaphy.arcanemagic.tileentity.TileEntityCrystallizer;
import com.raphydaphy.arcanemagic.tileentity.TileEntityElementalCraftingTable;
import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceConcentrator;
import com.raphydaphy.arcanemagic.tileentity.TileEntityWritingDesk;
import com.raphydaphy.arcanemagic.util.IHasRecipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRegistry
{

	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();
	public static final List<IRecipe> RECIPES = new ArrayList<>();

	public static final BlockWritingDesk WRITING_DESK = new BlockWritingDesk();
	public static final BlockElementalCraftingTable ELEMENTAL_CRAFTING_TABLE = new BlockElementalCraftingTable();
	public static final BlockCrystallizer CRYSTALLIZER = new BlockCrystallizer();
	public static final BlockEssenceConcentrator ESSENCE_CONCENTRATOR = new BlockEssenceConcentrator();
	public static final BlockFancyLight FANCY_LIGHT = new BlockFancyLight();

	public static final ItemBase TIP = new ItemTip();
	public static final ItemBase CORE = new ItemCore();
	public static final ItemScepter SCEPTER = new ItemScepter("scepter");
	public static final ItemNotebook NOTEBOOK = new ItemNotebook("notebook");
	public static final ItemEnum<EnumBasicEssence> ESSENCE = new ItemEnum<>("essence", EnumBasicEssence.values());
	public static final ItemAncientParchment ANCIENT_PARCHMENT = new ItemAncientParchment("ancient_parchment");

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
		for (Item i : ModRegistry.ITEMS)
			if (i instanceof IHasRecipe)
				((IHasRecipe) i).initRecipes(e);
		for (Block b : ModRegistry.BLOCKS)
			if (b instanceof IHasRecipe)
				((IHasRecipe) b).initRecipes(e);

		// @Shadows: The day I work with JSON recipes is the day the world ends.
		// @raphy: uhoh
		e.getRegistry().registerAll(RECIPES.toArray(new IRecipe[RECIPES.size()]));
	}

	@SubscribeEvent
	public void registerSounds(Register<SoundEvent> event)
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
		ArcaneMagicSoundHandler.register("infuser", registry);
		ArcaneMagicSoundHandler.register("infuser_start", registry);
		ArcaneMagicSoundHandler.register("camera_clack_1", registry);
		ArcaneMagicSoundHandler.register("camera_clack_2", registry);
		ArcaneMagicSoundHandler.register("camera_clack_3", registry);
	}

	public static void registerTiles()
	{
		GameRegistry.registerTileEntity(TileEntityElementalCraftingTable.class, ArcaneMagic.MODID + "_elemental_crafting_table");
		GameRegistry.registerTileEntity(TileEntityCrystallizer.class, ArcaneMagic.MODID + "_crystallizer");
		GameRegistry.registerTileEntity(TileEntityEssenceConcentrator.class,
				ArcaneMagic.MODID + "_essence_concentrator");
		GameRegistry.registerTileEntity(TileEntityWritingDesk.class,
				ArcaneMagic.MODID + "_writing_desk");
	}
}
