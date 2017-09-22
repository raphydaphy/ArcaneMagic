package com.raphydaphy.thaumcraft.handler;

import java.util.Random;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public final class ThaumcraftSoundHandler 
{
	private static Random rand = new Random();
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":craft_start")
	public static SoundEvent craft_start;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":wand_1")
	public static SoundEvent wand_1;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":wand_2")
	public static SoundEvent wand_2;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":wand_3")
	public static SoundEvent wand_3;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":page_1")
	public static SoundEvent page_1;
	
	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":page_2")
	public static SoundEvent page_2;
	
	public static SoundEvent register(String name, IForgeRegistry<SoundEvent> registry) {
		ResourceLocation loc = new ResourceLocation(Thaumcraft.MODID + ":" + name);
		SoundEvent e = new SoundEvent(loc).setRegistryName(name);
		registry.register(e);
		return e;
	}
	
	public static SoundEvent randomWandSound()
	{
		int sound = rand.nextInt(3) + 1;
		switch(sound)
		{
		case 1:
			return wand_1;
		case 2:
			return wand_2;
		case 3:
			return wand_3;
		}
		
		return wand_1;
	}
	
	public static SoundEvent randomPageSound()
	{
		return rand.nextInt(2) + 1 == 1 ? page_1 : page_2;
	}
	
}