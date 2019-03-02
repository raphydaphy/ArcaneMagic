package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin
{
	@Inject(at = @At("RETURN"), method = "getLooting", cancellable = true)
	private static void getLooting(LivingEntity entity, CallbackInfoReturnable<Integer> info)
	{
		ItemStack hand = entity.getStackInHand(entity.preferredHand);
		if (hand.getItem() instanceof ICrystalEquipment && hand.getTag() != null)
		{
			ArcaneMagicUtils.ForgeCrystal passive = ArcaneMagicUtils.ForgeCrystal.getFromID(hand.getTag().getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));
			if (passive == ArcaneMagicUtils.ForgeCrystal.LAPIS)
			{
				info.setReturnValue(info.getReturnValue() + 1);
			}
		}
	}
}
