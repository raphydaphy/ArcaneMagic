package com.raphydaphy.arcanemagic.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.raphydaphy.arcanemagic.api.anima.IAnimaCrystal;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityInfernalSmelter;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
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
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

// TODO: a better name goddamit
public class BlockInfernalSmelter extends BlockBase implements IHasRecipe
{
	protected static final List<AxisAlignedBB> BOUNDS = new ArrayList<>();
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumSmelterHalf> HALF = PropertyEnum.<EnumSmelterHalf>create("piece",
			EnumSmelterHalf.class);
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

		this.setDefaultState(this.getDefaultState().withProperty(HALF, EnumSmelterHalf.BOTTOM));
		;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (state.getValue(HALF).equals(EnumSmelterHalf.BOTTOM))
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

			for (Vec3i piece : pieceLocations)
			{
				BlockPos blockpos1 = pos.add(piece);
				if (!blockpos1.equals(pos))
				{
					if (world.getBlockState(blockpos1).getBlock().equals(this))
					{
						world.setBlockToAir(blockpos1);
					}
				}
			}
		} else
		{
			world.destroyBlock(pos.add(0,-1,0), true);

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
		return state.getValue(HALF) == EnumSmelterHalf.BOTTOM ? true : false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		if (pos.getY() >= worldIn.getHeight() - 1)
		{
			return false;
		} else
		{
			return super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return hasTileEntity(state) ? new TileEntityInfernalSmelter() : null;
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	public BlockPos getRoot(BlockPos pos, IBlockState state)
	{
		return pos.subtract(state.getValue(HALF).getRootPos());
	}
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		BlockPos root = getRoot(pos, state);
		TileEntity teUnchecked = world.getTileEntity(root);
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
								world.playSound(player, root, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM,
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
						world.playSound(player, root, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1,
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
								world.playSound(player, root, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM,
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

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		Vec3d smoke = new Vec3d(pos.getX(), pos.getY() + 1.65, pos.getZ());
		switch (state.getValue(FACING).getHorizontalIndex())
		{
		case 0:
			smoke = smoke.addVector(0.5, 0, 0.6);
			break;
		case 1:
			smoke = smoke.addVector(0.3, 0, 0.5);
			break;
		case 2:
			smoke = smoke.addVector(0.5, 0, 0.3);
			break;
		case 3:
			smoke = smoke.addVector(0.6, 0, 0.5);
			break;
		}
		world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, smoke.x, smoke.y, smoke.z, 0, 0, 0, 0, 0);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(HALF, meta < 4 ? EnumSmelterHalf.BOTTOM : EnumSmelterHalf.TOP)
				.withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(HALF) == EnumSmelterHalf.BOTTOM ? 0 : 4) + state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING, HALF });
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		for (Vec3i piece : pieceLocations)
		{
			worldIn.setBlockState(pos.add(piece), state.withProperty(HALF, EnumSmelterHalf.getFromRootPos(piece)));
		}

	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return state.getValue(HALF).equals(EnumSmelterHalf.BOTTOM) ? Item.getItemFromBlock(this) : Items.AIR;
	}

	private static final Vec3i[] pieceLocations = { new Vec3i(0, 0, 0), new Vec3i(0, 1, 0) };

	public static enum EnumSmelterHalf implements IStringSerializable
	{
		TOP, BOTTOM;

		public String getName()
		{
			return this.equals(TOP) ? "top" : "bottom";
		}

		public int getNum()
		{
			return this.equals(BOTTOM) ? 1 : 2;
		}

		public static EnumSmelterHalf getFromNum(int num)
		{
			return num == 1 ? BOTTOM : TOP;
		}

		public Vec3i getRootPos()
		{
			return pieceLocations[this.getNum() - 1];
		}

		public static EnumSmelterHalf getFromRootPos(Vec3i rootPos)
		{
			for (EnumSmelterHalf piece : EnumSmelterHalf.values())
			{
				if (piece.getRootPos().equals(rootPos))
				{
					return piece;
				}
			}
			return BOTTOM;
		}
	}
}
