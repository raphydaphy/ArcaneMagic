package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.common.data.IPropertyEnum;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;

public class ItemEnum<E extends Enum<E> & IPropertyEnum> extends ItemBase
{

	protected final E[] values;

	public ItemEnum(String name, E[] values)
	{
		super(name, values.length);
		this.values = values;
	}

	@Override
	public void initModels(ModelRegistryEvent ev)
	{
		for (E e : values)
			IHasModel.sMRL("items", this, e.ordinal(), "item=" + e.getName());
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
			for (E e : values)
			{
				items.add(new ItemStack(this, 1, e.ordinal()));
			}
	}

	@Override
	public int getMetadata(int damage)
	{
		return MathHelper.clamp(damage, 0, values.length - 1);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item.arcanemagic." + values[stack.getMetadata()].getName();
	}

}
