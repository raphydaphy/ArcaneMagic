package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IHasModel
{
	protected final int variants;
	protected final TextFormatting color;

	public ItemBase(String name)
	{
		this(name, 0, TextFormatting.WHITE);
	}

	public ItemBase(String name, int variants)
	{
		this(name, variants, TextFormatting.WHITE);
	}

	public ItemBase(String name, TextFormatting color)
	{
		this(name, 0, color);
	}

	public ItemBase(String name, int variants, TextFormatting color)
	{
		setRegistryName(name);
		setUnlocalizedName(ArcaneMagic.MODID + "." + name);
		setCreativeTab(ArcaneMagic.creativeTab);
		this.variants = variants;
		this.color = color;
		if (variants > 0)
			setHasSubtypes(true);
		init();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack)
	{
		return color + I18n.format(this.getUnlocalizedName(stack) + ".name").trim();
	}

	@Override
	public void initModels(ModelRegistryEvent e)
	{
		for (int i = 0; variants > 0 ? i < variants : i <= 0; i++)
		{
			IHasModel.sMRL("items", this, i, "item=" + getRegistryName().getResourcePath() + (i == 0 ? "" : i));
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
		{
			if (variants == 0)
				items.add(new ItemStack(this));
			else
				for (int i = 0; i < variants; i++)
				{
					items.add(new ItemStack(this, 1, i));
				}
		}
	}

	@Override
	public int getMetadata(int damage)
	{
		if (variants > 0)
		{
			return damage;
		}
		return 0;
	}

	public void init()
	{
		ModRegistry.ITEMS.add(this);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if (variants > 0)
		{
			return super.getUnlocalizedName() + "." + stack.getMetadata();
		}
		return super.getUnlocalizedName();
	}
}
