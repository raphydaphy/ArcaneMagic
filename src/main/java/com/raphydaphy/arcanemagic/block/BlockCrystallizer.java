package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.tileentity.TileEntityCrystallizer;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockCrystallizer extends BlockBase
{
	public static final int GUI_ID = 2;
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

	public BlockCrystallizer()
	{
		super("crystallizer", Material.ROCK, 2.5f);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityCrystallizer te = (TileEntityCrystallizer) world.getTileEntity(pos);
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
		return new TileEntityCrystallizer();
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
		if (!(te instanceof TileEntityCrystallizer))
		{
			return false;
		}
		player.openGui(ArcaneMagic.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
