package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Shadow public abstract void setPosition(double double_1, double double_2, double double_3);

    @Shadow public DimensionType dimension;

    @Inject(method = "fromTag", at = @At("RETURN"))
    private void setDimensionIfNull(final CompoundTag compoundTag, final CallbackInfo info)
    {
        if (compoundTag.containsKey("Dimension"))
        {
            DimensionType type = DimensionType.byRawId(compoundTag.getInt("Dimension"));

            if(type == null)
            {
                dimension = DimensionType.OVERWORLD;
                MinecraftServer server = getServer();
                if (server != null) {
                    BlockPos spawnPosition = getServer().getWorld(dimension).getSpawnPos();
                    this.setPosition(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
                } else {
                    ArcaneMagic.getLogger().error("Server was null! This is quite bad..");
                }
            }
        }
    }
}
