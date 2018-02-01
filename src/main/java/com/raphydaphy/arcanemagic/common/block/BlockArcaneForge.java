package com.raphydaphy.arcanemagic.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneForge;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
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

public class BlockArcaneForge extends BlockBase implements IHasRecipe
{
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D);
	public static final PropertyEnum<BlockArcaneForge.EnumForgePiece> PIECE = PropertyEnum.<BlockArcaneForge.EnumForgePiece>create(
			"piece", BlockArcaneForge.EnumForgePiece.class);

	public BlockArcaneForge()
	{
		super("arcane_forge", Material.ROCK, 4f, SoundType.STONE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityArcaneForge te = (TileEntityArcaneForge) world.getTileEntity(pos);

		ItemStack[] stacks = { te.getWeapon(), te.getGem(0), te.getGem(1) };

		for (ItemStack stack : stacks)
		{
			if (!stack.isEmpty())
			{
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
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
		return new TileEntityArcaneForge();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
		{
			TileEntityArcaneForge te = (TileEntityArcaneForge) world.getTileEntity(pos);
			if (!world.isRemote)
			{

				if (!player.getHeldItem(hand).isEmpty())
				{
					if (player.getHeldItem(hand).getItem().equals(Items.IRON_AXE))
					{
						List<Integer> gems = new ArrayList<>();
						if (!te.getGem(0).isEmpty() && te.getDepth(0) > 0)
						{
							gems.add(0);
						}
						if (!te.getGem(1).isEmpty() && te.getDepth(1) > 0)
						{
							gems.add(1);
						}

						for (int gem : gems)
						{
							if (world.rand.nextInt(5) == 1)
							{
								te.setDepth(te.getDepth(gem) - 1, gem);
							}
						}

						if (te.getDepth(0) == 0 && te.getDepth(1) == 0)
						{

							InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 0.5, pos.getZ(),
									te.getWeapon().copy());

							te.setGem(ItemStack.EMPTY, 0);
							te.setDepth(4, 0);
							te.setGem(ItemStack.EMPTY, 1);
							te.setDepth(4, 1);
							te.setWeapon(ItemStack.EMPTY);
						}
					} else
					{
						boolean didRemove = false;
						ItemStack heldItemClone = player.getHeldItem(hand).copy();
						heldItemClone.setCount(1);
						if (player.getHeldItem(hand).getItem().equals(ModRegistry.ARCANE_DAGGER)
								&& te.getWeapon().isEmpty())
						{

							te.setWeapon(heldItemClone);
							didRemove = true;

						} else if (player.getHeldItem(hand).getItem().equals(Items.DIAMOND)
								|| player.getHeldItem(hand).getItem().equals(Items.EMERALD))
						{
							if (te.getGem(0).isEmpty())
							{
								te.setGem(heldItemClone, 0);
								te.setDepth(4, 0);
								didRemove = true;
							} else if (te.getGem(1).isEmpty())
							{
								te.setGem(heldItemClone, 1);
								te.setDepth(4, 1);
								didRemove = true;
							}
						}

						if (didRemove)
						{
							if (player.getHeldItem(hand).getCount() > 1)
							{
								player.getHeldItem(hand).shrink(1);
							} else
							{
								player.inventory.setInventorySlotContents(player.inventory.currentItem,
										ItemStack.EMPTY);
							}
							player.openContainer.detectAndSendChanges();
						}
					}
				} else
				{
					ItemStack stack = ItemStack.EMPTY;
					if (!te.getGem(1).isEmpty())
					{
						stack = te.getGem(1).copy();
						te.setGem(ItemStack.EMPTY, 1);
					} else if (!te.getGem(0).isEmpty())
					{
						stack = te.getGem(0).copy();
						te.setGem(ItemStack.EMPTY, 0);
					} else if (!te.getWeapon().isEmpty())
					{
						stack = te.getWeapon().copy();
						te.setWeapon(ItemStack.EMPTY);
					}

					if (!stack.isEmpty())
					{
						InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), stack);
					}
				}
			} else if (player.getHeldItem(hand).getItem().equals(Items.IRON_AXE) && !te.getWeapon().isEmpty())
			{
				world.spawnParticle(EnumParticleTypes.CRIT, pos.getX() + 0.35 + world.rand.nextInt(30) / 100d,
						pos.getY() + 1.1, pos.getZ() + 0.2 + world.rand.nextInt(60) / 100d, 0, 0.01, 0);
			}
			return true;
		}
		return false;
	}

	@Override
	public void initRecipes(Register<IRecipe> e)
	{
		RecipeHelper.addElementalShaped(this, null, 0, null, Items.IRON_AXE, null, Blocks.OBSIDIAN, Blocks.ANVIL,
				Blocks.OBSIDIAN, Blocks.IRON_BLOCK, Blocks.IRON_BLOCK, Blocks.IRON_BLOCK);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		for (Vec3i piece : pieceLocations)
		{
			worldIn.setBlockState(pos.add(piece),
					state.withProperty(PIECE, BlockArcaneForge.EnumForgePiece.getFromRootPos(piece)));

		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		for (Vec3i piece : pieceLocations)
		{
			if (!worldIn.isAirBlock(pos.add(piece)))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		/*
		if (!(state.getValue(PIECE) == BlockArcaneForge.EnumForgePiece.ONE))
		{
			BlockPos blockpos = pos.add(state.getValue(PIECE).getRootPos());
			IBlockState iblockstate = worldIn.getBlockState(blockpos);

			if (iblockstate.getBlock() != this)
			{
				//worldIn.setBlockToAir(pos);
			} else if (blockIn != this)
			{
				iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
			}
		} else
		{
			boolean flag1 = false;

			for (Vec3i piece : pieceLocations)
			{
				BlockPos blockpos1 = pos.add(piece);
				if (worldIn.getBlockState(blockpos1).getBlock() != this)
				{
					worldIn.setBlockToAir(pos);
					flag1 = true;

					break;
				}
			}

			if (flag1)
			{
				for (Vec3i piece : pieceLocations)
				{
					BlockPos blockpos1 = pos.add(piece);
					if (worldIn.getBlockState(blockpos1).getBlock() == this)
					{
						worldIn.setBlockToAir(blockpos1);
					}
				}
				if (!worldIn.isRemote)
				{
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}
			}
		}*/
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(PIECE, BlockArcaneForge.EnumForgePiece.getFromNum(meta + 1));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PIECE).getNum() - 1;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { PIECE });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return state.getValue(PIECE).equals(BlockArcaneForge.EnumForgePiece.ONE) ? Item.getItemFromBlock(this)
				: Items.AIR;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getValue(PIECE).equals(BlockArcaneForge.EnumForgePiece.ONE))
		{
			for (Vec3i piece : pieceLocations)
			{
				BlockPos blockpos1 = pos.add(piece);
				worldIn.setBlockToAir(blockpos1);
			}
			this.dropBlockAsItem(worldIn, pos, state, 0);
		} else
		{
			BlockPos main = pos.subtract(state.getValue(PIECE).getRootPos());
			for (Vec3i piece : pieceLocations)
			{
				BlockPos blockpos1 = pos.add(piece);
				if (blockpos1.equals(main))
				{
					worldIn.destroyBlock(main, true);
				} else
				{
					worldIn.setBlockToAir(blockpos1);
				}
			}

		}
	}

	private static final Vec3i[] pieceLocations = { new Vec3i(0, 0, 0), new Vec3i(1, 0, 0), new Vec3i(0, 0, 1),
			new Vec3i(1, 0, 1) };

	public static enum EnumForgePiece implements IStringSerializable
	{
		ONE, TWO, THREE, FOUR;

		public String toString()
		{
			return this.getName();
		}

		public String getName()
		{
			switch (this)
			{
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

		public Vec3i getRootPos()
		{
			switch (this)
			{
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

		public int getNum()
		{
			switch (this)
			{
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

		public static EnumForgePiece getFromNum(int num)
		{
			switch (num)
			{
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

		public static EnumForgePiece getFromRootPos(Vec3i rootPos)
		{
			for (EnumForgePiece piece : EnumForgePiece.values())
			{
				if (piece.getRootPos().equals(rootPos))
				{
					return piece;
				}
			}
			return ONE;
		}
	}

}
