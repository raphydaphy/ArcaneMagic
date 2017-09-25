package com.raphydaphy.thaumcraft.item;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item
{
	private final int variants;
	public ItemBase(String name)
	{
		this(name, 1);
	}
	
	public ItemBase(String name, int variants)
	{
		setRegistryName(name);
		setUnlocalizedName(Thaumcraft.MODID + "." + name);
		setCreativeTab(Thaumcraft.creativeTab);
		
		this.variants = variants;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 5, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return null;
	}
	
	@Override
	 public int getMetadata(int damage)
    {
		if (variants > 1)
		{
			return damage;
		}
        return 0;
    }
}
