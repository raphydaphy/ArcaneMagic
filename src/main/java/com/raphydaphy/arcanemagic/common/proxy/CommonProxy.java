package com.raphydaphy.arcanemagic.common.proxy;

import java.awt.Color;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.item.ItemIlluminator;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void spawnAnimaParticles(World world, Vec3d pos, Vec3d speed, Anima anima, Vec3d travelPos,
			boolean isCosmetic) {

	}

	public void addIlluminatorParticle(ItemIlluminator item, World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ) {

	}

	public void sendAnimaSafe(AnimaStack anima, Vec3d from, Vec3d to, Vec3d toCosmetic, boolean spawnParticles) {

	}

	public void addCategoryUnlockToast(NotebookCategory category, boolean expanded) {

	}

	public abstract String translate(String key, Object... args);
	
	public void magicParticle(Color color, BlockPos from, BlockPos to)
	{
		
	}
	
	public void animaParticle(World worldIn, double x, double y, double z, float r,
			float g, float b, float a, float scale)
	{
		
	}
}
