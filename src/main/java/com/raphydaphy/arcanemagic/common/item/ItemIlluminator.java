package com.raphydaphy.arcanemagic.common.item;

import java.util.ArrayList;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemIlluminator extends ItemBase
{
	public ItemIlluminator()
	{
		super("mystical_illuminator", TextFormatting.GRAY);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
		{
			if (world.isRemote)
			{
				ItemStack asItem = new ItemStack(world.getBlockState(pos).getBlock());

				if (!asItem.isEmpty() && !asItem.getItem().equals(Items.AIR)
						&& ArcaneMagicAPI.getFromAnalysis(asItem, new ArrayList<>()).size() > 0)
				{
					ArcaneMagic.proxy.addIlluminatorParticle(this, world, pos, facing, hitX, hitY, hitZ);
				}
			}
			player.swingArm(hand);
		}
		return EnumActionResult.SUCCESS;
	}
}
