package com.raphydaphy.arcanemagic.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase
{
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource src, CallbackInfo ci)
    {
        System.out.println("death in the family");
    }
}
