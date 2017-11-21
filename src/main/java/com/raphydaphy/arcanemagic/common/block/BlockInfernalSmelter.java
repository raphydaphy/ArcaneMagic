package com.raphydaphy.arcanemagic.common.block;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.anima.IAnimaCrystal;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityInfernalSmelter;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;

import net.minecraft.block.SoundType;
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

// TODO: a better name goddamit
public class BlockInfernalSmelter extends BlockBase implements IHasRecipe
{
	protected static final List<AxisAlignedBB> BOUNDS = new ArrayList<>();

	static
	{
		BOUNDS.add(makeAABB(0, 0, 0, 16, 16, 16));
	}

	public IRecipe recipe;

	public BlockInfernalSmelter()
	{
		super("infernal_smelter", Material.ROCK, 4, SoundType.STONE);

		this.setRenderedAABB(makeAABB(0, 0, 0, 16, 16, 16));
		this.setCollisionAABBList(BOUNDS);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityInfernalSmelter te = (TileEntityInfernalSmelter) world.getTileEntity(pos);
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
		return new TileEntityInfernalSmelter();
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		TileEntity teUnchecked = world.getTileEntity(pos);
		if (!(teUnchecked instanceof TileEntityInfernalSmelter))
		{
			return false;
		}
		TileEntityInfernalSmelter te = (TileEntityInfernalSmelter) teUnchecked;

		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		if (cap != null)
		{
			ItemStack stack = player.getHeldItem(hand);
			if (!stack.isEmpty() && !player.isSneaking())
			{
				ItemStack insertStack = stack.copy();
				insertStack.setCount(1);

				if (stack.getItem() instanceof IAnimaCrystal)
				{
					for (int slot = TileEntityInfernalSmelter.ORE + 1; slot < TileEntityInfernalSmelter.SIZE; slot++)
					{
						if (cap.getStackInSlot(slot).isEmpty())
						{
							if (cap.insertItem(slot, insertStack, true).isEmpty())
							{
								cap.insertItem(slot, insertStack, false);

								stack.shrink(1);
								player.setHeldItem(hand, stack);

								te.markDirty();
								world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM,
										SoundCategory.BLOCKS, 1, 1);

								return true;
							}
						}
					}
				} else if (cap.getStackInSlot(TileEntityInfernalSmelter.ORE).isEmpty())
				{
					if (cap.insertItem(TileEntityInfernalSmelter.ORE, insertStack, true).isEmpty())
					{
						cap.insertItem(TileEntityInfernalSmelter.ORE, insertStack, false);
						stack.shrink(1);

						player.setHeldItem(hand, stack);

						te.markDirty();
						world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1,
								1);

						return true;
					}
				}
			} else if (player.isSneaking())
			{

				for (int slot = TileEntityInfernalSmelter.SIZE - 1; slot > -1; slot--)
				{
					System.out.println(slot);
					ItemStack toExtract = cap.getStackInSlot(slot).copy();
					if (!toExtract.isEmpty())
					{
						if (cap.extractItem(slot, 1, true).getCount() == toExtract.getCount())
						{
							if (player.addItemStackToInventory(toExtract.copy()))
							{
								cap.extractItem(slot, Math.min(toExtract.getCount(), cap.getSlotLimit(slot)), false);
								te.markDirty();
								world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM,
										SoundCategory.BLOCKS, 1, 1);

								return true;
							}

						}

					}
				}
			}
		}

		return false;
	}

	@Override
	public void initRecipes(Register<IRecipe> e)
	{
		// TODO: recipe
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}
