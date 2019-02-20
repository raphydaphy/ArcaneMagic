package com.raphydaphy.empowered.item;

import com.raphydaphy.empowered.Empowered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ItemChannelingRod extends Item
{
	public ItemChannelingRod()
	{
		super(new Item.Settings().itemGroup(Empowered.GROUP));
	}

	@Override
	public UseAction getUseAction(ItemStack itemStack_1)
	{
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack itemStack_1)
	{
		return 32;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		return new TypedActionResult<>(ActionResult.FAILURE, player.getStackInHand(hand));
	}

	@Override
	public boolean interactWithEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
	{
		if (player.getItemCooldownManager().isCooldown(stack.getItem()))
		{
			return false;
		}

		player.setCurrentHand(hand);
		player.getStackInHand(hand).getOrCreateTag().putInt("drain_target", target.getEntityId());

		if (!player.world.isClient)
		{
			target.addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 32, 128, true, false));
			target.addPotionEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 32, 128, true, false));
		}

		return true;
	}

	@Override
	public ItemStack onItemFinishedUsing(ItemStack stack, World world, LivingEntity livingEntity)
	{
		if (!world.isClient && stack.getTag() != null)
		{
			Entity mousedEntity = world.getEntityById(stack.getTag().getInt("drain_target"));
			if (mousedEntity != null)
			{
				mousedEntity.kill();
			}
		}

		if (livingEntity instanceof PlayerEntity)
		{
			((PlayerEntity)livingEntity).getItemCooldownManager().set(stack.getItem(), 50);
		}
		return stack;
	}
}
