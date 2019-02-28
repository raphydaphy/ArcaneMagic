package com.raphydaphy.arcanemagic.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// Derived from Elucent's particle system in Embers
// Source (no longer available)
public class GlowParticle extends SpriteBillboardParticle implements ArcaneMagicParticle
{
	private static Identifier texture = new Identifier("arcanemagic:misc/glow_particle");

	private float colorR = 0;
	private float colorG = 0;
	private float colorB = 0;
	private float initScale = 0;
	private float initAlpha = 0;

	public GlowParticle(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ, float red, float green, float blue, float alpha, float scale, int lifetime)
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
		this.maxAge = (int) ((float) lifetime * 0.5f);
		this.field_17867 = scale; // particleScale
		this.initScale = scale;
		this.velocityX = velocityX * 2.0f;
		this.velocityY = velocityY * 2.0f;
		this.velocityZ = velocityZ * 2.0f;
		this.initAlpha = alpha;
		this.field_3839 = 2.0f * (float) Math.PI; // particleAngle
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(texture.toString());
		this.setSprite(sprite);
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
	public ParticleTextureSheet getTextureSheet()
	{
		// Similar to getFXLayer
		// CUSTOM
		return ParticleTextureSheet.CUSTOM;
	}
}