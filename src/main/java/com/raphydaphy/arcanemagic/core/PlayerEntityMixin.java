package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world)
	{
		super(type, world);
	}

	@Inject(at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_7350()V"), method = "attack")
	private void attack(Entity entity, CallbackInfo info)
	{
		ItemStack stack = getMainHandStack();
		CompoundTag tag;
		if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null)
		{
			if (tag.getInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY) > 0)
			{
				System.out.println("its got a timer");
				ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
				if (active == ArcaneMagicUtils.ForgeCrystal.REDSTONE)
				{
					if (!world.isClient)
					{
						tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, 0);
					} else
					{
						world.playSound(null, getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYER, 1, 1);
					}
				}
			}
		}
	}
}
