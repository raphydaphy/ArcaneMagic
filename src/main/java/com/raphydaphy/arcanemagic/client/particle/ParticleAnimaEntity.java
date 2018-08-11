package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleAnimaEntity extends Particle
{
    public float colorR = 0;
    public float colorG = 0;
    public float colorB = 0;
    public float initScale = 0;
    public float initAlpha = 0;

    public ParticleAnimaEntity(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r,
                                 float g, float b, float a, float scale, int lifetime, float gravity)
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
        this.particleMaxAge = (int) ((float) lifetime * 0.5f);
        this.particleScale = scale;
        this.initScale = scale;
        this.motionX = vx * 2.0f;
        this.motionY = vy * 2.0f;
        this.motionZ = vz * 2.0f;
        this.initAlpha = a;
        this.particleAngle = 2.0f * (float) Math.PI;
        this.particleGravity = gravity;
        this.particleAlpha = initAlpha;
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite(new ResourceLocation(ArcaneMagicResources.MOD_ID, "particle/plus").toString());
        this.setParticleTexture(sprite);
    }

    @Override
    public void renderParticle(BufferBuilder p_renderParticle_1_, Entity p_renderParticle_2_, float p_renderParticle_3_, float p_renderParticle_4_, float p_renderParticle_5_, float p_renderParticle_6_, float p_renderParticle_7_, float p_renderParticle_8_)
    {
        super.renderParticle(p_renderParticle_1_, p_renderParticle_2_, p_renderParticle_3_, p_renderParticle_4_, p_renderParticle_5_, p_renderParticle_6_, p_renderParticle_7_, p_renderParticle_8_);
    }

    @Override
    public int getBrightnessForRender(float pTicks)
    {
        return 255;
    }

    @Override
    public boolean shouldDisableDepth()
    {
        return true;
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    @Override
    public void onUpdate()
    {
        System.out.println("update");
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.motionY -= 0.04D * (double) this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
        if (rand.nextInt(6) == 0)
        {
            this.particleAge++;
        }
        this.prevParticleAngle = particleAngle;
        particleAngle += 1f;
    }
}
