package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.ArcaneMagicClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.world.World;

public class SmokeParticle extends ParticleBase
{
	//private static Identifier texture = new Identifier(ArcaneMagic.DOMAIN, "particle/smoke_particle");

	public SmokeParticle(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ, float red, float green, float blue, float alpha, float scale, int lifetime)
	{
		super(worldIn, xPos, yPos, zPos, velocityX, velocityY, velocityZ, red, green, blue, alpha, scale, lifetime);
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(ArcaneMagicClient.GLOW_PARTICLE_TEXTURE);
		this.setSprite(sprite);
	}

	@Override
	public boolean isAdditive()
	{
		return false;
	}

	@Override
	public boolean renderThroughBlocks()
	{
		return false;
	}
}