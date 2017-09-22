package com.raphydaphy.thaumcraft.particle;

import java.util.Random;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ParticleUtil 
{
	public static Random random = new Random();
	public static int counter = 0;
	
	public static void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime)
	{
		if (Thaumcraft.proxy instanceof ClientProxy)
		{
			
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0)
			{
				ClientProxy.particleRenderer.addParticle(new ParticleStar(world,x,y,z,vx,vy,vz,r,g,b, scale, lifetime));
			}
		}
	}
}
