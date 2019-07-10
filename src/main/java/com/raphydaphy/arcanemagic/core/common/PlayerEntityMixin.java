package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    @Final
    public PlayerInventory inventory;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"), method = "attack")
    private void attack(Entity entity, CallbackInfo info) {
        ItemStack stack = ((PlayerEntity) (Object) this).getMainHandStack();
        CompoundTag tag;
        if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null) {
            if (tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY) > 0) {
                ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));
                if (active == ArcaneMagicUtils.ForgeCrystal.REDSTONE) {
                    tag.putInt(ArcaneMagicConstants.DAGGER_TIMER_KEY, 0);
                    tag.putBoolean(ArcaneMagicConstants.DAGGER_IS_ACTIVE_KEY, false);
                    ((PlayerEntity) (Object) this).getItemCooldownManager().set(stack.getItem(), ArcaneMagicConstants.DAGGER_ACTIVE_COOLDOWN);
                }

                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    boolean isClient = ((PlayerEntity) (Object) this).world.isClient;
                    if (active == ArcaneMagicUtils.ForgeCrystal.EMERALD) {
                        if (!isClient) {
                            livingEntity.addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 128, 128, true, false));
                            livingEntity.addPotionEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 128, 128, true, false));
                        }
                        tag.putInt(ArcaneMagicConstants.DAGGER_TIMER_KEY, 0);
                        tag.putBoolean(ArcaneMagicConstants.DAGGER_IS_ACTIVE_KEY, false);
                        ((PlayerEntity) (Object) this).getItemCooldownManager().set(stack.getItem(), ArcaneMagicConstants.DAGGER_ACTIVE_COOLDOWN);
                    } else if (active == ArcaneMagicUtils.ForgeCrystal.DIAMOND) {
                        ItemStack held = livingEntity.getMainHandStack().copy();
                        if (held.isEmpty()) {
                            held = livingEntity.getOffHandStack().copy();
                            if (!held.isEmpty()) {
                                livingEntity.setEquippedStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                            }
                        } else {
                            livingEntity.setEquippedStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        }


                        if (!held.isEmpty()) {
                            if (!isClient) {
                                ItemEntity itemEntity = new ItemEntity(livingEntity.world, livingEntity.x, livingEntity.y + 0.5f, livingEntity.z, held);
                                livingEntity.world.spawnEntity(itemEntity);
                            }
                            tag.putInt(ArcaneMagicConstants.DAGGER_TIMER_KEY, 0);
                            tag.putBoolean(ArcaneMagicConstants.DAGGER_IS_ACTIVE_KEY, false);
                            ((PlayerEntity) (Object) this).getItemCooldownManager().set(stack.getItem(), ArcaneMagicConstants.DAGGER_ACTIVE_COOLDOWN);
                        }
                    }
                }

            }
        }

    }
}