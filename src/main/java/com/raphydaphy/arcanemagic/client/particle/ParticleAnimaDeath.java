package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
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
        this.particleScale *= 0.5F;
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
        float left = (float)this.particleTextureIndexX / 32.0F;
        float right = left + 0.03121875F;
        float up = (float)this.particleTextureIndexY / 32.0F;
        float down = up + 0.03121875F;
        float lvt_13_1_ = 0.1F * this.particleScale;

        float lvt_14_1_ = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_renderParticle_3_ - interpPosX);
        float lvt_15_1_ = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_renderParticle_3_ - interpPosY);
        float lvt_16_1_ = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_renderParticle_3_ - interpPosZ);
        int lvt_17_1_ = this.getBrightnessForRender(p_renderParticle_3_);
        int lvt_18_1_ = lvt_17_1_ >> 16;
        Vec3d[] lvt_20_1_ = new Vec3d[]{new Vec3d((double)(-p_renderParticle_4_ * lvt_13_1_ - p_renderParticle_7_ * lvt_13_1_), (double)(-p_renderParticle_5_ * lvt_13_1_), (double)(-p_renderParticle_6_ * lvt_13_1_ - p_renderParticle_8_ * lvt_13_1_)), new Vec3d((double)(-p_renderParticle_4_ * lvt_13_1_ + p_renderParticle_7_ * lvt_13_1_), (double)(p_renderParticle_5_ * lvt_13_1_), (double)(-p_renderParticle_6_ * lvt_13_1_ + p_renderParticle_8_ * lvt_13_1_)), new Vec3d((double)(p_renderParticle_4_ * lvt_13_1_ + p_renderParticle_7_ * lvt_13_1_), (double)(p_renderParticle_5_ * lvt_13_1_), (double)(p_renderParticle_6_ * lvt_13_1_ + p_renderParticle_8_ * lvt_13_1_)), new Vec3d((double)(p_renderParticle_4_ * lvt_13_1_ - p_renderParticle_7_ * lvt_13_1_), (double)(-p_renderParticle_5_ * lvt_13_1_), (double)(p_renderParticle_6_ * lvt_13_1_ - p_renderParticle_8_ * lvt_13_1_))};
        if (this.particleAngle != 0.0F) {
            float lvt_21_1_ = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * p_renderParticle_3_;
            float lvt_22_1_ = MathHelper.cos(lvt_21_1_ * 0.5F);
            float lvt_23_1_ = MathHelper.sin(lvt_21_1_ * 0.5F) * (float)cameraViewDir.x;
            float lvt_24_1_ = MathHelper.sin(lvt_21_1_ * 0.5F) * (float)cameraViewDir.y;
            float lvt_25_1_ = MathHelper.sin(lvt_21_1_ * 0.5F) * (float)cameraViewDir.z;
            Vec3d lvt_26_1_ = new Vec3d((double)lvt_23_1_, (double)lvt_24_1_, (double)lvt_25_1_);

            for(int lvt_27_1_ = 0; lvt_27_1_ < 4; ++lvt_27_1_) {
                lvt_20_1_[lvt_27_1_] = lvt_26_1_.scale(2.0D * lvt_20_1_[lvt_27_1_].dotProduct(lvt_26_1_)).add(lvt_20_1_[lvt_27_1_].scale((double)(lvt_22_1_ * lvt_22_1_) - lvt_26_1_.dotProduct(lvt_26_1_))).add(lvt_26_1_.crossProduct(lvt_20_1_[lvt_27_1_]).scale((double)(2.0F * lvt_22_1_)));
            }
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(ArcaneMagicResources.PARTICLES);

        builder .pos((double)lvt_14_1_ + lvt_20_1_[0].x, (double)lvt_15_1_ + lvt_20_1_[0].y, (double)lvt_16_1_ + lvt_20_1_[0].z)
                .tex((double)right, (double)down)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lvt_18_1_, lvt_17_1_).endVertex();
        builder .pos((double)lvt_14_1_ + lvt_20_1_[1].x, (double)lvt_15_1_ + lvt_20_1_[1].y, (double)lvt_16_1_ + lvt_20_1_[1].z)
                .tex((double)right, (double)up)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lvt_18_1_, lvt_17_1_).endVertex();
        builder .pos((double)lvt_14_1_ + lvt_20_1_[2].x, (double)lvt_15_1_ + lvt_20_1_[2].y, (double)lvt_16_1_ + lvt_20_1_[2].z)
                .tex((double)left, (double)up)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lvt_18_1_, lvt_17_1_).endVertex();
        builder .pos((double)lvt_14_1_ + lvt_20_1_[3].x, (double)lvt_15_1_ + lvt_20_1_[3].y, (double)lvt_16_1_ + lvt_20_1_[3].z)
                .tex((double)left, (double)down)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(lvt_18_1_, lvt_17_1_).endVertex();
    }
}
