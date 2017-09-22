package com.raphydaphy.thaumcraft.init;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.entity.EntityItemFancy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities 
{
	public static void init()
	{
		int entityID = 0;
		
		EntityRegistry.registerModEntity(new ResourceLocation(Thaumcraft.MODID, "entity_item_fancy"), EntityItemFancy.class, "entity_item_fancy", entityID++, Thaumcraft.instance, 64, 20, true);
	}
}
