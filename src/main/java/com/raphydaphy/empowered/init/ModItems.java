package com.raphydaphy.empowered.init;

import com.raphydaphy.empowered.Empowered;
import com.raphydaphy.empowered.item.ItemChannelingRod;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems
{
	public static final ItemChannelingRod CHANNELING_ROD = new ItemChannelingRod();

	public static void init()
	{
		Empowered.getLogger().info("Registering Items");
		Registry.register(Registry.ITEM, new Identifier(Empowered.DOMAIN, "channeling_rod"), CHANNELING_ROD);
	}
}
