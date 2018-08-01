package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleAnimaDeath extends ParticleSimpleAnimated
{
    public ParticleAnimaDeath(World world, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(world, xCoordIn, yCoordIn, zCoordIn, 0, 8, -5.0E-4F);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.particleScale *= 0.75F;
        this.particleMaxAge = 60 + this.rand.nextInt(12);
        setBaseAirFriction(1);
        setColor(0x6e009f);
        setColorFade(0x8c3baf);
    }

    @Override
    public void move(double p_move_1_, double p_move_3_, double p_move_5_)
    {
        this.setBoundingBox(this.getBoundingBox().offset(p_move_1_, p_move_3_, p_move_5_));
        this.resetPositionToBB();
    }

    @Override
    public int getBrightnessForRender(float p_getBrightnessForRender_1_)
    {
        BlockPos lvt_2_1_ = new BlockPos(this.posX, this.posY, this.posZ);
        return this.world.isBlockLoaded(lvt_2_1_) ? this.world.getCombinedLight(lvt_2_1_, 0) : 0;
    }

    @Override
    public void renderParticle(BufferBuilder builder, Entity entity, float p_renderParticle_3_, float p_renderParticle_4_, float p_renderParticle_5_, float p_renderParticle_6_, float p_renderParticle_7_, float p_renderParticle_8_)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ArcaneMagicResources.DEATH_PARTICLES);
        super.renderParticle(builder, entity, p_renderParticle_3_, p_renderParticle_4_, p_renderParticle_5_, p_renderParticle_6_, p_renderParticle_7_, p_renderParticle_8_);
    }
}
