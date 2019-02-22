package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.TransfigurationTableBlock;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRegistry
{
	public static BlockEntityType<TransfigurationTableBlockEntity> TRANSFIGURATION_TABLE_TE = Registry.register(Registry.BLOCK_ENTITY, ArcaneMagic.PREFIX + "transfiguration_table", BlockEntityType.Builder.create(TransfigurationTableBlockEntity::new).build(null));

	public static TransfigurationTableBlock TRANSFIGURATION_TABLE = new TransfigurationTableBlock();

	public static ScepterItem GOLDEN_SCEPTER = new ScepterItem();

	public static void init()
	{
		Registry.register(Registry.BLOCK, new Identifier(ArcaneMagic.DOMAIN, "transfiguration_table"), TRANSFIGURATION_TABLE);
		Registry.register(Registry.ITEM, ArcaneMagic.PREFIX + "transfiguration_table", new BlockItem(TRANSFIGURATION_TABLE, new Item.Settings().itemGroup(ArcaneMagic.GROUP)));

		Registry.register(Registry.ITEM, new Identifier(ArcaneMagic.DOMAIN, "golden_scepter"), GOLDEN_SCEPTER);
	}
}
