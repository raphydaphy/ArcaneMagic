package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleAnimaDeath extends Particle
{
    protected final int textureIdx;
    protected final int numAgingFrames;
    private final float yAccel;
    private final float baseAirFriction = 1;
    private float fadeTargetRed;
    private float fadeTargetGreen;
    private float fadeTargetBlue;
    private boolean fadingColor;

    private final BlockPos destination;

    public ParticleAnimaDeath(World world, BlockPos destination, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(world, xCoordIn, yCoordIn, zCoordIn);
        if (destination == null)
		{
			this.motionX = xSpeedIn;
			this.motionY = ySpeedIn;
			this.motionZ = zSpeedIn;
		}
		else {
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
		}
        this.particleScale *= 0.5F;
        this.particleMaxAge = 60 + this.rand.nextInt(12);
        this.destination = destination;
        setColor(0x6e009f);
        setColorFade(0x8c3baf);
        this.textureIdx = 0;
        this.numAgingFrames = 8;
        this.yAccel = -5.0E-4F;
    }

    public void setColor(int p_setColor_1_) // ParticleSimpleAnimated
    {
        float lvt_2_1_ = (float) ((p_setColor_1_ & 16711680) >> 16) / 255.0F;
        float lvt_3_1_ = (float) ((p_setColor_1_ & '\uff00') >> 8) / 255.0F;
        float lvt_4_1_ = (float) ((p_setColor_1_ & 255)) / 255.0F;
        this.setRBGColorF(lvt_2_1_ * 1.0F, lvt_3_1_ * 1.0F, lvt_4_1_ * 1.0F);
    }

    public void setColorFade(int p_setColorFade_1_) // ParticleSimpleAnimated
    {
        this.fadeTargetRed = (float) ((p_setColorFade_1_ & 16711680) >> 16) / 255.0F;
        this.fadeTargetGreen = (float) ((p_setColorFade_1_ & '\uff00') >> 8) / 255.0F;
        this.fadeTargetBlue = (float) ((p_setColorFade_1_ & 255)) / 255.0F;
        this.fadingColor = true;
    }

    @Override // ParticleSimpleAnimated
    public boolean shouldDisableDepth()
    {
        return true;
    }

    @Override // ParticleEndRod
    public void move(double p_move_1_, double p_move_3_, double p_move_5_)
    {
        this.setBoundingBox(this.getBoundingBox().offset(p_move_1_, p_move_3_, p_move_5_));
        this.resetPositionToBB();
    }

    @Override
    public void renderParticle(BufferBuilder builder, Entity entity, float p_renderParticle_3_, float p_renderParticle_4_, float p_renderParticle_5_, float p_renderParticle_6_, float p_renderParticle_7_, float p_renderParticle_8_)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ArcaneMagicResources.PARTICLES);
        super.renderParticle(builder, entity, p_renderParticle_3_, p_renderParticle_4_, p_renderParticle_5_, p_renderParticle_6_, p_renderParticle_7_, p_renderParticle_8_);
    }

    @Override // ParticleSimpleAnimated with edits
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        if (this.particleAge > this.particleMaxAge / 2)
        {
            this.setAlphaF(1.0F - ((float) this.particleAge - (float) (this.particleMaxAge / 2)) / (float) this.particleMaxAge);
            if (this.fadingColor)
            {
                this.particleRed += (this.fadeTargetRed - this.particleRed) * 0.2F;
                this.particleGreen += (this.fadeTargetGreen - this.particleGreen) * 0.2F;
                this.particleBlue += (this.fadeTargetBlue - this.particleBlue) * 0.2F;
            }
        }

        this.setParticleTextureIndex(this.textureIdx + this.numAgingFrames - 1 - this.particleAge * this.numAgingFrames / this.particleMaxAge);
        if (destination == null)
        {
            this.motionY += (double) this.yAccel;
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        if (destination == null)
        {
            this.motionX *= (double) this.baseAirFriction;
            this.motionY *= (double) this.baseAirFriction;
            this.motionZ *= (double) this.baseAirFriction;

            if (this.onGround)
            {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
            }
        }
        else
		{
			Vector3f motion = ArcaneMagicUtils.lerp((float)posX, (float)posY, (float)posZ, destination.getX() + 0.5f, destination.getY() + 0.6f, destination.getZ() + 0.5f, (float)particleAge / particleMaxAge);
			motionX = motion.func_195899_a() - posX;
			motionY = motion.func_195900_b() - posY;
			motionZ = motion.func_195902_c() - posZ;
		}
    }
}
