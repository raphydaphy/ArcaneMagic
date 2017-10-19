package com.raphydaphy.arcanemagic.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.client.particle.ParticlePos;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIlluminator extends ItemBase
{
	private Map<ParticlePos, Integer> particles = new HashMap<ParticlePos, Integer>();

	public ItemIlluminator()
	{
		super("mystical_illuminator", TextFormatting.GRAY);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (isSelected && world.isRemote)
		{
			Map<ParticlePos, Integer> newParticles = new HashMap<ParticlePos, Integer>();
			for (ParticlePos p : particles.keySet())
			{

				if (particles.get(p) > 0)
				{

					doParticle(world, p);
					newParticles.put(p, particles.get(p) - 1);
				}
			}
			particles = newParticles;
		}
	}

	@SideOnly(Side.CLIENT)
	private void doParticle(World world, ParticlePos ppos)
	{
		if (world.rand.nextInt(3) == 1)
		{
			BlockPos pos = ppos.getPos();
			for (int x = 0; x < 1; x++)
			{
				switch (ppos.getFacing())
				{
				case UP:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getZ() + 0.4 + (world.rand.nextFloat() / 4), 0, 0.3, 0);
					break;
				case EAST:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() - (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							1, 0.5, 0);
					break;
				case WEST:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							-1, 0.5, 0);
					break;
				case SOUTH:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							0, 0.5, 1);
					break;
				case NORTH:
					world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 4),
							pos.getY() + (world.rand.nextFloat() / 4), pos.getZ() + 0.4 + (world.rand.nextFloat() / 4),
							0, 0.5, -1);
					break;
				case DOWN:
					break;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void addParticle(World world, BlockPos newPos, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			ParticlePos ppos1 = new ParticlePos(newPos, facing, hitX, hitY, hitZ);
			particles.put(ppos1, 50);
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
		{
			if (world.isRemote)
			{
				ItemStack asItem = new ItemStack(world.getBlockState(pos).getBlock());

				if (!asItem.isEmpty() && !asItem.getItem().equals(Items.AIR)
						&& ArcaneMagicAPI.getFromAnalysis(asItem, new ArrayList<>()).size() > 0)
				{
					ArcaneMagic.proxy.addIlluminatorParticle(this, world, pos, facing, hitX, hitY, hitZ);
				}
			}
			player.swingArm(hand);
		}
		return EnumActionResult.SUCCESS;
	}
}
