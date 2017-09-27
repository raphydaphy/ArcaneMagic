package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemNotebook extends ItemBase
{
	public static final int GUI_ID = 1;

	public ItemNotebook(String name)
	{
		super(name);
		maxStackSize = 1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote)
		{
			world.playSound(player.posX, player.posY, player.posZ, ArcaneMagicSoundHandler.randomPageSound(),
					SoundCategory.MASTER, 1f, 1f, false);
		}
		player.openGui(ArcaneMagic.instance, GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
