package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.block.BlockWritingDesk;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemScribingTools extends ItemBase
{

	public ItemScribingTools(String name)
	{
		super(name);
		maxStackSize = 1;
		setMaxDamage(100);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return super.showDurabilityBar(stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		Block block = world.getBlockState(pos).getBlock();
		if (block.equals(ModRegistry.TABLE))
		{
			if (world.getBlockState(pos.add(1, 0, 0)).getBlock().equals(ModRegistry.TABLE))
			{
				world.setBlockState(pos, ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 0).withProperty(BlockHorizontal.FACING, EnumFacing.WEST));
				world.setBlockState(pos.add(1, 0, 0), ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 1).withProperty(BlockHorizontal.FACING, EnumFacing.WEST));
				return EnumActionResult.SUCCESS;
			} else if (world.getBlockState(pos.add(-1, 0, 0)).getBlock().equals(ModRegistry.TABLE))
			{
				world.setBlockState(pos, ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 1).withProperty(BlockHorizontal.FACING, EnumFacing.EAST));
				world.setBlockState(pos.add(-1, 0, 0), ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 0).withProperty(BlockHorizontal.FACING, EnumFacing.EAST));
				return EnumActionResult.SUCCESS;
			} else if (world.getBlockState(pos.add(0, 0, 1)).getBlock().equals(ModRegistry.TABLE))
			{
				world.setBlockState(pos, ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 1).withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
				world.setBlockState(pos.add(0, 0, 1), ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 0).withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
				return EnumActionResult.SUCCESS;
			} else if (world.getBlockState(pos.add(0, 0, -1)).getBlock().equals(ModRegistry.TABLE))
			{
				world.setBlockState(pos, ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 0).withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH));
				world.setBlockState(pos.add(0, 0, -1), ModRegistry.RESEARCH_TABLE.getDefaultState()
						.withProperty(BlockWritingDesk.SIDE, 1).withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH));
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}
}
