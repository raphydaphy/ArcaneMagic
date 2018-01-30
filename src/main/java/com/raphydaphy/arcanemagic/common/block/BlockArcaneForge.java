package com.raphydaphy.arcanemagic.common.block;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.util.EnumParticleTypes;
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
			TileEntityArcaneForge te = (TileEntityArcaneForge) world.getTileEntity(pos);
			if (!world.isRemote) {
				
				if (!player.getHeldItem(hand).isEmpty()) {
					if (player.getHeldItem(hand).getItem().equals(Items.IRON_AXE)) {
						List<Integer> gems = new ArrayList<>();
						if (!te.getGem(0).isEmpty() && te.getDepth(0) > 0) {
							gems.add(0);
						}
						if (!te.getGem(1).isEmpty() && te.getDepth(1) > 0) {
							gems.add(1);
						}

						for (int gem : gems) {
							if (world.rand.nextInt(5) == 1) {
								te.setDepth(te.getDepth(gem) - 1, gem);
							}
						}

						if (te.getDepth(0) == 0 && te.getDepth(1) == 0) {

							InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 0.5, pos.getZ(),
									te.getWeapon().copy());

							te.setGem(ItemStack.EMPTY, 0);
							te.setDepth(4, 0);
							te.setGem(ItemStack.EMPTY, 1);
							te.setDepth(4,1);
							te.setWeapon(ItemStack.EMPTY);
						}
					} else {
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
								te.setDepth(4, 0);
								didRemove = true;
							} else if (te.getGem(1).isEmpty()) {
								te.setGem(heldItemClone, 1);
								te.setDepth(4, 1);
								didRemove = true;
							}
						}

						if (didRemove) {
							if (player.getHeldItem(hand).getCount() > 1) {
								player.getHeldItem(hand).shrink(1);
							} else {
								player.inventory.setInventorySlotContents(player.inventory.currentItem,
										ItemStack.EMPTY);
							}
							player.openContainer.detectAndSendChanges();
						}
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
			} else if (player.getHeldItem(hand).getItem().equals(Items.IRON_AXE) && !te.getWeapon().isEmpty()) {
				world.spawnParticle(EnumParticleTypes.CRIT, pos.getX() + 0.35 + world.rand.nextInt(30) / 100d,
						pos.getY() + 1.1, pos.getZ() + 0.2 + world.rand.nextInt(60) / 100d, 0, 0.01, 0);
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
