package com.raphydaphy.arcanemagic.client.particle;

import net.minecraft.class_3999;
import net.minecraft.class_4003;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// Derived from Elucent's particle system in Embers
// Source (no longer available)
public class GlowParticle extends class_4003 implements ArcaneMagicParticle
{
	private static Identifier texture = new Identifier("arcanemagic:misc/glow_particle");

	private float colorR = 0;
	private float colorG = 0;
	private float colorB = 0;
	private float initScale = 0;
	private float initAlpha = 0;

	public GlowParticle(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime)
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
		this.setColor(colorR, colorG, colorB);
		this.maxAge = (int) ((float) lifetime * 0.5f);
		this.field_17867 = scale; // particleScale
		this.initScale = scale;
		this.velocityX = vx * 2.0f;
		this.velocityY = vy * 2.0f;
		this.velocityZ = vz * 2.0f;
		this.initAlpha = a;
		this.field_3839 = 2.0f * (float) Math.PI; // particleAngle
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(texture.toString());
		this.method_18141(sprite); // setParticleTexture
	}
    /*
	@Override
	public void buildGeometry(BufferBuilder buffer, Entity entity, float partialTicks, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ){
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		super.renderParticle(buffer, entity, partialTicks, rotX, rotZ, rotYZ, rotXY, rotXZ);
	}*/

	@Override
	public void update()
	{
		super.update();
		if (ParticleUtil.random.nextInt(6) == 0)
		{
			this.age++;
		}
		float lifeCoeff = (float) this.age / (float) this.maxAge;
		this.field_3839 = initScale - initScale * lifeCoeff; // particleScale
		this.colorAlpha = initAlpha * (1.0f - lifeCoeff);
		this.field_3857 = field_3839; // prevParticleAngle = particleAngle
		field_3839 += 1.0f; // particleAngle
	}

	@Override
	public boolean alive()
	{
		return this.age < this.maxAge;
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

	@Override
	public class_3999 method_18122()
	{
		// Similar to getFXLayer
		// CUSTOM
		return class_3999.field_17831;
	}
}