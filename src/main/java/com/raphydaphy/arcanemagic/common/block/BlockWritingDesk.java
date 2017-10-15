package com.raphydaphy.arcanemagic.common.block;

import com.raphydaphy.arcanemagic.common.tileentity.TileEntityWritingDesk;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockWritingDesk extends BlockBase implements IHasRecipe
{
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 10d * (1d / 16d), 1.0D);

	public BlockWritingDesk()
	{
		super("writing_desk", Material.WOOD, 2.5f);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
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

		ItemStack stack = player.getHeldItem(hand);

		int slot = 0;
		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		if (stack != null && !stack.isEmpty() && !player.isSneaking())
		{
			ItemStack insertStack = stack.copy();
			ItemStack remain = cap.insertItem(slot, insertStack, false);
			if (remain.getCount() != insertStack.getCount())
			{
				if (!world.isRemote)
				{
					player.setHeldItem(hand, remain);
					te.markDirty();
				} else
				{
					world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, SoundCategory.BLOCKS, 1, 1);
				}
			}

		} else if (player.isSneaking())
		{
			ItemStack toExtract = cap.getStackInSlot(slot);
			if (toExtract != null && !toExtract.isEmpty())
			{
				if (!world.isRemote)
				{
					if (player.addItemStackToInventory(toExtract.copy()))
					{
						cap.getStackInSlot(slot).setCount(0);
						te.markDirty();
					}
				} else
				{
					world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1, 1);
				}
			}
		}

		return true;
	}

	@Override
	public void initRecipes(Register<IRecipe> e)
	{
		RecipeHelper.addShaped(this, 3, 3, "paper", "dyeBlack", "paper", "plankWood", "plankWood", "plankWood",
				"plankWood", null, "plankWood");
	}
}
