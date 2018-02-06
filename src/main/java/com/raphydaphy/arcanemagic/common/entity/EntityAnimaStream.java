package com.raphydaphy.arcanemagic.common.entity;

import java.util.List;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAnimaStream extends EntityThrowable
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

	public EntityAnimaStream(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r,
			float g, float b, float a, float scale, int lifetime, float gravity)
	{
		super(worldIn);

		this.setPosition(x, y, z);
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

	private void mainUpdate()
	{
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;

		{
			if (!world.isRemote)
			{
				setFlag(6, isGlowing());
			}

			onEntityUpdate();
		}

		if (throwableShake > 0)
		{
			--throwableShake;
		}

		Vec3d vec3d = new Vec3d(posX, posY, posZ);
		Vec3d vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
		RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(posX, posY, posZ);
		vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

		if (raytraceresult != null)
		{
			vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		if (!world.isRemote)
		{ // entity colliding on server
			Entity entity = null;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this,
					getEntityBoundingBox().offset(motionX, motionY, motionZ).grow(1.0D));
			double d0 = 0.0D;
			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity1 = list.get(i);

				if (entity1.canBeCollidedWith())
				{
					if (entity1 == ignoreEntity)
					{
					} else if (ticksExisted < 2 && ignoreEntity == null)
					{
						ignoreEntity = entity1;
					} else
					{
						AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
						RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

						if (raytraceresult1 != null)
						{
							double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

							if (d1 < d0 || d0 == 0.0D)
							{
								entity = entity1;
								d0 = d1;
							}
						}
					}
				}
			}

			if (entity != null)
			{
				raytraceresult = new RayTraceResult(entity);
			}
		} // End wrap - only do entity colliding on server

		if (raytraceresult != null)
		{
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK
					&& world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)
			{
				setPortal(raytraceresult.getBlockPos());
			} else
			{
				onImpact(raytraceresult);
			}
		}
		this.motionY -= 0.04D * (double) this.gravity;

		posX += motionX;
		posY += motionY;
		posZ += motionZ;

		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

		if (isInWater())
		{
			for (int j = 0; j < 4; ++j)
			{
				float f3 = 0.25F;
				world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * f3, posY - motionY * f3,
						posZ - motionZ * f3, motionX, motionY, motionZ, new int[0]);
			}
		}

		setPosition(posX, posY, posZ);

		if (rand.nextInt(6) == 0)
		{
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

	@Override
	public void onUpdate()
	{
		mainUpdate();

		animaStreamEffect();
	}

	public void animaStreamEffect()
	{
		if (isDead || !world.isRemote)
			return;

		ArcaneMagic.proxy.animaParticle(world, this.posX, this.posY, this.posZ, this.colorR, this.colorG, this.colorB,
				this.alpha, this.scale);
	}

	public boolean alive()
	{
		return this.age < this.maxAge;
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
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
	public void writeEntityToNBT(NBTTagCompound compound)
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

	@Override
	protected void onImpact(RayTraceResult result)
	{
		// TODO Auto-generated method stub
		
	}
}
