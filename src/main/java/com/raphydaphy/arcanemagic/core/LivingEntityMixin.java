package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModEvents;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	@Shadow protected int playerHitTimer;

	@Shadow private int lastAttackedTime;

	@Shadow public int hurtTime;

	@Shadow public abstract void setAttacker(LivingEntity livingEntity_1);

	protected LivingEntityMixin(EntityType<? extends LivingEntity> type, World world)
	{
		super(type, world);
	}

	@Inject(at = @At(value = "HEAD"), method = "method_16077", cancellable = true) // dropLoot
	private void method_16077(DamageSource source, boolean killedByPlayer, CallbackInfo info)
	{
		if (source == ModRegistry.DRAINED_DAMAGE || ModEvents.shouldLivingEntityDropLoot(this, source))
		{
			info.cancel();
		}
	}

	@Inject(at = @At(value ="RETURN"), method="damage")
	private void damage(DamageSource source, float float_1, CallbackInfoReturnable<Boolean> info)
	{
		Entity entity = source.getAttacker();
		if (entity instanceof LivingEntity)
		{
			ItemStack stack = ((LivingEntity)entity).getMainHandStack();
			CompoundTag tag;
			if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null)
			{
				ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
				int timer = tag.getInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY);
				if (active == ArcaneMagicUtils.ForgeCrystal.GOLD && timer > 0)
				{
					// TODO: Disable hit cooldown.. how?
					this.playerHitTimer = 0;
				}
			}
		}
	}
}
