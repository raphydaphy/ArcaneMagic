package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.block.entity.*;
import com.raphydaphy.arcanemagic.client.model.ArcaneModelLoader;
import com.raphydaphy.arcanemagic.client.model.IronDaggerModel;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.render.*;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModCutscenes;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.network.TremorPacket;
import com.raphydaphy.arcanemagic.util.TremorTracker;
import com.raphydaphy.cutsceneapi.fakeworld.CutsceneChunk;
import com.raphydaphy.cutsceneapi.fakeworld.CutsceneWorld;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.GameMode;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig;
import net.minecraft.world.chunk.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;

import java.util.Collections;
import java.util.EnumSet;

public class ArcaneMagicClient implements ClientModInitializer
{
	public static CutsceneWorld MUSHROOM_ISLAND_WORLD;

	public ArcaneMagicClient()
	{
		ModCutscenes.preInitClient();
	}

	@Override
	public void onInitializeClient()
	{
		BlockEntityRendererRegistry.INSTANCE.register(AltarBlockEntity.class, new AltarRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(AnalyzerBlockEntity.class, new AnalyzerRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(CrystalInfuserBlockEntity.class, new CrystalInfuserRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(MixerBlockEntity.class, new MixerRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(PipeBlockEntity.class, new PipeRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(SmelterBlockEntity.class, new SmelterRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(TransfigurationTableBlockEntity.class, new TransfigurationTableRenderer());
		BlockEntityRendererRegistry.INSTANCE.register(PumpBlockEntity.class, new PumpRenderer());

		ClientSidePacketRegistry.INSTANCE.register(ClientBlockEntityUpdatePacket.ID, new ClientBlockEntityUpdatePacket.Handler());
		ClientSidePacketRegistry.INSTANCE.register(ProgressionUpdateToastPacket.ID, new ProgressionUpdateToastPacket.Handler());
		ClientSidePacketRegistry.INSTANCE.register(TremorPacket.ID, new TremorPacket.Handler());

		ClientSpriteRegistryCallback.registerBlockAtlas((atlaxTexture, registry) ->
		                                                {
			                                                registry.register(ArcaneMagicConstants.GLOW_PARTICLE_TEXTURE);
			                                                registry.register(ArcaneMagicConstants.SMOKE_PARTICLE_TEXTURE);
		                                                });

		ClientTickCallback.EVENT.register((client) ->
		                                  {
			                                  if (!MinecraftClient.getInstance().isPaused())
			                                  {
				                                  ParticleRenderer.INSTANCE.update();
				                                  TremorTracker.updateClient();
				                                  HudRenderer.update();
			                                  }
		                                  });

		ArcaneModelLoader.registerModel(new ModelIdentifier(ModRegistry.IRON_DAGGER_IDENTIFIER, "inventory"), IronDaggerModel::new);

		ModCutscenes.initClient();

		LevelInfo levelInfo = new LevelInfo(1234651246, GameMode.SPECTATOR, false, false, LevelGeneratorType.DEFAULT);
		MUSHROOM_ISLAND_WORLD = new CutsceneWorld(null, levelInfo, DimensionType.OVERWORLD, 1, null, null);

		// Configurations
		LevelProperties levelProperties = new LevelProperties(levelInfo, "Old Nether World");
		OverworldChunkGeneratorConfig chunkGenConfig = new OverworldChunkGeneratorConfig();
		VanillaLayeredBiomeSourceConfig biomeConfig = new VanillaLayeredBiomeSourceConfig();

		// Apply configurations
		biomeConfig.setLevelProperties(levelProperties);
		biomeConfig.setGeneratorSettings(chunkGenConfig);

		// Biome & Chunk Generators
		BiomeSource biomeSource = new VanillaLayeredBiomeSource(biomeConfig);
		ChunkGenerator generator = new OverworldChunkGenerator(MUSHROOM_ISLAND_WORLD, biomeSource, chunkGenConfig);

		int cX, cZ, pX, pY, pZ, index;

		int range = 15;
		for (cX = - range; cX < range; cX++)
		{
			for (cZ = - range; cZ < range; cZ++)
			{
				ChunkPos chunkPos = new ChunkPos(cX, cZ);

				// Step 1: Create Chunk
				ProtoChunk protoChunk = new ProtoChunk(chunkPos, new UpgradeData(new CompoundTag()));

				// Step 2: Generate Biomes
				generator.populateBiomes(protoChunk);

				// Step 3: Populate Noise
				generator.populateNoise(MUSHROOM_ISLAND_WORLD, protoChunk);

				// Step 4: Build Surface
				generator.buildSurface(protoChunk);

				// Step 5: Carve
				generator.carve(protoChunk, GenerationStep.Carver.AIR);
				generator.carve(protoChunk, GenerationStep.Carver.LIQUID);

				// Step 6: Populate Heightmaps
				Heightmap.populateHeightmaps(protoChunk, EnumSet.of(Heightmap.Type.MOTION_BLOCKING, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Heightmap.Type.OCEAN_FLOOR, Heightmap.Type.WORLD_SURFACE));

				// Step 7: Generate features
				generator.generateFeatures(new ChunkRegion(MUSHROOM_ISLAND_WORLD, Collections.singletonList(protoChunk)));

				// Create Cutscene Chunk
				CutsceneChunk cutsceneChunk = new CutsceneChunk(MUSHROOM_ISLAND_WORLD, chunkPos, protoChunk.getBiomeArray());
				BlockState[] states = cutsceneChunk.blockStates;

				// Transfer data to cutscene chunk
				for (pX = 0; pX < 16; pX++)
				{
					for (pY = 0; pY < cutsceneChunk.getHeight(); pY++)
					{
						for (pZ = 0; pZ < 16; pZ++)
						{
							index = pZ * 16 * cutsceneChunk.getHeight() + pY * 16 + pX;
							states[index] = protoChunk.getBlockState(new BlockPos(pX, pY, pZ));
						}
					}
				}

				// Save cutscene chunk to world
				MUSHROOM_ISLAND_WORLD.putChunk(cutsceneChunk);
			}
		}
	}
}
