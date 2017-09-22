package com.raphydaphy.thaumcraft.block;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block 
{
	public BlockBase(String name, Material material, float hardness) 
	{
		super(material);
		setUnlocalizedName(Thaumcraft.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(Thaumcraft.creativeTab);
		setHardness(hardness);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
