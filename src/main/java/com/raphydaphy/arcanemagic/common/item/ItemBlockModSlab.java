/*
 * Slab Code by @Shadows_Of_Fire
 * Reproduced with permission
 */

package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.common.block.BlockModSlab;
import com.raphydaphy.arcanemagic.common.block.BlockModSlab.SlabVariant;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockModSlab extends ItemBlock
{

	private BlockModSlab realBlock = (BlockModSlab) this.block;
	private PropertyEnum<SlabVariant> VARIANT = BlockModSlab.VARIANT;

	public ItemBlockModSlab(BlockModSlab block)
	{
		super(block);
		this.setMaxDamage(0);
		setRegistryName(block.getRegistryName());
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote)
		{
			System.out.println("Calling onItemUse with facing " + facing.getName() + " and hitY " + hitY);
		}
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (!block.isReplaceable(world, pos) && !(block == this.block && checkOppositeByFacing(state, facing)))
		{
			pos = pos.offset(facing);
		}
		state = world.getBlockState(pos);
		IBlockState placeState = getByHitY(hitY);
		if (player.canPlayerEdit(pos, facing, stack) && facing.getAxis().isVertical() && state.getBlock() == this.block)
		{
			placeState = this.realBlock.getDouble();
			if (doubleSlab(world, pos))
			{
				return shrinkAndSucceed(world, pos, player, stack);
			}
		} else if (validEditable(state, world, pos, facing, stack, player) && facing.getAxis().isHorizontal())
		{
			if (world.setBlockState(pos, placeState))
			{
				return shrinkAndSucceed(world, pos, player, stack);
			}
		} else if (validEditable(state, world, pos, facing, stack, player) && facing.getAxis().isVertical())
		{
			placeState = realBlock.getOpposite(placeState);
			if (world.setBlockState(pos, placeState))
			{
				return shrinkAndSucceed(world, pos, player, stack);
			}
		} else if (player.canPlayerEdit(pos, facing, stack) && facing.getAxis().isHorizontal()
				&& state.getBlock() == this.block && canBeMerged(state, placeState))
		{
			if (doubleSlab(world, pos))
			{
				return shrinkAndSucceed(world, pos, player, stack);
			}
		}
		return EnumActionResult.FAIL;
	}

	private boolean validEditable(IBlockState state, World world, BlockPos pos, EnumFacing facing, ItemStack stack,
			EntityPlayer player)
	{
		return state.getBlock().isReplaceable(world, pos) && player.canPlayerEdit(pos, facing, stack);
	}

	private boolean canBeMerged(IBlockState state, IBlockState state2)
	{
		if (state != realBlock.getDouble() && state2 != realBlock.getDouble())
			return !(state == state2);
		else
			return false;
	}

	private boolean checkOppositeByFacing(IBlockState state, EnumFacing facing)
	{
		if (facing == EnumFacing.DOWN)
		{
			return state.getValue(VARIANT) == SlabVariant.UPPER;
		} else if (facing == EnumFacing.UP)
		{
			return state.getValue(VARIANT) == SlabVariant.LOWER;
		} else
		{
			return false;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
	{
		return world.checkNoEntityCollision(Block.FULL_BLOCK_AABB.offset(pos.offset(side)), null);
	}

	private EnumActionResult shrinkAndSucceed(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos,
				player);
		world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
				(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		stack.shrink(1);
		return EnumActionResult.SUCCESS;
	}

	private IBlockState getByHitY(float hitY)
	{
		if (hitY >= 0.5)
		{
			return this.block.getDefaultState().withProperty(VARIANT, SlabVariant.UPPER);
		}
		return this.block.getDefaultState();
	}

	private boolean doubleSlab(World world, BlockPos pos)
	{
		return world.setBlockState(pos, realBlock.getDouble());
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.block.getUnlocalizedName() + "." + stack.getMetadata();
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

}