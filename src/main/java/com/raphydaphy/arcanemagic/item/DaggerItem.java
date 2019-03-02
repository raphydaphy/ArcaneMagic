package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.world.World;

import java.util.List;

public class DaggerItem extends SwordItem implements ICrystalEquipment
{
	private final float speed;
	public DaggerItem(ToolMaterial material, int damage, float speed)
	{
		super(material, damage, speed, new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
		this.speed = speed;
	}

	public float getSpeed()
	{
		return this.speed;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> lines, TooltipContext ctx)
	{
		if (stack.getTag() != null)
		{
			if (stack.getTag().containsKey(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY))
			{
				lines.add(new StringTextComponent("Active Crystal: " + stack.getTag().getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY)));
			}
			if (stack.getTag().containsKey(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY))
			{
				lines.add(new StringTextComponent("Passive Crystal: " + stack.getTag().getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY)));
			}
		}
	}
}
