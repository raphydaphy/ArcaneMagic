package com.raphydaphy.thaumcraft.world;

import java.util.Random;

import com.raphydaphy.thaumcraft.init.ModBlocks;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenGreatwood extends WorldGenHugeTrees implements IWorldGenerator
{
	private static final IBlockState TRUNK = ModBlocks.log_greatwood.getDefaultState();
    private static final IBlockState LEAF = ModBlocks.leaves_greatwood.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private final boolean useBaseHeight;

    public WorldGenGreatwood(boolean notify, boolean p_i45457_2_)
    {
        super(notify, 13, 15, TRUNK, LEAF);
        this.useBaseHeight = p_i45457_2_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
    	if (worldIn.getBiome(position).canRain() && rand.nextInt(10) == 1)
    	{
    		int i = this.getHeight(rand);

            if (!this.ensureGrowable(worldIn, rand, position, i))
            {
                return false;
            }
            else
            {
                this.createCrown(worldIn, position.getX(), position.getZ(), position.getY() + i, 0, rand);

                for (int j = 0; j < i; ++j)
                {
                    if (isAirLeaves(worldIn, position.up(j)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.up(j), this.woodMetadata);
                    }

                    if (j < i - 1)
                    {
                        if (isAirLeaves(worldIn, position.add(1, j, 0)))
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), this.woodMetadata);
                        }

                        if (isAirLeaves(worldIn, position.add(1, j, 1)))
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), this.woodMetadata);
                        }


                        if (isAirLeaves(worldIn, position.add(0, j, 1)))
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), this.woodMetadata);
                        }
                    }
                }

                return true;
            }
    	}
    	return false;
    }

    private void createCrown(World worldIn, int x, int z, int y, int p_150541_5_, Random rand)
    {
        int i = rand.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
        int j = 0;

        for (int k = y - i; k <= y; ++k)
        {
            int l = y - k;
            int i1 = p_150541_5_ + MathHelper.floor((float)l / (float)i * 3.5F);
            this.growLeavesLayerStrict(worldIn, new BlockPos(x, k, z), i1 + (l > 0 && i1 == j && (k & 1) == 0 ? 1 : 0));
            j = i1;
        }
    }

    //Helper macro
    private boolean isAirLeaves(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
    }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		this.generate(world, random, world.getTopSolidOrLiquidBlock(new BlockPos(chunkX * 16 + random.nextInt(16), 60, chunkZ * 16 + random.nextInt(16))));
	}
}