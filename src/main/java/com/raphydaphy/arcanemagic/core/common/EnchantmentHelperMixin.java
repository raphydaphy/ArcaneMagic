package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(at = @At("RETURN"), method = "getLooting", cancellable = true)
    private static void getLooting(LivingEntity entity, CallbackInfoReturnable<Integer> info) {
        ItemStack hand = entity.getMainHandStack();
        CompoundTag tag;
        if (hand.getItem() instanceof ICrystalEquipment && (tag = hand.getTag()) != null) {
            ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_PASSIVE_CRYSTAL_KEY));
            if (passive == ArcaneMagicUtils.ForgeCrystal.LAPIS) {
                info.setReturnValue(info.getReturnValue() + 1);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getSweepingMultiplier", cancellable = true)
    private static void getSweepingMultiplier(LivingEntity entity, CallbackInfoReturnable<Float> info) {
        ItemStack hand = entity.getMainHandStack();
        CompoundTag tag;
        if (hand.getItem() instanceof ICrystalEquipment && (tag = hand.getTag()) != null) {
            ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_PASSIVE_CRYSTAL_KEY));
            if (passive == ArcaneMagicUtils.ForgeCrystal.EMERALD) {
                info.setReturnValue(info.getReturnValue() + 0.2f);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getAttackDamage", cancellable = true)
    private static void getAttackDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> info) {
        CompoundTag tag;
        if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null) {
            ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_PASSIVE_CRYSTAL_KEY));
            ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));

            if (passive == ArcaneMagicUtils.ForgeCrystal.REDSTONE) {
                info.setReturnValue(info.getReturnValue() + 1);
            } else if (active == ArcaneMagicUtils.ForgeCrystal.REDSTONE) {
                if (tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY) > 0) {
                    info.setReturnValue(info.getReturnValue() + 15);
                }
            }
        }
    }
}
