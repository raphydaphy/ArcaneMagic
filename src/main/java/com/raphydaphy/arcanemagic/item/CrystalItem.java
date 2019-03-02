package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.world.World;

import java.util.List;

public class CrystalItem extends Item
{
	public final ArcaneMagicUtils.ForgeCrystal type;

	public CrystalItem(ArcaneMagicUtils.ForgeCrystal type)
	{
		super(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
		this.type = type;
	}
}
