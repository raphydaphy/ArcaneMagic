package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalForcer.class)
public class PortalForcerMixin {
    @Shadow
    @Final
    private ServerWorld world;

    @Inject(method = "usePortal", at = @At("HEAD"), cancellable = true)
    private void usePortal(Entity entity, float float_1, CallbackInfoReturnable<Boolean> info) {
        // Going to soul dimension
        if (world.getDimension().getType() == ModRegistry.SOUL_DIMENSION) {
            info.setReturnValue(true);
            info.cancel();
        }

        // Going from the soul dimension
        if (entity.getEntityWorld().getDimension().getType() == ModRegistry.SOUL_DIMENSION) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}
