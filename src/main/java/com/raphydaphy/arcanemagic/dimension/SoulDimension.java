package com.raphydaphy.arcanemagic.dimension;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

import javax.annotation.Nullable;

public class SoulDimension extends Dimension {
    public SoulDimension(World world, DimensionType type) {
        super(world, type);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        FlatChunkGeneratorConfig config = ChunkGeneratorType.FLAT.createSettings();
        config.setBiome(Biomes.THE_VOID);
        return ChunkGeneratorType.FLAT.create(world, new FixedBiomeSource(new FixedBiomeSourceConfig().setBiome(Biomes.THE_VOID)), config);
    }

    @Nullable
    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean b) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos getTopSpawningBlockPosition(int i, int i1, boolean b) {
        return null;
    }

    @Override
    public float getSkyAngle(long l, float v) {
        return 0.5f;
    }

    @Override
    public boolean hasVisibleSky() {
        return false;
    }

    @Override
    public Vec3d getFogColor(float v, float v1) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    public boolean canPlayersSleep() {
        return false;
    }

    @Override
    public boolean shouldRenderFog(int i, int i1) {
        return false;
    }

    @Override
    public DimensionType getType() {
        return ModRegistry.SOUL_DIMENSION;
    }
}
