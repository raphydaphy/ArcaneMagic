package com.raphydaphy.arcanemagic.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;

import java.util.Random;

// Derived from Elucent's particle system in Embers
// Source (no longer available)
@Environment(EnvType.CLIENT)
public class ParticleUtil
{
	public static Random random = new Random();
	public static int counter = 0;

	public static void spawnGlowParticle(World world, float posX, float posY, float posZ, float velocityX, float velocityY, float velocityZ, float red, float green, float blue, float alpha, float scale, int lifetime)
	{
		counter += random.nextInt(3);
		if (counter % (MinecraftClient.getInstance().options.particles.getId() == 0 ? 1 : 2 * MinecraftClient.getInstance().options.particles.getId()) == 0)
		{
			ParticleRenderer.INSTANCE.add(new GlowParticle(world, posX, posY, posZ, velocityX, velocityY, velocityZ, red, green, blue, alpha, scale, lifetime));
		}
	}
}
