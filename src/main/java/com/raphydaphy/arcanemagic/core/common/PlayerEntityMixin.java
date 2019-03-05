package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DataHolder
{
	@Shadow public abstract ItemCooldownManager getItemCooldownManager();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world)
	{
		super(type, world);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_7350()V"), method = "attack")
	private void attack(Entity entity, CallbackInfo info)
	{
		ItemStack stack = getMainHandStack();
		CompoundTag tag;
		if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null)
		{
			if (tag.getInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY) > 0)
			{
				ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
				if (active == ArcaneMagicUtils.ForgeCrystal.REDSTONE)
				{
					tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, 0);
				} else if (active == ArcaneMagicUtils.ForgeCrystal.EMERALD)
				{
					if (entity instanceof LivingEntity)
					{
						if (!world.isClient)
						{
							((LivingEntity) entity).addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 128, 128, true, false));
							((LivingEntity) entity).addPotionEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 128, 128, true, false));
						}
						tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, 0);
					}
				}
			}
		}
	}

	// TODO: Disable shield with active diamond bonus here
	@Inject(at=@At("RETURN"), method="method_6090")
	private void method_6090(LivingEntity other, CallbackInfo info) // onAttackedBy
	{
		ItemStack stack = other.getMainHandStack();
		CompoundTag tag;
		if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null)
		{
			ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
			if (tag.getInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY) > 0 && active == ArcaneMagicUtils.ForgeCrystal.DIAMOND)
			{
				if (!world.isClient)
				{
					tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, 0);
					other.setEquippedStack(EquipmentSlot.HAND_MAIN, stack);
				}
				this.getItemCooldownManager().set(Items.SHIELD, 100);
				this.method_6021();
				this.world.summonParticle(this, (byte) 30);
			}
		}
	}

	private static final String ADDITIONAL_DATA_TAG = "ArcaneMagicData";
	private CompoundTag additionalData = new CompoundTag();

	@Inject(at=@At("TAIL"), method="readCustomDataFromTag")
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo info)
	{
		additionalData = tag.getCompound(ADDITIONAL_DATA_TAG);
	}

	@Inject(at=@At("TAIL"), method="writeCustomDataToTag")
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