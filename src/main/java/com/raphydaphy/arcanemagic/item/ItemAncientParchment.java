package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.client.particle.ParticleEssence;

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
		
		if (world.isRemote)
		{
			for (int x = (int) entityItem.posX - 5; x < (int) entityItem.posX + 5; x++)
			{
				for (int y = (int) entityItem.posY - 5; y < (int) entityItem.posY + 5; y++)
				{
					for (int z = (int) entityItem.posZ - 5; z < (int) entityItem.posZ + 5; z++)
					{
						if (itemRand.nextInt(300) == 1)
						{
							BlockPos here = new BlockPos(x, y, z);
							if (world.getBlockState(here).getBlock().equals(Blocks.BEDROCK))
							{
								Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleEssence(
										world, x + 0.5, y + 0.5, z + 0.5, 0, 0, 0, Essence.getFromBiome(world.getBiome(here)).getColorHex(),entityItem.getPositionVector().addVector(0, 0.4, 0)));
							}
						}
					}
				}
			}
		}
		return false;
	}

}
