package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.core.LivingEntityMixin;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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

import java.util.Random;

public class ChannelingRodItem extends Item
{
	private static final String DRAIN_TARGET = "drain_target";
	public static final String SOUL_KEY = "soul_stored";

	public ChannelingRodItem()
	{
		super(new Item.Settings().itemGroup(ArcaneMagic.GROUP));
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
			player.getStackInHand(hand).getOrCreateTag().putInt(DRAIN_TARGET, target.getEntityId());

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
			Entity drainTarget = player.world.getEntityById(stack.getTag().getInt(DRAIN_TARGET));
			if (drainTarget instanceof LivingEntity)
			{
				((LivingEntity) drainTarget).clearPotionEffects();
			}
			stack.getTag().remove(DRAIN_TARGET);
		}

		if (player instanceof  PlayerEntity)
		{
			((PlayerEntity) player).getItemCooldownManager().set(stack.getItem(), 25);
		}
	}

	@Override
	public void method_7852(World world, LivingEntity player, ItemStack stack, int timeLeft) // usingUpdate
	{
		if (player instanceof PlayerEntity)
		{
			if (stack.getTag() != null)
			{
				Entity drainTarget = player.world.getEntityById(stack.getTag().getInt(DRAIN_TARGET));
				if (drainTarget instanceof LivingEntity)
				{
					Random rand = new Random(System.nanoTime());
					if (rand.nextInt(10) == 1)
					{
						((LivingEntityMixin)drainTarget).playHurtSound(DamageSource.MAGIC);
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
			Entity mousedEntity = world.getEntityById(stack.getTag().getInt(DRAIN_TARGET));
			if (mousedEntity != null)
			{
				mousedEntity.kill();
			}
			stack.getTag().remove(DRAIN_TARGET);
			Random rand = new Random(System.nanoTime());
			stack.getTag().putInt(SOUL_KEY,  stack.getTag().getInt(SOUL_KEY) + rand.nextInt(10) + 10);
			if (stack.getTag().getInt(SOUL_KEY) > ArcaneMagicConstants.SOUL_METER_MAX)
			{
				stack.getTag().putInt(SOUL_KEY, ArcaneMagicConstants.SOUL_METER_MAX);
			}
		}

		if (livingEntity instanceof PlayerEntity)
		{
			((PlayerEntity)livingEntity).getItemCooldownManager().set(stack.getItem(), 50);
		}
		return stack;
	}
}
