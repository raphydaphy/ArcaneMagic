package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class DaggerItem extends SwordItem implements ICrystalEquipment
{
	public DaggerItem(ToolMaterial material, int damage, float speed)
	{
		super(material, damage, speed, new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
	}
}
