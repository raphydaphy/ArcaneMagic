package com.raphydaphy.arcanemagic.entity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.particle.ParticleAnimaEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityAnima extends EntityThrowable
{
    public EntityAnima(World world)
    {
        super(ArcaneMagic.ANIMA_ENTITY, world);
    }

    // super.onUpdate without drag
    private void modifiedUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.throwableShake > 0)
        {
            --this.throwableShake;
        }

        if (this.inGround)
        {
            this.inGround = false;
            this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
        }

        Vec3d lvt_1_1_ = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d lvt_2_1_ = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult lvt_3_1_ = this.world.rayTraceBlocks(lvt_1_1_, lvt_2_1_);
        lvt_1_1_ = new Vec3d(this.posX, this.posY, this.posZ);
        lvt_2_1_ = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (lvt_3_1_ != null)
        {
            lvt_2_1_ = new Vec3d(lvt_3_1_.hitVec.x, lvt_3_1_.hitVec.y, lvt_3_1_.hitVec.z);
        }

        Entity lvt_4_1_ = null;
        List<Entity> lvt_5_1_ = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double lvt_6_1_ = 0.0D;
        boolean flag = false;

        for (int lvt_9_1_ = 0; lvt_9_1_ < lvt_5_1_.size(); ++lvt_9_1_)
        {
            Entity lvt_10_1_ = (Entity) lvt_5_1_.get(lvt_9_1_);
            if (lvt_10_1_.canBeCollidedWith())
            {
                if (lvt_10_1_ == this.ignoreEntity)
                {
                    flag = true;
                } else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null)
                {
                    this.ignoreEntity = lvt_10_1_;
                    flag = true;
                } else
                {
                    flag = false;
                    AxisAlignedBB lvt_11_1_ = lvt_10_1_.getEntityBoundingBox().grow(0.30000001192092896D);
                    RayTraceResult lvt_12_1_ = lvt_11_1_.calculateIntercept(lvt_1_1_, lvt_2_1_);
                    if (lvt_12_1_ != null)
                    {
                        double lvt_13_1_ = lvt_1_1_.squareDistanceTo(lvt_12_1_.hitVec);
                        if (lvt_13_1_ < lvt_6_1_ || lvt_6_1_ == 0.0D)
                        {
                            lvt_4_1_ = lvt_10_1_;
                            lvt_6_1_ = lvt_13_1_;
                        }
                    }
                }
            }
        }

        if (this.ignoreEntity != null)
        {
            /* ignoreTime is private and i dont think we need it...
            if (flag) {
                this.ignoreTime = 2;
            } else if (this.ignoreTime-- <= 0) {
                this.ignoreEntity = null;
            }
            */
        }

        if (lvt_4_1_ != null)
        {
            lvt_3_1_ = new RayTraceResult(lvt_4_1_);
        }

        if (lvt_3_1_ != null)
        {
            if (lvt_3_1_.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(lvt_3_1_.getBlockPos()).getBlock() == Blocks.PORTAL)
            {
                this.setPortal(lvt_3_1_.getBlockPos());
            } else
            {
                this.onImpact(lvt_3_1_);
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float lvt_9_2_ = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.2957763671875D);

        for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) lvt_9_2_) * 57.2957763671875D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float lvt_10_2_ = 0.99F;
        float lvt_11_2_ = this.getGravityVelocity();
        if (this.isInWater())
        {
            for (int lvt_12_2_ = 0; lvt_12_2_ < 4; ++lvt_12_2_)
            {
                float lvt_13_2_ = 0.25F;
                this.world.addParticle(Particles.BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
            }

            lvt_10_2_ = 0.8F;
        }

        // removed drag calculations because anima streams don't drag

        if (!this.hasNoGravity())
        {
            this.motionY -= (double) lvt_11_2_;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @Override
    public void onUpdate()
    {
        modifiedUpdate();

        if (world.isRemote)
        {
            particles();
        }
    }

    @Override
    protected void onImpact(RayTraceResult rayTraceResult)
    {
        System.out.println("i no longer have a soul");
    }

    private void particles()
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleAnimaEntity(world, posX, posY, posZ, 1, 0, 0, 1, 1, 10));
        System.out.println("doing my part");
    }

    @Override
    protected void entityInit()
    {

    }
}
