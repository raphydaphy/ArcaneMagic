package com.raphydaphy.arcanemagic.structure;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.init.Biomes;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WizardHutStructure extends ScatteredStructure<WizardHutConfig>
{
    public WizardHutStructure()
    {

    }

    @Override
    protected StructureStart func_202369_a(IWorld world, IChunkGenerator<?> generator, SharedSeedRandom rand, int chunkX, int chunkZ)
    {
        System.out.println("Hello world! I AM A WIZRAD HUT NOTICE ME");
        Biome biome = generator.getBiomeProvider().getBiome(new BlockPos((chunkX << 4) + 9, 0, (chunkZ << 4) + 9), Biomes.PLAINS);
        return new WizardHutStructure.Start(world, generator, rand, chunkX, chunkZ, biome);
    }

    @Override
    protected String getStructureName()
    {
        return ArcaneMagicResources.WIZARD_HUT_NAME;
    }

    @Override
    public int func_202367_b()
    {
        return 100;
    }

    @Override
    protected int func_202382_c()
    {
        return 84739471;
    }

    public static class Start extends StructureStart
    {
        public Start(IWorld world, IChunkGenerator<?> generator, SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome)
        {
            super(chunkX, chunkZ, biome, rand, world.getSeed());
            TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
            BlockPos pos = new BlockPos(chunkX * 16, 90, chunkZ * 16);
            WizardHutPieces.addPieces(this.components, manager, pos);
            this.func_202500_a(world);
        }
    }
}
