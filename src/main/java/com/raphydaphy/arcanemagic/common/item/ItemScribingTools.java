package com.raphydaphy.arcanemagic.common.item;

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
	public boolean showDurabilityBar(ItemStack stack) {
		return super.showDurabilityBar(stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {

		return EnumActionResult.PASS;
	}
}
