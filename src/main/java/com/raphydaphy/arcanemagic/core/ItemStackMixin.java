package com.raphydaphy.arcanemagic.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.CrystalToolItem;
import com.raphydaphy.arcanemagic.item.DaggerItem;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
		if (getItem() instanceof ICrystalEquipment && getTag() != null)
		{
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(getTag().getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));
			if (passive == ArcaneMagicUtils.ForgeCrystal.COAL)
			{
				info.setReturnValue((int)(info.getReturnValue() * 1.2f));
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "getAttributeModifiers", cancellable = true)
	private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> info)
	{
		if (getItem() instanceof DaggerItem && getTag() != null && getTag().containsKey(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY) && slot == EquipmentSlot.HAND_MAIN)
		{
			DaggerItem dagger = (DaggerItem) getItem();
			Multimap<String, EntityAttributeModifier> map = HashMultimap.create();
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(getTag().getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));

			if (passive == ArcaneMagicUtils.ForgeCrystal.GOLD)
			{
				map.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(DaggerItem.getSpeedModifier(), "Weapon modifier", (double) dagger.getSpeed() + 0.5d, EntityAttributeModifier.Operation.ADDITION));

			} else
			{
				map.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(DaggerItem.getSpeedModifier(), "Weapon modifier", dagger.getSpeed(), EntityAttributeModifier.Operation.ADDITION));
			}
			if (passive == ArcaneMagicUtils.ForgeCrystal.REDSTONE)
			{
				map.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(DaggerItem.getDamageModifier(), "Weapon modifier", (double) dagger.getWeaponDamage() + 1, EntityAttributeModifier.Operation.ADDITION));
			} else
			{
				map.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(DaggerItem.getDamageModifier(), "Weapon modifier", dagger.getWeaponDamage(), EntityAttributeModifier.Operation.ADDITION));
			}

			if (!map.isEmpty())
			{
				info.setReturnValue(map);
			}
		}
	}
}
