package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.world.World;

// Derived from Elucent's particle system in Embers
// Source (no longer available)
public abstract class ParticleBase extends SpriteBillboardParticle implements ArcaneMagicParticle
{
	private float colorR = 0;
	private float colorG = 0;
	private float colorB = 0;
	private float initScale = 0;
	private float initAlpha = 0;

	public ParticleBase(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ, float red, float green, float blue, float alpha, float scale, int lifetime)
	{
		super(worldIn, xPos, yPos, zPos, 0, 0, 0);
		this.colorR = red;
		this.colorG = green;
		this.colorB = blue;
		if (this.colorR > 1.0)
		{
			this.colorR = this.colorR / 255.0f;
		}
		if (this.colorG > 1.0)
		{
			this.colorG = this.colorG / 255.0f;
		}
		if (this.colorB > 1.0)
		{
			this.colorB = this.colorB / 255.0f;
		}
		this.setColor(colorR, colorG, colorB);
		this.maxAge = lifetime;
		this.scale = scale;
		this.initScale = scale;
		this.velocityX = velocityX * 2.0f;
		this.velocityY = velocityY * 2.0f;
		this.velocityZ = velocityZ * 2.0f;
		this.initAlpha = alpha;
		this.angle = 2.0f * (float) Math.PI;
	}

	@Override
	public void update()
	{
		super.update();
		if (ArcaneMagic.RANDOM.nextInt(6) == 0)
		{
			this.age++;
		}
		float lifeCoeff = (float) this.age / (float) this.maxAge;
		if (lifeCoeff < 1)
		{
			this.scale = initScale - initScale * lifeCoeff;
			this.colorAlpha = initAlpha * (1.0f - lifeCoeff);
		}
	}

	@Override
	public boolean alive()
	{
		return this.age < this.maxAge;
	}

	@Override
	public ParticleTextureSheet getTextureSheet()
	{
		// Similar to getFXLayer
		// CUSTOM
		return ParticleTextureSheet.CUSTOM;
	}
}
