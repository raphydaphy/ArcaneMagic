package com.raphydaphy.arcanemagic.common.entity;

import java.util.List;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAnimaStream extends EntityThrowable {
	
	private static final DataParameter<Integer> RED = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> GREEN = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> BLUE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	
	private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);

	private static final DataParameter<Float> INITSCALE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	
	private static final DataParameter<Float> INITALPHA = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ALPHA = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> PREVANGLE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	
	private static final DataParameter<Integer> AGE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MAXAGE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	
	public EntityAnimaStream(World worldIn) {
		super(worldIn);
	}

	public EntityAnimaStream(World worldIn, double x, double y, double z, double vx, double vy, double vz, int r,
			int g, int b, float a, float scale, int lifetime, float gravity) {
		super(worldIn);
		this.setPosition(x, y, z);
		dataManager.set(RED, r);
		dataManager.set(GREEN, g);
		dataManager.set(BLUE, b);
		dataManager.set(ANGLE, 2.0f * (float) Math.PI);
		dataManager.set(PREVANGLE, (float)dataManager.get(ANGLE));
		dataManager.set(AGE, 0);
		dataManager.set(SCALE, scale);
		dataManager.set(INITSCALE, scale);
		dataManager.set(MAXAGE, (int) ((float) lifetime * 0.5f));
		this.motionX = vx * 2.0f;
		this.motionY = vy * 2.0f;
		this.motionZ = vz * 2.0f;
		dataManager.set(GRAVITY, gravity);
		dataManager.set(INITALPHA, a);
		dataManager.set(ALPHA, a);
	}

	private void mainUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;

		if (!world.isRemote) {
			setFlag(6, isGlowing());
		}

		onEntityUpdate();

		if (throwableShake > 0) {
			--throwableShake;
		}

		Vec3d vec3d = new Vec3d(posX, posY, posZ);
		Vec3d vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
		RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(posX, posY, posZ);
		vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

		if (raytraceresult != null) {
			vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		if (!world.isRemote) { // entity colliding on server
			Entity entity = null;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this,
					getEntityBoundingBox().offset(motionX, motionY, motionZ).grow(1.0D));
			double d0 = 0.0D;
			for (int i = 0; i < list.size(); ++i) {
				Entity entity1 = list.get(i);

				if (entity1.canBeCollidedWith()) {
					if (entity1 == ignoreEntity) {
					} else if (ticksExisted < 2 && ignoreEntity == null) {
						ignoreEntity = entity1;
					} else {
						AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
						RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

						if (raytraceresult1 != null) {
							double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

							if (d1 < d0 || d0 == 0.0D) {
								entity = entity1;
								d0 = d1;
							}
						}
					}
				}
			}

			if (entity != null) {
				raytraceresult = new RayTraceResult(entity);
			}
		} // End wrap - only do entity colliding on server

		if (raytraceresult != null) {
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK
					&& world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL) {
				setPortal(raytraceresult.getBlockPos());
			} else {
				onImpact(raytraceresult);
			}
		}
		this.motionY -= 0.04D * (double) dataManager.get(GRAVITY);

		posX += motionX;
		posY += motionY;
		posZ += motionZ;

		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

		if (isInWater()) {
			for (int j = 0; j < 4; ++j) {
				float f3 = 0.25F;
				world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * f3, posY - motionY * f3,
						posZ - motionZ * f3, motionX, motionY, motionZ, new int[0]);
			}
		}

		setPosition(posX, posY, posZ);

		if (rand.nextInt(6) == 0) {
			dataManager.set(AGE, dataManager.get(AGE) + 1);
		}
		// wat? this.age = maxAge;

		dataManager.set(PREVANGLE, dataManager.get(ANGLE));
		dataManager.set(ANGLE, dataManager.get(ANGLE) + 1);

		if (!alive()) {
			this.setDead();
		}

	}

	@Override
	public void onUpdate() {
		mainUpdate();

		animaStreamEffect();
	}

	public void animaStreamEffect() {
		if (!world.isRemote)
			return;
		ArcaneMagic.proxy.animaParticle(world, posX, posY, posZ, dataManager.get(RED), dataManager.get(GREEN), dataManager.get(BLUE), 1, 1);
	}

	public boolean alive() {
		return dataManager.get(AGE) < dataManager.get(MAXAGE);
	}
/*
 private static final DataParameter<Integer> RED = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> GREEN = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> BLUE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	
	private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);

	private static final DataParameter<Float> INITSCALE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	
	private static final DataParameter<Float> INITALPHA = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ALPHA = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> PREVANGLE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.FLOAT);
	
	private static final DataParameter<Integer> AGE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> MAXAGE = EntityDataManager.createKey(EntityAnimaStream.class, DataSerializers.VARINT);
 */
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(RED, 0);
		dataManager.register(GREEN, 0);
		dataManager.register(BLUE, 0);
		
		dataManager.register(GRAVITY, 0f);
		
		dataManager.register(INITSCALE, 0f);
		dataManager.register(SCALE, 0f);
		
		dataManager.register(INITALPHA, 0f);
		dataManager.register(ALPHA, 0f);
		
		dataManager.register(ANGLE, 0f);
		dataManager.register(PREVANGLE, 0f);
		
		dataManager.register(AGE, 0);
		dataManager.register(MAXAGE, 0);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		dataManager.set(GRAVITY,compound.getFloat("gravity"));

		dataManager.set(AGE, compound.getInteger("age"));
		dataManager.set(MAXAGE, compound.getInteger("maxAge"));

		dataManager.set(RED,compound.getInteger("colorR"));
		dataManager.set(GREEN,compound.getInteger("colorG"));
		dataManager.set(BLUE,compound.getInteger("colorB"));
		
		dataManager.set(INITSCALE,compound.getFloat("initScale"));
		dataManager.set(SCALE, compound.getFloat("scale"));

		dataManager.set(INITALPHA,compound.getFloat("initAlpha"));
		dataManager.set(ALPHA,compound.getFloat("alpha"));

		dataManager.set(ANGLE,compound.getFloat("angle"));
		dataManager.set(PREVANGLE,compound.getFloat("prevAngle"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setFloat("gravity", (float)dataManager.get(GRAVITY));

		compound.setInteger("age", (int)dataManager.get(AGE));
		compound.setInteger("maxAge", (int)dataManager.get(MAXAGE));

		compound.setInteger("colorR", (int)dataManager.get(RED));
		compound.setInteger("colorG", (int)dataManager.get(GREEN));
		compound.setInteger("colorB", (int)dataManager.get(BLUE));
		
		compound.setFloat("initScale", (float)dataManager.get(INITSCALE));
		compound.setFloat("scale", (float)dataManager.get(SCALE));

		compound.setFloat("initAlpha", (float)dataManager.get(INITALPHA));
		compound.setFloat("alpha", (float)dataManager.get(ALPHA));

		compound.setFloat("angle", (float)dataManager.get(ANGLE));
		compound.setFloat("prevAngle", (float)dataManager.get(PREVANGLE));
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		// TODO Auto-generated method stub

	}
}
