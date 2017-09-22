package com.raphydaphy.thaumcraft.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemFancy extends EntityItem 
{
	public EntityItemFancy(World worldIn) 
    {
        super(worldIn);
    }
	
    public EntityItemFancy(World worldIn, double x, double y, double z, ItemStack stack) 
    {
        super(worldIn, x, y, z, stack);
    }
}