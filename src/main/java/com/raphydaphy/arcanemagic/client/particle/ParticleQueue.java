package com.raphydaphy.arcanemagic.client.particle;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleQueue
{
	private Map<ParticlePos, Integer> particles = new HashMap<ParticlePos, Integer>();

	public static final ParticleQueue INSTANCE = new ParticleQueue();

	public ParticleQueue()
	{

	}

	public static ParticleQueue getInstance()
	{
		return INSTANCE;
	}

	public void addParticle(World world, ParticlePos pos)
	{
		if (world.isRemote)
		{
			particles.put(pos, 50);
		}
	}

	private void doParticle(World world, ParticlePos ppos)
	{
		if (world.rand.nextInt(3) == 1)
		{
			BlockPos pos = ppos.getPos();
			for (int x = 0; x < 1; x++)
			{
				switch (ppos.getFacing())
				{
				case UP:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getZ() + 0.4 + (world.rand.nextFloat() / 4), 0, 0.3, 0);
					break;
				case EAST:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() - (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							1, 0.5, 0);
					break;
				case WEST:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							-1, 0.5, 0);
					break;
				case SOUTH:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							0, 0.5, 1);
					break;
				case NORTH:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							0, 0.5, -1);
					break;
				case DOWN:
					break;
				}
			}
		}
	}

	public void updateQueue(World world, EntityPlayer player)
	{
		if (world.isRemote)
		{
			Map<ParticlePos, Integer> newParticles = new HashMap<ParticlePos, Integer>();
			for (ParticlePos p : particles.keySet())
			{

				if (particles.get(p) > 0)
				{

					doParticle(world, p);
					newParticles.put(p, particles.get(p) - 1);
				}
			}
			particles = newParticles;
		}
	}
}
