package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.world.World;

public class GlowParticle extends ParticleBase {
    private final boolean lit;

    public GlowParticle(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ, float red, float green, float blue, float alpha, boolean lit, float scale, int lifetime) {
        super(worldIn, xPos, yPos, zPos, velocityX, velocityY, velocityZ, red, green, blue, alpha, scale, (int) ((float) lifetime * 0.5f));
        this.lit = lit;
        Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(ArcaneMagicConstants.GLOW_PARTICLE_TEXTURE);
        this.setSprite(sprite);
    }

    @Override
    public void update() {
        super.update();
        this.prevAngle = angle;
        angle += 1.0f;
    }

    @Override
    public boolean isAdditive() {
        return lit;
    }

    @Override
    public boolean renderThroughBlocks() {
        return false;
    }
}