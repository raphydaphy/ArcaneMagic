package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.crochet.data.PlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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
        if (world.getDimension().getType() == ModRegistry.VOID_DIM) {
            if (entity instanceof PlayerEntity) {
                PlayerData.get((PlayerEntity) entity, ArcaneMagic.DOMAIN).putLong(ArcaneMagicConstants.ENTERED_VOID_POS_KEY, entity.getBlockPos().asLong());
                PlayerData.markDirty((PlayerEntity)entity);
            }
            entity.setPosition(0, 50, 0);
            info.setReturnValue(true);
            info.cancel();
        }

        // Going from the soul dimension
        else if (entity.getEntityWorld().getDimension().getType() == ModRegistry.VOID_DIM) {
            if (entity instanceof PlayerEntity) {
                CompoundTag data = PlayerData.get((PlayerEntity)entity, ArcaneMagic.DOMAIN);
                BlockPos enteredPos = ((PlayerEntity)entity).getSpawnPosition();
                if (data.containsKey(ArcaneMagicConstants.ENTERED_VOID_POS_KEY)) {
                    enteredPos = BlockPos.fromLong(data.getLong(ArcaneMagicConstants.ENTERED_VOID_POS_KEY));
                }
                entity.setPosition(enteredPos.getX(), enteredPos.getY(), enteredPos.getZ());
            }
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(at= @At("HEAD"), method="createPortal", cancellable = true)
    private void createPortal(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (world.getDimension().getType() == ModRegistry.VOID_DIM) {
            System.out.println("going to soul dim");
        }

        else if (entity.getEntityWorld().getDimension().getType() == ModRegistry.VOID_DIM) {
            System.out.println("going from soul dim");
        }
        System.out.println("CREATION!!!");
    }
}
