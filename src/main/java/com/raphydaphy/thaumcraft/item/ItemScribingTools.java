package com.raphydaphy.thaumcraft.item;

import com.raphydaphy.thaumcraft.block.BlockResearchTable;
import com.raphydaphy.thaumcraft.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemScribingTools extends ItemBase {

	public ItemScribingTools(String name) {
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
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		Block block = world.getBlockState(pos).getBlock(); 
		if (block.equals(ModBlocks.table))
		{
			if (world.getBlockState(pos.add(1, 0, 0)).getBlock().equals(ModBlocks.table))
			{
				world.setBlockState(pos, ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 0).withProperty(BlockResearchTable.FACING, EnumFacing.WEST));
				world.setBlockState(pos.add(1, 0, 0), ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 1).withProperty(BlockResearchTable.FACING, EnumFacing.WEST));
				return EnumActionResult.SUCCESS;
			}
			else if (world.getBlockState(pos.add(-1,0,0)).getBlock().equals(ModBlocks.table))
			{
				world.setBlockState(pos, ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 1).withProperty(BlockResearchTable.FACING, EnumFacing.EAST));
				world.setBlockState(pos.add(-1, 0, 0), ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 0).withProperty(BlockResearchTable.FACING, EnumFacing.EAST));
				return EnumActionResult.SUCCESS;
			}
			else if (world.getBlockState(pos.add(0,0,1)).getBlock().equals(ModBlocks.table))
			{
				world.setBlockState(pos, ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 1).withProperty(BlockResearchTable.FACING, EnumFacing.NORTH));
				world.setBlockState(pos.add(0, 0, 1), ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 0).withProperty(BlockResearchTable.FACING, EnumFacing.NORTH));
				return EnumActionResult.SUCCESS;
			}
			else if (world.getBlockState(pos.add(0,0,-1)).getBlock().equals(ModBlocks.table))
			{
				world.setBlockState(pos, ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 0).withProperty(BlockResearchTable.FACING, EnumFacing.SOUTH));
				world.setBlockState(pos.add(0, 0, -1), ModBlocks.research_table.getDefaultState().withProperty(BlockResearchTable.SIDE, 1).withProperty(BlockResearchTable.FACING, EnumFacing.SOUTH));
				return EnumActionResult.SUCCESS;
			}
		}
		
        return EnumActionResult.PASS;
    }
}
