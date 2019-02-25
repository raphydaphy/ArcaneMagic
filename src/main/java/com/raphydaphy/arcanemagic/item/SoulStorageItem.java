package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.world.World;

import java.util.List;

public class SoulStorageItem extends Item
{
	public SoulStorageItem(Item.Settings settings)
	{
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> tooltip, TooltipContext ctx)
	{
		int soul = 0;
		if (stack.getTag() != null)
		{
			soul = stack.getTag().getInt(ArcaneMagicConstants.SOUL_KEY);
		}
		// TODO: render soul graphic on mouseover
		//tooltip.add(new StringTextComponent("Storing " + soul + " Soul"));
	}
}
