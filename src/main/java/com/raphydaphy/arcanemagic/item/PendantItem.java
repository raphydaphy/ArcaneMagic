package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.sun.istack.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.world.World;

import java.util.List;

public class PendantItem extends Item
{
	public PendantItem()
	{
		super(new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void buildTooltip(ItemStack stack, @Nullable World world, List<TextComponent> tooltip, TooltipContext ctx)
	{
		int soul = 0;
		if (stack.getTag() != null)
		{
			soul = stack.getTag().getInt(ArcaneMagicConstants.SOUL_KEY);
		}
		tooltip.add(new StringTextComponent("Storing " + soul + " Soul"));
	}
}
