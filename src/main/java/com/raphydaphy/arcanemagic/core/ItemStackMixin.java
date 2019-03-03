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
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));
			if (passive == ArcaneMagicUtils.ForgeCrystal.COAL)
			{
				info.setReturnValue((int)(info.getReturnValue() * 1.2f));
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "getAttributeModifiers", cancellable = true)
	private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> info)
	{
		CompoundTag tag;
		if (getItem() instanceof DaggerItem && (tag = getTag()) != null && tag.containsKey(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY) && slot == EquipmentSlot.HAND_MAIN)
		{
			DaggerItem dagger = (DaggerItem) getItem();
			Multimap<String, EntityAttributeModifier> map = HashMultimap.create();
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));

			double speed = dagger.getSpeed();
			float damage = dagger.getWeaponDamage();

			if (passive == ArcaneMagicUtils.ForgeCrystal.GOLD)
			{
				speed += 0.5;
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
		if (!info.getReturnValue() && one.getItem() instanceof ICrystalEquipment && two.getItem() instanceof ICrystalEquipment && (tagOne = one.getTag()) != null && (tagTwo = two.getTag()) != null)
		{
			UUID uuidOne = tagOne.getUuid(ArcaneMagicConstants.CRYSTAL_ITEM_UUID);
			if (uuidOne != null && uuidOne.equals(tagTwo.getUuid(ArcaneMagicConstants.CRYSTAL_ITEM_UUID)))
			{
				info.setReturnValue(true);
			}
		}
	}
}
