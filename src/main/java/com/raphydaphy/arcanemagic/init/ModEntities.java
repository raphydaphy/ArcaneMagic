package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
	public static void init()
	{
		int entityID = 0;

		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneMagic.MODID, "entity_item_fancy"),
				EntityItemFancy.class, "entity_item_fancy", entityID++, ArcaneMagic.instance, 64, 20, true);
	}
}
