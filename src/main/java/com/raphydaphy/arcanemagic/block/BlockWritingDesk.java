package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.tileentity.TileEntityWritingDesk;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockWritingDesk extends BlockBase
{
	public static final int GUI_ID = 3;

	public BlockWritingDesk()
	{
		super("writing_desk", Material.ROCK, 2.5f);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityWritingDesk te = (TileEntityWritingDesk) world.getTileEntity(pos);
		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		for (int i = 0; i < cap.getSlots(); ++i)
		{
			ItemStack itemstack = cap.getStackInSlot(i);

			if (!itemstack.isEmpty())
			{
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityWritingDesk();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileEntityWritingDesk))
		{
			return false;
		}
		player.openGui(ArcaneMagic.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
