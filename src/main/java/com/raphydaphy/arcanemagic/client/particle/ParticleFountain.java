package com.raphydaphy.arcanemagic.client.particle;


import org.lwjgl.util.vector.Vector3f;

import com.raphydaphy.arcanemagic.client.ClientEvents;

public class ParticleFountain {
	private Vector3f position;
	private Vector3f velocity;
	private float gravity;
	private float life;
	private float rotation;
	private float scale;

	private float elapsedTime = 0;
	
	public ParticleFountain(Vector3f position, Vector3f velocity, float gravity, float life, float rotation,
			float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravity = gravity;
		this.life = life;
		this.rotation = rotation;
		this.scale = scale;
		
		ClientEvents.particles.add(this);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public boolean update()
	{
		velocity.y = gravity;
		Vector3f change = new Vector3f(velocity);
		Vector3f.add(change, position, position);
		elapsedTime++;
		return elapsedTime < life;
	}
	
}
