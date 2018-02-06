package com.raphydaphy.arcanemagic.client.particle;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticlePos
{
	private final BlockPos pos;
	private final EnumFacing facing;
	private final float hitX;
	private final float hitY;
	private final float hitZ;

	public ParticlePos(BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		this.pos = pos;
		this.facing = facing;
		this.hitX = hitX;
		this.hitY = hitY;
		this.hitZ = hitZ;
	}

	public BlockPos getPos()
	{
		return pos;
	}

	public EnumFacing getFacing()
	{
		return facing;
	}

	public float getHitX()
	{
		return hitX;
	}

	public float getHitY()
	{
		return hitY;
	}

	public float getHitZ()
	{
		return hitZ;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof ParticlePos)
		{
			ParticlePos pos2 = (ParticlePos) other;

			if (pos2.getPos().getX() == this.getPos().getX() && pos2.getPos().getY() == this.getPos().getY()
					&& pos2.getPos().getZ() == this.getPos().getZ())
			{
				if (pos2.getFacing().equals(this.getFacing()))
				{
					if (pos2.getHitX() == this.getHitX() && pos2.getHitY() == this.getHitY()
							&& pos2.getHitZ() == this.getHitZ())
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}