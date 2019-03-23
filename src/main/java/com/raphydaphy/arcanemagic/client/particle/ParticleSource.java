package com.raphydaphy.arcanemagic.client.particle;

import java.util.function.Consumer;

public class ParticleSource
{
	private final Consumer<Integer> particles;
	private final int lifetime;
	private int age = 0;

	public ParticleSource(Consumer<Integer> particles, int lifetime)
	{
		this.particles = particles;
		this.lifetime = lifetime;
	}

	void update()
	{
		this.age++;
		if (alive())
		{
			particles.accept(age);
		}
	}

	boolean alive()
	{
		return age < lifetime;
	}
}
