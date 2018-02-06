package com.raphydaphy.arcanemagic.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityAnimaStream extends Entity
{
	public float gravity;
	private int maxAge;
	private int age = 0;
	
	public float colorR = 0;
	public float colorG = 0;
	public float colorB = 0;
	
	public float initScale = 0;
	public float scale;
	
	public float initAlpha = 0;
	public float alpha;
	
	public float angle = 2.0f * (float) Math.PI;
	public float prevAngle = angle;

	public EntityAnimaStream(World worldIn)
	{
		super(worldIn);
	}
	public EntityAnimaStream(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime, float gravity) {
		super(worldIn);
		
		this.setPosition(x, y, z);
		this.colorR = r;
		this.colorG = g;
		this.colorB = b;
		if (this.colorR > 1.0) {
			this.colorR = this.colorR / 255.0f;
		}
		if (this.colorG > 1.0) {
			this.colorG = this.colorG / 255.0f;
		}
		if (this.colorB > 1.0) {
			this.colorB = this.colorB / 255.0f;
		}
		this.scale = scale;
		this.initScale = scale;
		this.maxAge = (int) ((float) lifetime * 0.5f);
		this.motionX = vx * 2.0f;
		this.motionY = vy * 2.0f;
		this.motionZ = vz * 2.0f;
		this.gravity = gravity;
		
		this.initAlpha = a;
		this.alpha = initAlpha;
	}
	
	public void move(double x, double y, double z)
    {
        double d0 = y;
        double origX = x;
        double origZ = z;
/*
            List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, this.getCollisionBoundingBox().expand(x, y, z));

            for (AxisAlignedBB axisalignedbb : list)
            {
                y = axisalignedbb.calculateYOffset(this.getCollisionBoundingBox(), y);
            }

            this.setEntityBoundingBox(this.getCollisionBoundingBox().offset(0.0D, y, 0.0D));

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                x = axisalignedbb1.calculateXOffset(this.getCollisionBoundingBox(), x);
            }

            this.setEntityBoundingBox(this.getCollisionBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb2 : list)
            {
                z = axisalignedbb2.calculateZOffset(this.getCollisionBoundingBox(), z);
            }
*/
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x,y,z));
       

        this.resetPositionToBB();
        this.onGround = d0 != y && d0 < 0.0D;

        if (origX != x)
        {
            this.motionX = 0.0D;
        }

        if (origZ != z)
        {
            this.motionZ = 0.0D;
        }
    }
	
	@Override
	public void onEntityUpdate()
    {
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
        {
            this.age++;
        }

        this.motionY -= 0.04D * (double)this.gravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        
        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
		if (rand.nextInt(6) == 0) {
			this.age++;
		}
		this.age = maxAge;
		
		this.prevAngle = angle;
		angle += 1f;
		
		if (!alive())
		{
			this.setDead();
		}
    }
	
	public boolean alive() {
		return this.age < this.maxAge;
	}

	@Override
	protected void entityInit()
	{
		
	}

	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.gravity = compound.getFloat("gravity");
		
		this.age = compound.getInteger("age");
		this.maxAge = compound.getInteger("maxAge");
		
		this.colorR = compound.getFloat("colorR");
		this.colorG = compound.getFloat("colorG");
		this.colorB = compound.getFloat("colorB");
		
		this.initScale = compound.getFloat("ïnitScale");
		this.scale = compound.getFloat("scale");
		
		this.initAlpha = compound.getFloat("initAlpha");
		this.alpha = compound.getFloat("älpha");
		
		this.angle = compound.getFloat("angle");
		this.prevAngle = compound.getFloat("prevAngle");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setFloat("gravity", this.gravity);
		
		compound.setInteger("age", this.age);
		compound.setInteger("maxAge", this.maxAge);
		
		compound.setFloat("colorR", this.colorR);
		compound.setFloat("colorG", this.colorG);
		compound.setFloat("colorB", this.colorB);
		
		compound.setFloat("initScale", this.initScale);
		compound.setFloat("scale", this.scale);
		
		compound.setFloat("initAlpha", this.initAlpha);
		compound.setFloat("alpha", this.alpha);
		
		compound.setFloat("angle", this.angle);
		compound.setFloat("prevAngle", this.prevAngle);
	}
}
