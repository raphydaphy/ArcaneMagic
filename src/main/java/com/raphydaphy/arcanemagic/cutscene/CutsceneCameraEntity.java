package com.raphydaphy.arcanemagic.cutscene;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class CutsceneCameraEntity extends Entity
{
	public CutsceneCameraEntity(World world)
	{
		super(ModRegistry.CUTSCENE_CAMERA_ENTITY, world);
	}

	void setPos(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}

	void update()
	{
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.x += getVelocity().x;
		this.y += getVelocity().y;
		this.z += getVelocity().z;
	}

	@Override
	protected void initDataTracker()
	{
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag var1)
	{
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag var1)
	{
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return null;
	}
}
