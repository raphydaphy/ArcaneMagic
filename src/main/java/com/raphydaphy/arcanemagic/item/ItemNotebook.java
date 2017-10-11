package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.network.PacketNotebookOpened;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		NotebookInfo cap = player.getCapability(NotebookInfo.CAP, null);
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

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack)
	{
		return TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName(stack) + ".name").trim();
	}
}
