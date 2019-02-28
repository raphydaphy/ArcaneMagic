package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.block.entity.AltarBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.Random;

public class ModEvents
{
	public static void onLivingEntityDeath(Entity entity, DamageSource src)
	{
		if (!entity.world.isClient && entity instanceof LivingEntity && !(entity instanceof PlayerEntity) && ((LivingEntity)entity).getHealthMaximum() < 20)
		{
			BlockPos pos = entity.getPos();
			for (int x = -2; x < 2; x++)
			{
				for (int y = -1; y < 1; y++)
				{
					for (int z = -2; z < 2; z++)
					{
						BlockPos curPos = pos.add(x, y, z);
						BlockEntity blockEntity = entity.world.getBlockEntity(curPos);
						if (blockEntity instanceof AltarBlockEntity)
						{
							ItemStack stack = ((AltarBlockEntity)blockEntity).getInvStack(0);
							if (!stack.isEmpty() && stack.getItem() == ModRegistry.SOUL_PENDANT)
							{
								Random rand = new Random(System.currentTimeMillis());
								stack.getOrCreateTag().putInt(ArcaneMagicConstants.SOUL_KEY, stack.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY) + rand.nextInt(3) + 4);
								if (Objects.requireNonNull(stack.getTag()).getInt(ArcaneMagicConstants.SOUL_KEY) > ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL)
								{
									stack.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL);
								}
								entity.world.playSound(null, curPos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCK, 1, 1);
							}
							return;
						}
					}
				}
			}
		}
	}
}
