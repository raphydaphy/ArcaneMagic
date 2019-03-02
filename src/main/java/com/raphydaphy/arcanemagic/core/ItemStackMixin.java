package com.raphydaphy.arcanemagic.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.DaggerItem;
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

import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin
{
	@Shadow public abstract CompoundTag getTag();

	@Shadow public abstract Item getItem();

	private static final UUID MODIFIER_DAMAGE = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
	private static final UUID MODIFIER_SWING_SPEED = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

	@Inject(at = @At("RETURN"), method="getAttributeModifiers", cancellable=true)
	private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> info)
	{
		if (getItem() instanceof DaggerItem && getTag() != null && getTag().containsKey(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY) && slot == EquipmentSlot.HAND_MAIN)
		{
			DaggerItem dagger = (DaggerItem)getItem();
			Multimap<String, EntityAttributeModifier> map = HashMultimap.create();
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(getTag().getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));
			if (passive == ArcaneMagicUtils.ForgeCrystal.GOLD)
			{

				map.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(MODIFIER_SWING_SPEED, "Weapon modifier", dagger.getSpeed() + 1, EntityAttributeModifier.Operation.ADDITION));
				map.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(MODIFIER_DAMAGE, "Weapon modifier", dagger.getWeaponDamage(), EntityAttributeModifier.Operation.ADDITION));

			} else if (passive == ArcaneMagicUtils.ForgeCrystal.REDSTONE)
			{

				map.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(MODIFIER_SWING_SPEED, "Weapon modifier", dagger.getSpeed(), EntityAttributeModifier.Operation.ADDITION));
				map.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(MODIFIER_DAMAGE, "Weapon modifier", dagger.getWeaponDamage() + 1, EntityAttributeModifier.Operation.ADDITION));

			}

			if (!map.isEmpty())
			{
				info.setReturnValue(map);
			}
		}
	}
}
