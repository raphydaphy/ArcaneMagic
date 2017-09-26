package com.raphydaphy.arcanemagic.handler;

import java.util.Random;

import com.raphydaphy.arcanemagic.Thaumcraft;

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

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":creak_1")
	public static SoundEvent creak_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":creak_2")
	public static SoundEvent creak_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":tool_1")
	public static SoundEvent tool_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":tool_2")
	public static SoundEvent tool_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":fly_1")
	public static SoundEvent fly_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":fly_2")
	public static SoundEvent fly_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":gore_1")
	public static SoundEvent gore_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":gore_2")
	public static SoundEvent gore_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":wind_1")
	public static SoundEvent wind_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":wind_2")
	public static SoundEvent wind_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":write_1")
	public static SoundEvent write_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":write_2")
	public static SoundEvent write_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":shock_1")
	public static SoundEvent shock_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":shock_2")
	public static SoundEvent shock_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":swing_1")
	public static SoundEvent swing_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":swing_2")
	public static SoundEvent swing_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":squeek_1")
	public static SoundEvent squeek_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":squeek_2")
	public static SoundEvent squeek_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":zap_1")
	public static SoundEvent zap_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":zap_2")
	public static SoundEvent zap_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":hh_on")
	public static SoundEvent hh_on;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":hh_off")
	public static SoundEvent hh_off;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":camera_clack_1")
	public static SoundEvent camera_clack_1;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":camera_clack_2")
	public static SoundEvent camera_clack_2;

	@GameRegistry.ObjectHolder(Thaumcraft.MODID + ":camera_clack_3")
	public static SoundEvent camera_clack_3;

	public static SoundEvent register(String name, IForgeRegistry<SoundEvent> registry)
	{
		ResourceLocation loc = new ResourceLocation(Thaumcraft.MODID + ":" + name);
		SoundEvent e = new SoundEvent(loc).setRegistryName(name);
		registry.register(e);
		return e;
	}

	public static SoundEvent randomWandSound()
	{
		int sound = rand.nextInt(3) + 1;
		switch (sound)
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

	public static SoundEvent randomCreakSound()
	{
		return rand.nextInt(2) + 1 == 1 ? creak_1 : creak_2;
	}

	public static SoundEvent randomToolSound()
	{
		return rand.nextInt(2) + 1 == 1 ? tool_1 : tool_2;
	}

	public static SoundEvent randomFlySound()
	{
		return rand.nextInt(2) + 1 == 1 ? fly_1 : fly_2;
	}

	public static SoundEvent randomGoreSound()
	{
		return rand.nextInt(2) + 1 == 1 ? gore_1 : gore_2;
	}

	public static SoundEvent randomWriteSound()
	{
		return rand.nextInt(2) + 1 == 1 ? write_1 : write_2;
	}

	public static SoundEvent randomWindSound()
	{
		return rand.nextInt(2) + 1 == 1 ? wind_1 : wind_2;
	}

	public static SoundEvent randomShockSound()
	{
		return rand.nextInt(2) + 1 == 1 ? shock_1 : shock_2;
	}

	public static SoundEvent randomSwingSound()
	{
		return rand.nextInt(2) + 1 == 1 ? swing_1 : swing_2;
	}

	public static SoundEvent randomSqueekSound()
	{
		return rand.nextInt(2) + 1 == 1 ? squeek_1 : squeek_2;
	}

	public static SoundEvent randomZapSound()
	{
		return rand.nextInt(2) + 1 == 1 ? zap_1 : zap_2;
	}

	public static SoundEvent randomCameraClackSound()
	{
		int sound = rand.nextInt(3) + 1;
		switch (sound)
		{
		case 1:
			return camera_clack_1;
		case 2:
			return camera_clack_2;
		case 3:
			return camera_clack_3;
		}

		return camera_clack_1;
	}

}