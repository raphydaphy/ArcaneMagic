package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemAncientParchment extends ItemBase
{
	public ItemAncientParchment(String name)
	{
		super(name);
		setMaxStackSize(1);
	}

	public boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem)
	{
		super.onEntityItemUpdate(entityItem);

		World world = entityItem.world;

		for (int x = (int) entityItem.posX - 5; x < (int) entityItem.posX + 5; x++)
		{
			for (int y = (int) entityItem.posY - 5; y < (int) entityItem.posY + 5; y++)
			{
				for (int z = (int) entityItem.posZ - 5; z < (int) entityItem.posZ + 5; z++)
				{
					BlockPos here = new BlockPos(x, y, z);
					if (world.getBlockState(here).getBlock().equals(Blocks.BEDROCK))
					{
						if (itemRand.nextInt(2000) == 1)
						{
							// just for looks..
							ArcaneMagic.proxy.spawnEssenceParticles(world, new Vec3d(x + 0.5, y + 0.5, z + 0.5),
									new Vec3d(0, 0, 0), Essence.getFromBiome(world.getBiome(here)),
									entityItem.getPositionVector().addVector(0, 0.4, 0), true);
						}
					}
				}
			}
		}
		return false;
	}

}
