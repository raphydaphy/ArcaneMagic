package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.entity.EntityMagicCircles;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
	public static void init()
	{
		int entityID = 0;

		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneMagic.MODID, "item_fancy"), EntityItemFancy.class,
				"item_fancy", entityID++, ArcaneMagic.instance, 64, 20, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneMagic.MODID, "entity_magic_circles"),
				EntityMagicCircles.class, "magic_circles", entityID++, ArcaneMagic.instance, 64, 20, true);
	}
}
