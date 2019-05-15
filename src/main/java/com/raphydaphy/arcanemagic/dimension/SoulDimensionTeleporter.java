package com.raphydaphy.arcanemagic.dimension;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PortalForcer;

/**
 * Kindly written by Kino
 */
public class SoulDimensionTeleporter extends PortalForcer {
    private final ServerWorld world;

    public SoulDimensionTeleporter(ServerWorld world) {
        super(world);

        this.world = world;
    }

    @Override
    public boolean usePortal(Entity entity, float yaw) {
        entity.setVelocity(Vec3d.ZERO);
        entity.yaw = yaw;

        if (entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).networkHandler.requestTeleport(entity.x, entity.y, entity.z, entity.yaw, entity.pitch);
            ((ServerPlayerEntity) entity).networkHandler.syncWithPlayerPosition();
        } else {
            entity.setPositionAndAngles(entity.x, entity.y, entity.z, entity.yaw, entity.pitch);
        }

        return true;
    }

    @Override
    public boolean createPortal(Entity entity) {
        for (int x = -1; x < 1; ++x) {
            for (int z = -1; z < 1; ++z) {
                this.world.setBlockState(new BlockPos(x, entity.y, z), Blocks.SMOOTH_QUARTZ.getDefaultState(), 3);
            }
        }

        return true;
    }

}