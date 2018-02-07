package com.raphydaphy.arcanemagic.common.anima;

import java.util.Random;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class AnimaWorldData extends WorldSavedData
{
	public static final String NAME = ArcaneMagic.MODID + ":ANIMA_DATA";

	public AnimaWorldData()
	{
		super(NAME);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		AnimaGenerator.offsetX = tag.getInteger("offsetX");
		AnimaGenerator.offsetZ = tag.getInteger("offsetZ");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("offsetX", AnimaGenerator.offsetX);
		tag.setInteger("offsetZ", AnimaGenerator.offsetZ);
		return tag;
	}

	public static AnimaWorldData get(World world)
	{
		MapStorage storage = world.getPerWorldStorage();
		AnimaWorldData instance = (AnimaWorldData) storage.getOrLoadData(AnimaWorldData.class, NAME);

		if (instance == null)
		{
			instance = new AnimaWorldData();
			storage.setData(NAME, instance);
		}
		return instance;
	}

	public static class AnimaGenerator
	{
		public static int offsetX = 0;
		public static int offsetZ = 0;
		private static Random random = new Random();

		public static AnimaStack getAnima(World world, Anima anima, long seed, int x, int z)
		{
			if (anima != null)
			{
				Anima special = Anima.getFromBiome(world.getBiome(new BlockPos(x, 60, z)));
				boolean chunkSpecial = anima.equals(special);
				boolean chunkOpposite = anima.equals(Anima.getOpposite(special));

				int amount = 0;
				if (chunkSpecial)
				{
					amount = (int) (((stability(seed * Anima.getNum(anima), x, z) * 10)
							* (float) Math.pow((80.0f * getOctave(seed, x + offsetX, z + offsetZ, 112)
									+ 30.1f * getOctave(seed, x + offsetX, z + offsetZ, 68)
									+ 25.5f * getOctave(seed, x + offsetX, z + offsetZ, 34)
									+ 20.3f * getOctave(seed, x + offsetX, z + offsetZ, 21)
									+ 16.8f * getOctave(seed, x + offsetX, z + offsetZ, 11)
									+ 13.5f * getOctave(seed, x + offsetX, z + offsetZ, 4)) / 93.0f, 1.6f))
							* 10000);
				} else if (!chunkOpposite)
				{
					amount = (int) ((stability(
							seed * Anima.getNum(anima)
									+ 1,
							x, z)
							* (float) Math.pow((80.0f * getOctave(seed, x + offsetX, z + offsetZ, 112)
									+ 20.8f * getOctave(seed, x + offsetX, z + offsetZ, 68)
									+ 6.6f * getOctave(seed, x + offsetX, z + offsetZ, 34)
									+ 4.5f * getOctave(seed, x + offsetX, z + offsetZ, 21)
									+ 2.4f * getOctave(seed, x + offsetX, z + offsetZ, 11)
									+ 1.7f * getOctave(seed, x + offsetX, z + offsetZ, 4)) / 93.0f, 1.6f))
							* 10000);
				}
				return new AnimaStack(anima, amount);
			}
			return null;

		}

		private static float stability(long seed, int x, int z)
		{
			return 1.0f - (float) Math.pow((32.0f * getOctave(seed, x, z, 120) + 16.0f * getOctave(seed, x, z, 76)
					+ 6.0f * getOctave(seed, x, z, 45) + 3.0f * getOctave(seed, x, z, 21)
					+ 1.0f * getOctave(seed, x, z, 5)) / 58.0f, 3.0f);
		}

		private static float interpolate(float s, float e, float t)
		{
			float t2 = (1.0f - (float) Math.cos(t * 3.14159265358979323f)) / 2.0f;
			return (s * (1.0f - t2) + (e) * t2);
		}

		private static float bilinear(float ul, float ur, float dr, float dl, float t1, float t2)
		{
			return interpolate(interpolate(ul, ur, t1), interpolate(dl, dr, t1), t2);
		}

		private static float getOctave(long seed, int x, int y, int dimen)
		{
			return bilinear(
					noise(seed, (int) Math.floor((float) x / (float) dimen) * dimen,
							(int) Math.floor((float) y / (float) dimen) * dimen),
					noise(seed, (int) Math.floor((float) x / (float) dimen) * dimen + dimen,
							(int) Math.floor((float) y / (float) dimen) * dimen),
					noise(seed, (int) Math.floor((float) x / (float) dimen) * dimen + dimen,
							(int) Math.floor((float) y / (float) dimen) * dimen + dimen),
					noise(seed, (int) Math.floor((float) x / (float) dimen) * dimen,
							(int) Math.floor((float) y / (float) dimen) * dimen + dimen),
					Math.abs(((float) (((x) - Math.floor(((float) x / (float) dimen)) * dimen))) / ((float) dimen)),
					Math.abs(((float) (((y) - Math.floor(((float) y / (float) dimen)) * dimen))) / ((float) dimen)));
		}

		private static int hash(int[] is, int count)
		{
			int i;
			int hash = 80238287;

			for (i = 0; i < count; i++)
			{
				hash = (hash << 4) ^ (hash >> 28) ^ (is[i] * 5449 % 130651);
			}

			return hash % 75327403;
		}

		private static float noise(long seed, int x, int y)
		{
			random.setSeed(hash(new int[] { (int) seed, (int) (seed << 32), (int) Math.signum(y) * 512 + 512,
					(int) Math.signum(x) * 512 + 512, x, y }, 6));
			return random.nextFloat();
		}

	}

}