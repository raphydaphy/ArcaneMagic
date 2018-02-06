package com.raphydaphy.arcanemagic.client.proxy;

import com.raphydaphy.arcanemagic.client.gui.GuiAnimusMaterializer;
import com.raphydaphy.arcanemagic.client.gui.GuiAltar;
import com.raphydaphy.arcanemagic.client.gui.GuiNotebook;
import com.raphydaphy.arcanemagic.common.block.BlockAnimusMaterializer;
import com.raphydaphy.arcanemagic.common.block.BlockAltar;
import com.raphydaphy.arcanemagic.common.container.ContainerAnimusMaterializer;
import com.raphydaphy.arcanemagic.common.container.ContainerInfusionAltar;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.item.ItemNotebook;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimusMaterializer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAltar;

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
		case BlockAnimusMaterializer.GUI_ID:

			if (te instanceof TileEntityAnimusMaterializer)
				return new ContainerAnimusMaterializer(player.inventory, (TileEntityAnimusMaterializer) te);
			break;
		case BlockAltar.GUI_ID:
			if (te instanceof TileEntityAltar)
				return new ContainerInfusionAltar(player.inventory, (TileEntityAltar) te);
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
		case BlockAnimusMaterializer.GUI_ID:
			if (te instanceof TileEntityAnimusMaterializer)
			{
				return new GuiAnimusMaterializer((TileEntityAnimusMaterializer) te,
						new ContainerAnimusMaterializer(player.inventory, (TileEntityAnimusMaterializer) te));
			}
			break;
		case BlockAltar.GUI_ID:
			if (te instanceof TileEntityAltar)
			{
				return new GuiAltar((TileEntityAltar) te,
						new ContainerInfusionAltar(player.inventory, (TileEntityAltar) te));
			}
			break;
		}
		return null;
	}
}