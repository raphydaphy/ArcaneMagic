package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.AltarBlock;
import com.raphydaphy.arcanemagic.block.TransfigurationTableBlock;
import com.raphydaphy.arcanemagic.block.entity.AltarBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.item.PendantItem;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ModRegistry
{
	public static BlockEntityType<AltarBlockEntity> ALTAR_TE = Registry.register(Registry.BLOCK_ENTITY, ArcaneMagic.PREFIX + "altar", BlockEntityType.Builder.create(AltarBlockEntity::new).build(null));
	public static BlockEntityType<TransfigurationTableBlockEntity> TRANSFIGURATION_TABLE_TE = Registry.register(Registry.BLOCK_ENTITY, ArcaneMagic.PREFIX + "transfiguration_table", BlockEntityType.Builder.create(TransfigurationTableBlockEntity::new).build(null));

	public static AltarBlock ALTAR = new AltarBlock();
	public static TransfigurationTableBlock TRANSFIGURATION_TABLE = new TransfigurationTableBlock();

	public static ScepterItem GOLDEN_SCEPTER = new ScepterItem(20);
	public static PendantItem SOUL_PENDANT = new PendantItem();
	public static Item EMERALD_CRYSTAL = new Item(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static Item DIAMOND_CRYSTAL = new Item(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static Item GOLD_CRYSTAL = new Item(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static Item LAPIS_CRYSTAL = new Item(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static Item REDSTONE_CRYSTAL = new Item(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static Item COAL_CRYSTAL = new Item(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static ArmorItem EMERALD_CRYSTAL_HELMET = new ArmorItem(ModArmorMaterials.EMERALD_CRYSTAL, EquipmentSlot.HEAD, new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static ArmorItem EMERALD_CRYSTAL_CHESTPLATE = new ArmorItem(ModArmorMaterials.EMERALD_CRYSTAL, EquipmentSlot.CHEST, new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static ArmorItem EMERALD_CRYSTAL_LEGGINGS = new ArmorItem(ModArmorMaterials.EMERALD_CRYSTAL, EquipmentSlot.LEGS, new Item.Settings().itemGroup(ArcaneMagic.GROUP));
	public static ArmorItem EMERALD_CRYSTAL_BOOTS = new ArmorItem(ModArmorMaterials.EMERALD_CRYSTAL, EquipmentSlot.FEET, new Item.Settings().itemGroup(ArcaneMagic.GROUP));

	public static final List<TransfigurationRecipe> TRANSFIGURATION_RECIPES = new ArrayList<>();

	public static void init()
	{
		// Block Registration
		Registry.register(Registry.BLOCK, new Identifier(ArcaneMagic.DOMAIN, "altar"), ALTAR);
		Registry.register(Registry.BLOCK, new Identifier(ArcaneMagic.DOMAIN, "transfiguration_table"), TRANSFIGURATION_TABLE);

		// Item Block Registration
		Registry.register(Registry.ITEM, ArcaneMagic.PREFIX + "altar", new BlockItem(ALTAR, new Item.Settings().itemGroup(ArcaneMagic.GROUP)));
		Registry.register(Registry.ITEM, ArcaneMagic.PREFIX + "transfiguration_table", new BlockItem(TRANSFIGURATION_TABLE, new Item.Settings().itemGroup(ArcaneMagic.GROUP)));

		// Item Registration
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "golden_scepter"), GOLDEN_SCEPTER);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "soul_pendant"), SOUL_PENDANT);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "emerald_crystal"), EMERALD_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "diamond_crystal"), DIAMOND_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "gold_crystal"), GOLD_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "lapis_crystal"), LAPIS_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "redstone_crystal"), REDSTONE_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "coal_crystal"), COAL_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "emerald_crystal_helmet"), EMERALD_CRYSTAL_HELMET);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "emerald_crystal_chestplate"), EMERALD_CRYSTAL_CHESTPLATE);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "emerald_crystal_leggings"), EMERALD_CRYSTAL_LEGGINGS);
		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "emerald_crystal_boots"), EMERALD_CRYSTAL_BOOTS);

		// Transfiguration Recipes
		new TransfigurationRecipe(new ItemStack(SOUL_PENDANT), 10,
				ItemStack.EMPTY, new ItemStack(Blocks.STONE), ItemStack.EMPTY,
				new ItemStack(Blocks.STONE), new ItemStack(GOLD_CRYSTAL), new ItemStack(Blocks.STONE),
				ItemStack.EMPTY, new ItemStack(Blocks.STONE), ItemStack.EMPTY);
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_HELMET), 10,
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL));
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_CHESTPLATE), 14,
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL));
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_LEGGINGS), 12,
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL));
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_BOOTS), 8,
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL));
	}
}
