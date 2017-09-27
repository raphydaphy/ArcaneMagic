package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBase extends Item implements IHasModel
{
	private final int variants;

	public ItemBase(String name)
	{
		this(name, 0);
	}

	public ItemBase(String name, int variants)
	{
		setRegistryName(name);
		setUnlocalizedName(ArcaneMagic.MODID + "." + name);
		setCreativeTab(ArcaneMagic.creativeTab);
		this.variants = variants;
		if (variants > 0)
			setHasSubtypes(true);
		init();
	}

	@Override
	public void initModels(ModelRegistryEvent e)
	{
		for (int i = 0; i < variants; i++)
			ModelLoader.setCustomModelResourceLocation(this, i,
					new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
		{
			if(variants == 0) items.add(new ItemStack(this));
			else for (int i = 0; i < variants; i++)
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
}
