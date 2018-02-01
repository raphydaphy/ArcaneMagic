package com.raphydaphy.arcanemagic.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneForge;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockArcaneForge extends BlockBase implements IHasRecipe {
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	public static final PropertyEnum<EnumForgePiece> PIECE = PropertyEnum.<EnumForgePiece>create("piece",
			EnumForgePiece.class);

	public BlockArcaneForge() {
		super("arcane_forge", Material.ROCK, 4f, SoundType.STONE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public void init()
	{
		ModRegistry.BLOCKS.add(this);
		ModRegistry.ITEMS.add(new ItemBlockArcaneForge());
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (state.getValue(PIECE).equals(EnumForgePiece.ONE)) {
			TileEntityArcaneForge te = (TileEntityArcaneForge) world.getTileEntity(pos);

			ItemStack[] stacks = { te.getWeapon(), te.getGem(0), te.getGem(1) };

			for (ItemStack stack : stacks) {
				if (!stack.isEmpty()) {
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				}
			}

			for (Vec3i piece : pieceLocations) {
				BlockPos blockpos1 = pos.add(piece);
				if (!blockpos1.equals(pos)) {
					if (world.getBlockState(blockpos1).getBlock().equals(this)) {
						world.setBlockToAir(blockpos1);
					}
				}
			}
		} else {
			world.destroyBlock(getRoot(state, pos), true);

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
		return state.getValue(PIECE).equals(EnumForgePiece.ONE) ? true : false;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if (hasTileEntity(state)) {
			return new TileEntityArcaneForge();
		}
		return null;
	}

	public BlockPos getRoot(IBlockState state, BlockPos pos) {
		return pos.subtract(state.getValue(PIECE).getRootPos());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		BlockPos root = getRoot(state, pos);
		if (!player.isSneaking()) {
			TileEntityArcaneForge te = (TileEntityArcaneForge) world.getTileEntity(root);
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

							InventoryHelper.spawnItemStack(world, root.getX(), root.getY() + 0.5, root.getZ(),
									te.getWeapon().copy());

							te.setGem(ItemStack.EMPTY, 0);
							te.setDepth(4, 0);
							te.setGem(ItemStack.EMPTY, 1);
							te.setDepth(4, 1);
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
								|| player.getHeldItem(hand).getItem().equals(Items.EMERALD)
								|| player.getHeldItem(hand).getItem().equals(ModRegistry.ANIMA)
								|| player.getHeldItem(hand).getItem().equals(ModRegistry.CREATION)) {
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
						InventoryHelper.spawnItemStack(world, root.getX(), root.getY() + 0.5, root.getZ(), stack);
					}
				}
			} else if (player.getHeldItem(hand).getItem().equals(Items.IRON_AXE) && !te.getWeapon().isEmpty()) {
				for (int i = 0; i < world.rand.nextInt(5); i++) {
					world.spawnParticle(EnumParticleTypes.CRIT, root.getX() + 0.85 + world.rand.nextInt(30) / 100d,
							root.getY() + 1.1, root.getZ() + 0.3 + world.rand.nextInt(120) / 100d, 0, 0.01, 0);
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

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		BlockPos root = getPlacementOffset(pos, placer.getHorizontalFacing());
		
		for (Vec3i piece : pieceLocations) {
			worldIn.setBlockState(root.add(piece), state.withProperty(PIECE, EnumForgePiece.getFromRootPos(piece)));

		}
		
	}
	
	public static BlockPos getPlacementOffset(BlockPos pos, EnumFacing facing)
	{
		switch(facing)
		{
		case DOWN:
			break;
		case EAST:
			return pos;
		case NORTH:
			return pos.add(0,0,-1);
		case SOUTH:
			return pos.add(-1,0,0);
		case UP:
			break;
		case WEST:
			return pos.add(-1,0,-1);
		}
		return pos;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos placementPos, EnumFacing facing) {
		
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PIECE, EnumForgePiece.getFromNum(meta + 1));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PIECE).getNum() - 1;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PIECE });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PIECE).equals(EnumForgePiece.ONE) ? Item.getItemFromBlock(this) : Items.AIR;
	}

	public static final Vec3i[] pieceLocations = { new Vec3i(0, 0, 0), new Vec3i(1, 0, 0), new Vec3i(0, 0, 1),
			new Vec3i(1, 0, 1) };

	public class ItemBlockArcaneForge extends ItemBlock
	{
		public ItemBlockArcaneForge() {
			super(ModRegistry.ARCANE_FORGE);
			setRegistryName(ModRegistry.ARCANE_FORGE.getRegistryName());
			setCreativeTab(ArcaneMagic.creativeTab);
		}
		
		@SideOnly(Side.CLIENT)
	    public boolean canPlaceBlockOnSide(World world, BlockPos placementPos, EnumFacing side, EntityPlayer player, ItemStack stack)
	    {
			BlockPos root = BlockArcaneForge.getPlacementOffset(placementPos, player.getHorizontalFacing());
			for (Vec3i piece : BlockArcaneForge.pieceLocations) {
				BlockPos pos = root.add(piece);
				if (world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(),
						pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), new Predicate<Entity>() {
							public boolean apply(@Nullable Entity entity) {
								return !(entity instanceof EntityItem);
							}
						}).size() > 0) {
					return false;
				}
				if (!world.isAirBlock(pos))
				{
					return false;
				}
			}
			return true;
	    }
	}
	
	public static enum EnumForgePiece implements IStringSerializable {
		ONE, TWO, THREE, FOUR;

		public String toString() {
			return this.getName();
		}

		public String getName() {
			switch (this) {
			case ONE:
				return "one";
			case TWO:
				return "two";
			case THREE:
				return "three";
			case FOUR:
				return "four";
			}
			return "one";
		}

		public Vec3i getRootPos() {
			switch (this) {
			case ONE:
				return pieceLocations[0];
			case TWO:
				return pieceLocations[1];
			case THREE:
				return pieceLocations[2];
			case FOUR:
				return pieceLocations[3];
			}

			return pieceLocations[0];
		}

		public int getNum() {
			switch (this) {
			case ONE:
				return 1;
			case TWO:
				return 2;
			case THREE:
				return 3;
			case FOUR:
				return 4;
			}
			return 1;
		}

		public static EnumForgePiece getFromNum(int num) {
			switch (num) {
			case 1:
				return ONE;
			case 2:
				return TWO;
			case 3:
				return THREE;
			case 4:
				return FOUR;
			}
			return ONE;
		}

		public static EnumForgePiece getFromRootPos(Vec3i rootPos) {
			for (EnumForgePiece piece : EnumForgePiece.values()) {
				if (piece.getRootPos().equals(rootPos)) {
					return piece;
				}
			}
			return ONE;
		}
	}

}
