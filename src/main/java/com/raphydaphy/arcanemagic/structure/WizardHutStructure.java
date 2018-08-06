package com.raphydaphy.arcanemagic.structure;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.init.Biomes;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class WizardHutStructure extends ScatteredStructure<WizardHutConfig>
{
	public WizardHutStructure()
	{

	}

	@Override
	protected boolean func_202372_a(IChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ)
	{
		// Copied from VillageStructure
		ChunkPos chunkPos = this.func_211744_a(generator, rand, chunkX, chunkZ, 0, 0);
		if (chunkX == chunkPos.x && chunkZ == chunkPos.z)
		{
			Biome biome = generator.getBiomeProvider().getBiome(new BlockPos((chunkX << 4) + 9, 0, (chunkZ << 4) + 9), Biomes.DEFAULT);
			return generator.hasStructure(biome, Feature.VILLAGE);
		} else
		{
			return false;
		}
	}

	@Override

	protected StructureStart func_202369_a(IWorld world, IChunkGenerator<?> generator, SharedSeedRandom rand, int chunkX, int chunkZ)
	{
		Biome biome = generator.getBiomeProvider().getBiome(new BlockPos((chunkX << 4) + 9, 0, (chunkZ << 4) + 9), Biomes.PLAINS);
		return new WizardHutStructure.Start(world, generator, rand, chunkX, chunkZ, biome);
	}

	@Override
	protected int func_202382_c()
	{
		// Seed modifier
		return 83743912;
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

	public static class Start extends StructureStart
	{
		public Start()
		{

		}

		public Start(IWorld world, IChunkGenerator<?> generator, SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome)
		{
			super(chunkX, chunkZ, biome, rand, world.getSeed());
			TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
			BlockPos pos = new BlockPos(chunkX * 16, 90, chunkZ * 16);
			WizardHutPieces.addPieces(this.components, world, manager, pos);
			this.func_202500_a(world);
		}
	}
}