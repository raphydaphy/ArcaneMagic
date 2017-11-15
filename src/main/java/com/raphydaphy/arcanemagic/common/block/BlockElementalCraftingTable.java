package com.raphydaphy.arcanemagic.common.block;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;
import com.raphydaphy.arcanemagic.common.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.item.ItemEssenceChannelingRod;
import com.raphydaphy.arcanemagic.common.item.ItemScepter;
import com.raphydaphy.arcanemagic.common.network.PacketItemEssenceChanged;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityElementalCraftingTable;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockElementalCraftingTable extends BlockBase implements IHasRecipe
{
	public static final int GUI_ID = 2;
	protected static final List<AxisAlignedBB> BOUNDS = new ArrayList<>();

	static
	{
		BOUNDS.add(makeAABB(2, 0, 2, 14, 4, 14));
		BOUNDS.add(makeAABB(0, 4, 0, 16, 8, 16));
		BOUNDS.add(makeAABB(3, 8, 3, 13, 10, 13));
	}

	public BlockElementalCraftingTable()
	{
		super("elemental_crafting_table", Material.WOOD, 2.5f, SoundType.WOOD);

		this.setLightLevel(1f);
		this.setRenderedAABB(makeAABB(0, 0, 0, 16, 10, 16));
		this.setCollisionAABBList(BOUNDS);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
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
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityElementalCraftingTable te = (TileEntityElementalCraftingTable) world.getTileEntity(pos);
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
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityElementalCraftingTable();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileEntityElementalCraftingTable))
		{
			return false;
		}
		ItemStack stack = player.getHeldItem(hand);
		boolean ret = false;
		if (stack.getItem() instanceof ItemScepter || stack.getItem() instanceof ItemEssenceChannelingRod)
		{
			ret = true;

			IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			NonNullList<ItemStack> recipeInputs = NonNullList.withSize(9, ItemStack.EMPTY);

			for (int i = 0; i < 9; i++)
				recipeInputs.set(i, cap.getStackInSlot(i));

			IElementalRecipe foundRecipe = ArcaneMagicAPI.getElementalCraftingRecipe(player, stack, recipeInputs,
					world);
			if (foundRecipe != null)
			{
				if (!world.isRemote)
				{
					foundRecipe.craft(stack, recipeInputs);
					te.markDirty();
					EntityItemFancy craftResult = new EntityItemFancy(world, pos.getX() + 0.5,
							pos.getY() + 9d * (1d / 16d), pos.getZ() + 0.5, foundRecipe.getRecipeOutput());
					craftResult.motionX = 0;
					craftResult.motionY = 0;
					craftResult.motionZ = 0;
					world.spawnEntity(craftResult);

					ArcaneMagicPacketHandler.INSTANCE.sendTo(
							new PacketItemEssenceChanged(stack.getCapability(IEssenceStorage.CAP, null),
									ItemScepter.stupidGetSlot(player.inventory, stack), stack),
							(EntityPlayerMP) player);
					return ret;
				} else
				{
					world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, pos.getX() + 0.5, pos.getY() + (12d * (1d / 16d)),
							pos.getZ() + 0.5, 0f, 0.1f, 0f);
					world.playSound(player, pos, ArcaneMagicSoundHandler.elemental_crafting_success,
							SoundCategory.BLOCKS, 1, 1);
					return ret;
				}
			}
			return ret;
		}

		else if (hitX >= 0.203 && hitX <= 0.801 && hitY >= 0.5625 && hitZ >= 0.203 && hitZ <= 0.801)
		{
			ret = true;

			float divX = (hitX - 0.203f);
			float divZ = (hitZ - 0.203f);
			int slotX = 2;
			int slotZ = 2;

			if (divX <= 0.2152)
			{
				slotX = 0;
			} else if (divX <= 0.4084)
			{
				slotX = 1;
			}

			if (divZ <= 0.2152)
			{
				slotZ = 0;
			} else if (divZ <= 0.4084)
			{
				slotZ = 1;
			}

			int slot = slotX + (slotZ * 3);
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

			} else
			{
				ItemStack toExtract = cap.getStackInSlot(slot);
				if (toExtract != null && !toExtract.isEmpty())
				{
					ret = true;
					if (!world.isRemote)
					{
						if (player.addItemStackToInventory(toExtract.copy()))
						{
							cap.getStackInSlot(slot).setCount(0);
							te.markDirty();
						}
					} else
					{
						world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1,
								1);
					}
				}
			}
		}
		if (player.isSneaking() && !ret)
		{
			return false;
		}
		return true;
	}

	@Override
	public void initRecipes(Register<IRecipe> e)
	{
		RecipeHelper.addShaped(this, 3, 3, null, "dustGlowstone", null, "plankWood", "workbench", "plankWood",
				"plankWood", null, "plankWood");
	}
}
