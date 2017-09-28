package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.client.particle.ParticleStar;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
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

		for (int x = (int) entityItem.posX - 10; x < (int) entityItem.posX + 10; x++)
		{
			for (int y = (int) entityItem.posY - 5; y < (int) entityItem.posY + 5; y++)
			{
				for (int z = (int) entityItem.posZ - 10; z < (int) entityItem.posZ + 10; z++)
				{
					if (itemRand.nextInt(50) == 1)
					{
						BlockPos here = new BlockPos(x, y, z);
						if (world.getBlockState(here).getBlock().equals(Blocks.BEDROCK))
						{
							Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleStar(
									world, x + 0.5, y + 0.5, z + 0.5, 0, 0, 0, 86, 13, 124,entityItem.getPositionVector().addVector(0, 0.4, 0)));
						}
					}
				}
			}
		}
		return false;
	}

}
