package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceConcentrator;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEssenceConcentrator extends BlockBase
{
	public static final int GUI_ID = 3;
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 6d * (1d / 16d), 1.0D);

	public BlockEssenceConcentrator()
	{
		super("essence_concentrator", Material.ROCK, 2.5f);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
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
		return new TileEntityEssenceConcentrator();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntityEssenceConcentrator te = (TileEntityEssenceConcentrator) world.getTileEntity(pos);
			if (te.getStack().isEmpty())
			{
				if (!player.getHeldItem(hand).isEmpty())
				{
					ItemStack heldItemClone = player.getHeldItem(hand).copy();

					if (heldItemClone.getCount() > 1)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem,
								new ItemStack(heldItemClone.getItem(), heldItemClone.getCount() - 1));
					} else
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
					}
					te.setStack(new ItemStack(heldItemClone.getItem(), 1));

					player.openContainer.detectAndSendChanges();
				}
			} else
			{
				ItemStack stack = te.getStack();
				te.setStack(ItemStack.EMPTY);
				if (!player.inventory.addItemStackToInventory(stack))
				{
					EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack);
					world.spawnEntity(entityItem);
				} else
				{
					player.openContainer.detectAndSendChanges();
				}
			}
		}

		return true;
	}
}