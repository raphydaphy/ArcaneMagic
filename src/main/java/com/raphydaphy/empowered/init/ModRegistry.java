package com.raphydaphy.empowered.init;

import com.raphydaphy.empowered.Empowered;
import com.raphydaphy.empowered.block.TransfigurationTableBlock;
import com.raphydaphy.empowered.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.empowered.item.ChannelingRodItem;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRegistry
{

	public static BlockEntityType<TransfigurationTableBlockEntity> TRANSFIGURATION_TABLE_TE = Registry.register(Registry.BLOCK_ENTITY, Empowered.PREFIX + "transfiguration_table", BlockEntityType.Builder.create(TransfigurationTableBlockEntity::new).build(null));

	public static TransfigurationTableBlock TRANSFIGURATION_TABLE = new TransfigurationTableBlock();

	public static ChannelingRodItem CHANNELING_ROD = new ChannelingRodItem();

	public static void init()
	{
		Registry.register(Registry.BLOCK, new Identifier(Empowered.DOMAIN, "transfiguration_table"), TRANSFIGURATION_TABLE);
		Registry.register(Registry.ITEM, Empowered.PREFIX + "transfiguration_table", new BlockItem(TRANSFIGURATION_TABLE, new Item.Settings().itemGroup(Empowered.GROUP)));

		Registry.register(Registry.ITEM, new Identifier(Empowered.DOMAIN, "channeling_rod"), CHANNELING_ROD);

	}
}
