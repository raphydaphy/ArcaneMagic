package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.item.Item;

public class NotebookItem extends Item
{
	public NotebookItem()
	{
		super(new Item.Settings().stackSize(1).itemGroup(ArcaneMagic.GROUP));
	}
}
