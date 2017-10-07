package com.raphydaphy.arcanemagic.proxy;

import com.raphydaphy.arcanemagic.block.BlockCrystallizer;
import com.raphydaphy.arcanemagic.client.gui.GuiCrystallizer;
import com.raphydaphy.arcanemagic.client.gui.GuiNotebook;
import com.raphydaphy.arcanemagic.container.ContainerCrystallizer;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ItemNotebook;
import com.raphydaphy.arcanemagic.tileentity.TileEntityCrystallizer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		switch (ID)
		{
		case BlockCrystallizer.GUI_ID:

			if (te instanceof TileEntityCrystallizer)
				return new ContainerCrystallizer(player.inventory, (TileEntityCrystallizer) te);
			break;

		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		switch (ID)
		{
		case ItemNotebook.GUI_ID:
			if (player.getHeldItemMainhand().getItem() == ModRegistry.NOTEBOOK
					|| player.getHeldItemOffhand().getItem() == ModRegistry.NOTEBOOK)
			{
				return new GuiNotebook(player);
			}
			break;
		case BlockCrystallizer.GUI_ID:

			if (te instanceof TileEntityCrystallizer)
			{
				TileEntityCrystallizer containerTileEntity = (TileEntityCrystallizer) te;
				return new GuiCrystallizer(containerTileEntity,
						new ContainerCrystallizer(player.inventory, containerTileEntity));
			}
		}
		return null;
	}
}