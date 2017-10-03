package com.raphydaphy.arcanemagic.entity;

import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class EntityItemFancy extends EntityItem
{
	private int freezeTime = 0;

	public EntityItemFancy(World worldIn)
	{
		super(worldIn);

		if (world.getBlockState(this.getPosition()).getBlock() == Blocks.AIR)
		{
			world.setBlockState(this.getPosition(), ModRegistry.FANCY_LIGHT.getDefaultState());
		}
	}

	public EntityItemFancy(World worldIn, double x, double y, double z, ItemStack stack)
	{
		super(worldIn, x, y, z, stack);

		if (world.getBlockState(this.getPosition()).getBlock() == Blocks.AIR)
		{
			world.setBlockState(this.getPosition(), ModRegistry.FANCY_LIGHT.getDefaultState());
		}
	}

	public EntityItemFancy(World worldIn, double x, double y, double z, ItemStack stack, int freezeTime)
	{
		this(worldIn, x, y, z, stack);
		this.freezeTime = freezeTime;

	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (freezeTime > 0)
		{
			this.freezeTime--;
			
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
		}
		if (this.isDead)
		{
			if (world.getBlockState(this.getPosition()).getBlock() == ModRegistry.FANCY_LIGHT)
			{
				world.setBlockToAir(getPosition());
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("freezeTime", freezeTime);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.freezeTime = compound.getInteger("freezeTime");
	}

	@Override
	public void setDead()
	{
		super.setDead();

		if (world.getBlockState(this.getPosition()).getBlock() == ModRegistry.FANCY_LIGHT)
		{
			world.setBlockToAir(getPosition());
		}
	}

	@Override
	public void move(MoverType type, double x, double y, double z)
	{
		if (this.freezeTime <= 0)
		{
			Vec3i oldPos = new Vec3i((int) Math.floor(this.getPosition().getX()),
					(int) Math.floor(this.getPosition().getY()), (int) Math.floor(this.getPosition().getZ()));
			super.move(type, x, y, z);
			Vec3i newPos = new Vec3i((int) Math.floor(this.getPosition().getX()),
					(int) Math.floor(this.getPosition().getY()), (int) Math.floor(this.getPosition().getZ()));
			if (!oldPos.equals(newPos))
			{
				if (world.getBlockState(new BlockPos(oldPos)).getBlock() == ModRegistry.FANCY_LIGHT)
				{
					world.setBlockToAir(new BlockPos(oldPos));
				}
	
				if (world.getBlockState(new BlockPos(newPos)).getBlock() == Blocks.AIR)
				{
					world.setBlockState(new BlockPos(newPos), ModRegistry.FANCY_LIGHT.getDefaultState());
				}
			}
		}
		else
		{
		}
	}
}