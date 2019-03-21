package com.raphydaphy.arcanemagic.core.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.DaggerItem;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin
{
	@Shadow
	public abstract CompoundTag getTag();

	@Shadow
	public abstract Item getItem();

	@Inject(at = @At("RETURN"), method = "getDurability", cancellable = true)
	private void getDurability(CallbackInfoReturnable<Integer> info)
	{
		CompoundTag tag;
		if (getItem() instanceof ICrystalEquipment && (tag = getTag()) != null)
		{
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_PASSIVE_CRYSTAL_KEY));
			if (passive == ArcaneMagicUtils.ForgeCrystal.COAL)
			{
				info.setReturnValue((int)(info.getReturnValue() * 1.2f));
			}
		}
	}

	@Inject(at = @At("HEAD"), method="applyDamage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", cancellable = true)
	private void applyDamage(int amount, Random rand, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> info)
	{
		CompoundTag tag = getTag();
		if (tag != null && tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY) > 0)
		{
			ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));
			if (active == ArcaneMagicUtils.ForgeCrystal.COAL)
			{
				info.setReturnValue(false);
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "getAttributeModifiers", cancellable = true)
	private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> info)
	{
		CompoundTag tag;
		if (getItem() instanceof DaggerItem && (tag = getTag()) != null && slot == EquipmentSlot.HAND_MAIN)
		{
			DaggerItem dagger = (DaggerItem) getItem();
			Multimap<String, EntityAttributeModifier> map = HashMultimap.create();
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_PASSIVE_CRYSTAL_KEY));
			ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));

			double speed = dagger.getSpeed();
			float damage = dagger.getWeaponDamage();

			if (passive == ArcaneMagicUtils.ForgeCrystal.GOLD)
			{
				speed += 0.5;
			} else if (active == ArcaneMagicUtils.ForgeCrystal.GOLD && tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY) > 0)
			{
				speed += 128;
			}

			map.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(DaggerItem.getSpeedModifier(), "Weapon modifier", speed, EntityAttributeModifier.Operation.ADDITION));
			map.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(DaggerItem.getDamageModifier(), "Weapon modifier", damage, EntityAttributeModifier.Operation.ADDITION));

			info.setReturnValue(map);
		}
	}

	@Inject(at = @At("RETURN"), method="areEqual", cancellable=true)
	private static void areEqual(ItemStack one, ItemStack two, CallbackInfoReturnable<Boolean> info)
	{
		CompoundTag tagOne;
		CompoundTag tagTwo;
		if (one.getDamage() == two.getDamage())
		{
			if (!info.getReturnValue() && one.getItem() instanceof ICrystalEquipment && two.getItem() instanceof ICrystalEquipment && (tagOne = one.getTag()) != null && (tagTwo = two.getTag()) != null)
			{
				UUID uuidOne = tagOne.getUuid(ArcaneMagicConstants.UUID_KEY);
				if (uuidOne != null && uuidOne.equals(tagTwo.getUuid(ArcaneMagicConstants.UUID_KEY)))
				{
					int timerOne = tagOne.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY);
					int timerTwo = tagTwo.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY);
					if ((timerOne != 0 && timerTwo != 0) || (timerTwo == 0 && timerOne == 0))
					{
						info.setReturnValue(true);
					}
				}
			}
		}
	}
}
