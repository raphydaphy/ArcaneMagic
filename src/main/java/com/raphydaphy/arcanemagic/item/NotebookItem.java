package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.screen.NotebookScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NotebookItem extends Item
{
	public NotebookItem()
	{
		super(new Item.Settings().stackSize(1).itemGroup(ArcaneMagic.GROUP));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);
		if (!player.isSneaking())
		{
			if (world.isClient)
			{
				openGUI(stack);
			}
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Environment(EnvType.CLIENT)
	private void openGUI(ItemStack stack)
	{
		MinecraftClient.getInstance().openScreen(new NotebookScreen(stack));
	}
}
