package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TextComponent;
import net.minecraft.world.World;

import java.util.List;

public class SoulStorageItem extends Item
{
	public SoulStorageItem(Item.Settings settings)
	{
		super(settings);
	}
}
