package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleAnimaEntity extends Particle
{
    public float colorR = 0;
    public float colorG = 0;
    public float colorB = 0;
    public float initScale = 0;
    public float initAlpha = 0;

    public ParticleAnimaEntity(World worldIn, double x, double y, double z, float r, float g, float b, float a, float scale, int lifetime)
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
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.initAlpha = a;
        this.particleAngle = 2.0f * (float) Math.PI;
        this.particleGravity = 0;
        this.particleAlpha = initAlpha;
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite(new ResourceLocation(ArcaneMagicResources.MOD_ID, "particle/plus").toString());
        this.setParticleTexture(sprite);
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
        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        if (rand.nextInt(6) == 0)
        {
            this.particleAge++;
        }
        this.prevParticleAngle = particleAngle;
        particleAngle += 1f;
    }
}
