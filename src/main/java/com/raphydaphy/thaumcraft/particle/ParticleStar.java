package com.raphydaphy.thaumcraft.particle;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleStar extends Particle implements IThaumcraftParticle
{
	public float colorR = 0;
	public float colorG = 0;
	public float colorB = 0;
	public float rotScale = rand.nextFloat() * 0.1f + 0.05f;
	public float initScale = 0;
	public ResourceLocation texture = new ResourceLocation(Thaumcraft.MODID + ":misc/particle_star");

	public ParticleStar(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g,
			float b, float scale, int lifetime)
	{
		super(worldIn, x, y, z, 0, 0, 0);
		this.colorR = r;
		this.colorG = g;
		this.colorB = b;
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
		this.setRBGColorF(colorR, colorG, colorB);
		this.particleMaxAge = lifetime;
		this.particleScale = scale;
		this.initScale = scale;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
		this.particleAngle = 2.0f * (float) Math.PI;
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
		this.setParticleTexture(sprite);
	}

	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge * 32.0F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	@Override
	public int getBrightnessForRender(float pTicks)
	{
		return 255;
	}

	@Override
	public int getFXLayer()
	{
		return 1;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (world.rand.nextInt(6) == 0)
		{
			this.particleAge++;
		}
		float lifeCoeff = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleScale = initScale - initScale * lifeCoeff;
		this.particleAlpha = (1.0f - lifeCoeff) * (1.0f - lifeCoeff);
		this.prevParticleAngle = particleAngle;
		particleAngle += rotScale;
	}

	@Override
	public boolean alive()
	{
		return this.particleAge < this.particleMaxAge;
	}

	@Override
	public boolean isAdditive()
	{
		return true;
	}

	@Override
	public boolean renderThroughBlocks()
	{
		return false;
	}
}
