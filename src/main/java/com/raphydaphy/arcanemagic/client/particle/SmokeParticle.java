package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.world.World;

public class SmokeParticle extends ParticleBase
{
	public SmokeParticle(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ, float red, float green, float blue, float alpha, float scale, int lifetime)
	{
		super(worldIn, xPos, yPos, zPos, velocityX, velocityY, velocityZ, red, green, blue, alpha, scale, lifetime);
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(ArcaneMagicConstants.SMOKE_PARTICLE_TEXTURE);
		this.setSprite(sprite);
		this.prevAngle = angle;
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