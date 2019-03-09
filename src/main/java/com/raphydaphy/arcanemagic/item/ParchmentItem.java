package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.IParchment;
import com.raphydaphy.arcanemagic.client.screen.ParchmentScreen;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ParchmentItem extends Item
{
	public final ParchmentType type;

	public ParchmentItem(ParchmentType type)
	{
		super(new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(type == ParchmentType.BLANK ? 64 : 1));
		this.type = type;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);
		if (!player.isSneaking())
		{
			IParchment parchment = ParchmentRegistry.getParchment(stack);
			if (parchment != null && (((ParchmentItem) stack.getItem()).type == ParchmentType.ANCIENT) == parchment.isAncient())
			{
				parchment.onOpened(world, player, hand, stack);
				if (world.isClient)
				{
					openGUI(stack, parchment);
				}
				return new TypedActionResult<>(ActionResult.SUCCESS, stack);
			} else
			{
				if (!world.isClient)
				{
					player.setStackInHand(hand, new ItemStack(ModRegistry.PARCHMENT, stack.getAmount()));
				}
				return new TypedActionResult<>(ActionResult.FAIL, stack);
			}
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Environment(EnvType.CLIENT)
	public boolean hasEnchantmentGlint(ItemStack stack)
	{
		return type == ParchmentType.ANCIENT;
	}

	@Environment(EnvType.CLIENT)
	private void openGUI(ItemStack stack, IParchment parchment)
	{
		MinecraftClient.getInstance().openScreen(new ParchmentScreen(stack, parchment));
	}

	public enum ParchmentType
	{
		BLANK, WRITTEN, ANCIENT
	}
}
