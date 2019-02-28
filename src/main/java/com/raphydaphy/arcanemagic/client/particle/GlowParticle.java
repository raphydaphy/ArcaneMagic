package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.ArcaneMagicClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.world.World;

public class GlowParticle extends ParticleBase
{
	//private static Identifier texture = new Identifier(ArcaneMagic.DOMAIN, "particle/glow_particle");

	public GlowParticle(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ, float red, float green, float blue, float alpha, float scale, int lifetime)
	{
		super(worldIn, xPos, yPos, zPos, velocityX, velocityY, velocityZ, red, green, blue, alpha, scale, (int) ((float) lifetime * 0.5f));
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(ArcaneMagicClient.GLOW_PARTICLE_TEXTURE);
		this.setSprite(sprite);
	}

	@Override
	public void update()
	{
		super.update();
		this.field_3857 = field_3839; // prevParticleAngle = particleAngle
		field_3839 += 1.0f; // particleAngle
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