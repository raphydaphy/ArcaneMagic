package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.AltarBlock;
import com.raphydaphy.arcanemagic.block.CrystalInfuserBlock;
import com.raphydaphy.arcanemagic.block.TransfigurationTableBlock;
import com.raphydaphy.arcanemagic.block.entity.AltarBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.item.CrystalArmorItem;
import com.raphydaphy.arcanemagic.item.CrystalItem;
import com.raphydaphy.arcanemagic.item.SoulStorageItem;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
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
	public static BlockEntityType<CrystalInfuserBlockEntity> CRYSTAL_INFUSER_TE = Registry.register(Registry.BLOCK_ENTITY, ArcaneMagic.PREFIX + "crystal_infuser", BlockEntityType.Builder.create(CrystalInfuserBlockEntity::new).build(null));
	public static BlockEntityType<TransfigurationTableBlockEntity> TRANSFIGURATION_TABLE_TE = Registry.register(Registry.BLOCK_ENTITY, ArcaneMagic.PREFIX + "transfiguration_table", BlockEntityType.Builder.create(TransfigurationTableBlockEntity::new).build(null));

	public static AltarBlock ALTAR = new AltarBlock();
	public static CrystalInfuserBlock CRYSTAL_INFUSER = new CrystalInfuserBlock();
	public static TransfigurationTableBlock TRANSFIGURATION_TABLE = new TransfigurationTableBlock();

	public static ScepterItem GOLDEN_SCEPTER = new ScepterItem(20);
	public static SoulStorageItem SOUL_PENDANT = new SoulStorageItem(new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
	public static CrystalItem EMERALD_CRYSTAL = new CrystalItem(ArcaneMagicUtils.ForgeCrystal.EMERALD);
	public static CrystalItem DIAMOND_CRYSTAL = new CrystalItem(ArcaneMagicUtils.ForgeCrystal.DIAMOND);
	public static CrystalItem GOLD_CRYSTAL = new CrystalItem(ArcaneMagicUtils.ForgeCrystal.GOLD);
	public static CrystalItem LAPIS_CRYSTAL = new CrystalItem(ArcaneMagicUtils.ForgeCrystal.LAPIS);
	public static CrystalItem REDSTONE_CRYSTAL = new CrystalItem(ArcaneMagicUtils.ForgeCrystal.REDSTONE);
	public static CrystalItem COAL_CRYSTAL = new CrystalItem(ArcaneMagicUtils.ForgeCrystal.COAL);
	public static CrystalArmorItem EMERALD_CRYSTAL_HELMET = new CrystalArmorItem(EquipmentSlot.HEAD);
	public static CrystalArmorItem EMERALD_CRYSTAL_CHESTPLATE = new CrystalArmorItem(EquipmentSlot.CHEST);
	public static CrystalArmorItem EMERALD_CRYSTAL_LEGGINGS = new CrystalArmorItem(EquipmentSlot.LEGS);
	public static CrystalArmorItem EMERALD_CRYSTAL_BOOTS = new CrystalArmorItem(EquipmentSlot.FEET);

	public static final List<TransfigurationRecipe> TRANSFIGURATION_RECIPES = new ArrayList<>();

	public static void init()
	{
		// Block Registration
		Registry.register(Registry.BLOCK, new Identifier(ArcaneMagic.DOMAIN, "altar"), ALTAR);
		Registry.register(Registry.BLOCK, new Identifier(ArcaneMagic.DOMAIN, "crystal_infuser"), CRYSTAL_INFUSER);
		Registry.register(Registry.BLOCK, new Identifier(ArcaneMagic.DOMAIN, "transfiguration_table"), TRANSFIGURATION_TABLE);

		// Item Block Registration
		Registry.register(Registry.ITEM, ArcaneMagic.PREFIX + "altar", new BlockItem(ALTAR, new Item.Settings().itemGroup(ArcaneMagic.GROUP)));
		Registry.register(Registry.ITEM, ArcaneMagic.PREFIX + "crystal_infuser", new BlockItem(CRYSTAL_INFUSER, new Item.Settings().itemGroup(ArcaneMagic.GROUP)));
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
		new TransfigurationRecipe(new ItemStack(SOUL_PENDANT), 20,
				ItemStack.EMPTY, new ItemStack(Blocks.STONE), ItemStack.EMPTY,
				new ItemStack(Blocks.STONE), new ItemStack(GOLD_CRYSTAL), new ItemStack(Blocks.STONE),
				ItemStack.EMPTY, new ItemStack(Blocks.STONE), ItemStack.EMPTY);
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_HELMET), 34,
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL));
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_CHESTPLATE), 38,
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL));
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_LEGGINGS), 36,
				new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL), new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL));
		new TransfigurationRecipe(new ItemStack(EMERALD_CRYSTAL_BOOTS), 32,
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL),
				new ItemStack(EMERALD_CRYSTAL), ItemStack.EMPTY, new ItemStack(EMERALD_CRYSTAL));
	}
}
