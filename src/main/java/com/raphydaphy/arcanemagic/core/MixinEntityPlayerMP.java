package com.raphydaphy.arcanemagic.core;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer
{
    public MixinEntityPlayerMP(World world, GameProfile profile) { super(world, profile); }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyFrom(EntityPlayerMP old, boolean keepEverything, CallbackInfo info)
    {
        System.out.println("dEAD");
    }
}
