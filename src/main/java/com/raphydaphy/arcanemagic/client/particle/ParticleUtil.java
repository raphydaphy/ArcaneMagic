package com.raphydaphy.arcanemagic.client.particle;

import java.util.Random;

import com.raphydaphy.arcanemagic.client.proxy.ClientProxy;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ParticleUtil {
	public static Random random = new Random();
	public static int counter = 0;

	public static void spawnParticleGlowTest(World world, float x, float y, float z, float vx, float vy, float vz,
			float r, float g, float b, float a, float scale, int lifetime, float gravity) {
		if (ArcaneMagic.proxy instanceof ClientProxy) {
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1
					: 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
				ClientProxy.particleRenderer
						.addParticle(new ParticleGlowTest(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime, gravity));
			}
			else
			{
				System.out.println("you shall not pass");
			}
		}
	}
}
