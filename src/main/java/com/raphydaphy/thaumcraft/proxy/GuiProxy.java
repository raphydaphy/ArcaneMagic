package com.raphydaphy.thaumcraft.proxy;

import com.raphydaphy.thaumcraft.gui.GuiThaumonomicon;
import com.raphydaphy.thaumcraft.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	switch(ID)
        {
	        case 1:
	        {
	        	if (player.getHeldItemMainhand().getItem() == ModItems.thaumonomicon || player.getHeldItemOffhand().getItem() == ModItems.thaumonomicon)
	        	{
	        		return new GuiThaumonomicon(player);
	        	}
	        }
        }
        return null;
    }
}