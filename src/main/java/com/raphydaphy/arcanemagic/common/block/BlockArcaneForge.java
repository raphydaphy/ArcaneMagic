package com.raphydaphy.arcanemagic.common.block;

import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneForge;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
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

public class BlockArcaneForge extends BlockBase implements IHasRecipe {
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D);

	public BlockArcaneForge() {
		super("arcane_forge", Material.ROCK, 4f, SoundType.STONE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityArcaneForge te = (TileEntityArcaneForge) world.getTileEntity(pos);

		ItemStack[] stacks = { te.getWeapon(), te.getGem(0), te.getGem(1) };

		for (ItemStack stack : stacks) {
			if (!stack.isEmpty()) {
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityArcaneForge();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			if (!world.isRemote) {
				TileEntityArcaneForge te = (TileEntityArcaneForge) world.getTileEntity(pos);
				if (!player.getHeldItem(hand).isEmpty()) {
					boolean didRemove = false;
					ItemStack heldItemClone = player.getHeldItem(hand).copy();
					heldItemClone.setCount(1);
					if (player.getHeldItem(hand).getItem().equals(ModRegistry.ARCANE_DAGGER)
							&& te.getWeapon().isEmpty()) {

						te.setWeapon(heldItemClone);
						didRemove = true;

					} else if (player.getHeldItem(hand).getItem().equals(Items.DIAMOND)
							|| player.getHeldItem(hand).getItem().equals(Items.EMERALD)) {
						if (te.getGem(0).isEmpty()) {
							te.setGem(heldItemClone, 0);
							didRemove = true;
						} else if (te.getGem(1).isEmpty()) {
							te.setGem(heldItemClone, 1);
							didRemove = true;
						}
					}

					if (didRemove) {
						if (player.getHeldItem(hand).getCount() > 1) {
							player.getHeldItem(hand).shrink(1);
						} else {
							player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
						}
						player.openContainer.detectAndSendChanges();
					}
				} else {
					ItemStack stack = ItemStack.EMPTY;
					if (!te.getGem(1).isEmpty()) {
						stack = te.getGem(1).copy();
						te.setGem(ItemStack.EMPTY, 1);
					} else if (!te.getGem(0).isEmpty()) {
						stack = te.getGem(0).copy();
						te.setGem(ItemStack.EMPTY, 0);
					} else if (!te.getWeapon().isEmpty()) {
						stack = te.getWeapon().copy();
						te.setWeapon(ItemStack.EMPTY);
					}

					if (!stack.isEmpty()) {
						InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), stack);
					}
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public void initRecipes(Register<IRecipe> e) {
		RecipeHelper.addElementalShaped(this, null, 0, null, Items.IRON_AXE, null, Blocks.OBSIDIAN, Blocks.ANVIL,
				Blocks.OBSIDIAN, Blocks.IRON_BLOCK, Blocks.IRON_BLOCK, Blocks.IRON_BLOCK);
	}
}
