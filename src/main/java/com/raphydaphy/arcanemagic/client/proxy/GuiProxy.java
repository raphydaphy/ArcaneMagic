package com.raphydaphy.arcanemagic.client.proxy;

import com.raphydaphy.arcanemagic.client.gui.GuiCrystallizer;
import com.raphydaphy.arcanemagic.client.gui.GuiNotebook;
import com.raphydaphy.arcanemagic.client.gui.GuiWritingDesk;
import com.raphydaphy.arcanemagic.common.block.BlockCrystallizer;
import com.raphydaphy.arcanemagic.common.block.BlockWritingDesk;
import com.raphydaphy.arcanemagic.common.container.ContainerCrystallizer;
import com.raphydaphy.arcanemagic.common.container.ContainerWritingDesk;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.item.ItemNotebook;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityCrystallizer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityWritingDesk;

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
		case BlockWritingDesk.GUI_ID:
			if (te instanceof TileEntityWritingDesk)
				return new ContainerWritingDesk(player.inventory, (TileEntityWritingDesk) te);
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
			break;
		case BlockWritingDesk.GUI_ID:
			if (te instanceof TileEntityWritingDesk)
			{
				TileEntityWritingDesk containerTE = (TileEntityWritingDesk) te;
				return new GuiWritingDesk(containerTE, new ContainerWritingDesk(player.inventory, containerTE));
			}
			break;
		}
		return null;
	}
}