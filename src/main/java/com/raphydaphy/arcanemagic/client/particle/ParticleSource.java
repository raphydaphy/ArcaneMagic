package com.raphydaphy.arcanemagic.client.particle;

import java.util.function.Consumer;

public class ParticleSource
{
	private final Consumer<Integer> particles;
	private int age = 0;
	private final int lifetime;

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
