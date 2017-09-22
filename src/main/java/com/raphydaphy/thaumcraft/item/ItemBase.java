package com.raphydaphy.thaumcraft.item;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item
{
	public ItemBase(String name) 
	{
		setRegistryName(name);
		setUnlocalizedName(Thaumcraft.MODID + "." + name);
		setCreativeTab(Thaumcraft.creativeTab);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
