package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookOpened;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemNotebook extends ItemBase
{
	public static final int GUI_ID = 1;

	public ItemNotebook(String name)
	{
		super(name, TextFormatting.DARK_PURPLE);
		maxStackSize = 1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		INotebookInfo cap = player.getCapability(INotebookInfo.CAP, null);
		if (cap != null)
		{
			if (world.isRemote)
			{
				world.playSound(player.posX, player.posY, player.posZ, ArcaneMagicSoundHandler.randomPageSound(),
						SoundCategory.MASTER, 1f, 1f, false);
			} else
			{
				ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookOpened(cap), (EntityPlayerMP) player);
			}
			player.openGui(ArcaneMagic.instance, GUI_ID, world, (int) player.posX, (int) player.posY,
					(int) player.posZ);
		} else
		{
			System.out.println("NULL NOTEBOOK CAPABILITY FOUND FOR PLAYER WITH UUID " + player.getUniqueID().toString()
					+ "! THIS IS BAD!");
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
