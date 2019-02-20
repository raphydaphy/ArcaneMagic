package com.raphydaphy.empowered.item;

import com.raphydaphy.empowered.Empowered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.Random;

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
		if (!(target instanceof PlayerEntity) && target.getHealthMaximum() <= player.getHealthMaximum())
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
		return false;
	}

	@Override
	public void onItemStopUsing(ItemStack stack, World world, LivingEntity player, int int_1)
	{
		if (!world.isClient && stack.getTag() != null)
		{
			Entity drainTarget = player.world.getEntityById(stack.getTag().getInt("drain_target"));
			if (drainTarget instanceof LivingEntity)
			{
				((LivingEntity) drainTarget).clearPotionEffects();
			}
			stack.getTag().remove("drain_target");
		}

		if (player instanceof  PlayerEntity)
		{
			((PlayerEntity) player).getItemCooldownManager().set(stack.getItem(), 25);
		}
	}

	@Override
	public void method_7852(World world, LivingEntity player, ItemStack stack, int timeLeft)
	{
		if (player instanceof PlayerEntity)
		{
			if (stack.getTag() != null)
			{
				Entity drainTarget = player.world.getEntityById(stack.getTag().getInt("drain_target"));
				if (drainTarget instanceof LivingEntity)
				{
					Random rand = new Random(System.nanoTime());
					if (rand.nextInt(10) == 1)
					{
						SoundEvent sound = SoundEvents.ENTITY_PIG_HURT;
						if (drainTarget instanceof CowEntity)
						{
							sound = SoundEvents.ENTITY_COW_HURT;
						}
						else if (drainTarget instanceof RabbitEntity)
						{
							sound = SoundEvents.ENTITY_RABBIT_HURT;
						}

						drainTarget.playSound(sound, rand.nextFloat() *2, 1);
					}
				}
			}
		}
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
			stack.getTag().remove("drain_target");
		}

		if (livingEntity instanceof PlayerEntity)
		{
			((PlayerEntity)livingEntity).getItemCooldownManager().set(stack.getItem(), 50);
		}
		return stack;
	}
}
