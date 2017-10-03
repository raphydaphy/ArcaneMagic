package com.raphydaphy.arcanemagic.entity;

import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMagicCircles extends Entity
{
	public double constantRot = 0;
	public double edgeRot = 0;
	public Vec3d circlePos;
	
	public EntityMagicCircles(World worldIn)
	{
		super(worldIn);
		
		circlePos = new Vec3d(this.posX + 0.5, this.posY, this.posZ + 0.5);
	}

	public EntityMagicCircles(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		
		circlePos = new Vec3d(this.posX + 0.5, this.posY, this.posZ + 0.5);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		
		if (rand.nextInt(30) == 1)
		{
			world.playSound(posX, posY, posZ, ArcaneMagicSoundHandler.infuser,
					SoundCategory.MASTER, 1f, 1f, false);
		}
		this.constantRot += 1;
		
		System.out.println("X: " + this.posX + " Y : " + this.posY + " Z: " + this.posZ);
		//System.out.println(world.isRemote);
		if (constantRot >= 360)
		{
			if (edgeRot < 90)
			{
				this.edgeRot += 0.5;
				if (edgeRot == 89)
				{
					
					if (!world.isRemote)
					{
						world.spawnEntity(new EntityItemFancy(world, circlePos.x, circlePos.y + 0.9 + (edgeRot == 0 ? 0 : (edgeRot / 90)),circlePos.z, new ItemStack(ModRegistry.NOTEBOOK), 270));
				
					}
				}
			}
		}

		if (this.ticksExisted >= 800)
		{
			this.setDead();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setDouble("constantRot", constantRot);
		compound.setDouble("edgeRot", edgeRot);
		compound.setDouble("circleX", circlePos.x);
		compound.setDouble("circleY", circlePos.y);
		compound.setDouble("circleZ", circlePos.z);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		constantRot = compound.getDouble("constantRot");
		edgeRot = compound.getDouble("edgeRot");
		circlePos = new Vec3d(compound.getDouble("circleX"), compound.getDouble("circleY"), compound.getDouble("circleX"));
	}

	@Override
	protected void entityInit()
	{
		
	}
}