package com.raphydaphy.arcanemagic.common.block;

import com.raphydaphy.arcanemagic.common.tileentity.TileEntityElementalBurner;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;

public class BlockElementalBurner extends BlockBase implements IHasRecipe
{
	public static final int GUI_ID = 3;
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 6d * (1d / 16d), 1.0D);

	public BlockElementalBurner()
	{
		super("elemental_burner", Material.ROCK, 2.5f, SoundType.STONE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityElementalBurner te = (TileEntityElementalBurner) world.getTileEntity(pos);

		ItemStack itemstack = te.getStack();

		if (!itemstack.isEmpty())
		{
			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
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
		return new TileEntityElementalBurner();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
		{
			if (!world.isRemote)
			{
				TileEntityElementalBurner te = (TileEntityElementalBurner) world.getTileEntity(pos);
				if (te.getStack().isEmpty())
				{
					if (!player.getHeldItem(hand).isEmpty())
					{
						ItemStack heldItemClone = player.getHeldItem(hand).copy();
						heldItemClone.setCount(1);

						if (player.getHeldItem(hand).getCount() > 1)
						{
							player.getHeldItem(hand).shrink(1);
						} else
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
						}
						te.setStack(heldItemClone);

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
		return false;
	}

	@Override
	public void initRecipes(Register<IRecipe> e)
	{
		RecipeHelper.addElementalShaped(this, null, 0, null, Items.REDSTONE, null, Items.GLOWSTONE_DUST,
				Blocks.IRON_BLOCK, Items.GLOWSTONE_DUST, Blocks.IRON_BLOCK, Blocks.IRON_BLOCK, Blocks.IRON_BLOCK);
	}
}
