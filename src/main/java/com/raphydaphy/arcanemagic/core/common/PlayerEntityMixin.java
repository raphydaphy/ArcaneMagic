package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements DataHolder
{
	@Shadow
	public abstract ItemCooldownManager getItemCooldownManager();

	@Shadow
	@Final
	public PlayerInventory inventory;

	@Shadow
	public abstract void addChatMessage(TextComponent textComponent_1, boolean boolean_1);

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_7350()V"), method = "attack")
	private void attack(Entity entity, CallbackInfo info)
	{
		ItemStack stack = ((PlayerEntity) (Object) this).getMainHandStack();
		CompoundTag tag;
		if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null)
		{
			if (tag.getInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY) > 0)
			{
				ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
				if (active == ArcaneMagicUtils.ForgeCrystal.REDSTONE)
				{
					tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, 0);
				}

				if (entity instanceof LivingEntity)
				{
					LivingEntity livingEntity = (LivingEntity) entity;
					boolean isClient = ((PlayerEntity) (Object) this).world.isClient;
					if (active == ArcaneMagicUtils.ForgeCrystal.EMERALD)
					{
						if (!isClient)
						{
							livingEntity.addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 128, 128, true, false));
							livingEntity.addPotionEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 128, 128, true, false));
						}
						tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, 0);
					} else if (active == ArcaneMagicUtils.ForgeCrystal.DIAMOND)
					{
						ItemStack held = livingEntity.getMainHandStack().copy();
						if (held.isEmpty())
						{
							held = livingEntity.getOffHandStack().copy();
							if (!held.isEmpty())
							{
								livingEntity.setEquippedStack(EquipmentSlot.HAND_OFF, ItemStack.EMPTY);
							}
						} else
						{
							livingEntity.setEquippedStack(EquipmentSlot.HAND_MAIN, ItemStack.EMPTY);
						}


						if (!held.isEmpty())
						{
							if (!isClient)
							{
								ItemEntity itemEntity = new ItemEntity(livingEntity.world, livingEntity.x, livingEntity.y + 0.5f, livingEntity.z, held);
								livingEntity.world.spawnEntity(itemEntity);
							}
							tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, 0);
						}
					}
				}

			}
		}

	}

	private static final String ADDITIONAL_DATA_TAG = "ArcaneMagicData";
	private CompoundTag additionalData = new CompoundTag();

	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag")
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo info)
	{
		additionalData = tag.getCompound(ADDITIONAL_DATA_TAG);
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag")
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo info)
	{
		tag.put(ADDITIONAL_DATA_TAG, additionalData);
	}

	@Override
	public CompoundTag getAdditionalData()
	{
		return additionalData;
	}

	@Override
	public void setAdditionalData(CompoundTag tag)
	{
		this.additionalData = tag;
	}
}