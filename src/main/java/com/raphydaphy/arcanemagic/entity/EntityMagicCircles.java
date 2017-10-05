package com.raphydaphy.arcanemagic.entity;

import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMagicCircles extends Entity
{
	public double constantRot = 0;
	public double edgeRot = 0;
	public Vec3d circlePos;
	public boolean hasBook = true;

	public EntityMagicCircles(World worldIn)
	{
		super(worldIn);
		circlePos = new Vec3d(this.posX + 0.5, this.posY, this.posZ + 0.5);
		this.setSize(1, 1);
	}

	public EntityMagicCircles(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		this.setSize(1, 1);
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		circlePos = new Vec3d(this.posX + 0.5, this.posY, this.posZ + 0.5);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);

		if (!this.world.isRemote)
		{
			ItemStack stack = player.getHeldItem(hand);

			System.out.println("interaction");
			if (stack.getItem().equals(Items.BOOK))
			{
				this.hasBook = true;
				stack.shrink(1);

				return true;
			}
		}
		return false;
	}

	public SoundCategory getSoundCategory()
	{
		return SoundCategory.BLOCKS;
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
			this.playSound(ArcaneMagicSoundHandler.infuser, 1f, 1f);
		}
		this.constantRot += 1;

		//System.out.println(world.isRemote);
		if (constantRot >= 360)
		{
			if (edgeRot < 90)
			{
				this.edgeRot += 0.5;
			}
		}

		if (this.ticksExisted >= 800)
		{
			if (!world.isRemote)
			{
				world.spawnEntity(
						new EntityItemFancy(world, circlePos.x, circlePos.y + 0.9 + (edgeRot == 0 ? 0 : (edgeRot / 90)),
								circlePos.z, new ItemStack(ModRegistry.NOTEBOOK)));

			}
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
		compound.setBoolean("hasBook", this.hasBook);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		constantRot = compound.getDouble("constantRot");
		edgeRot = compound.getDouble("edgeRot");
		circlePos = new Vec3d(compound.getDouble("circleX"), compound.getDouble("circleY"),
				compound.getDouble("circleX"));
		hasBook = compound.getBoolean("hasBook");
	}

	@Override
	protected void entityInit()
	{

	}
}