package com.raphydaphy.arcanemagic.handler;

import java.util.Random;

import com.raphydaphy.arcanemagic.ArcaneMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public final class ArcaneMagicSoundHandler
{
	private static Random rand = new Random();

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":craft_start")
	public static SoundEvent craft_start;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":scepter_1")
	public static SoundEvent scepter_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":scepter_2")
	public static SoundEvent scepter_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":scepter_3")
	public static SoundEvent scepter_3;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":page_1")
	public static SoundEvent page_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":page_2")
	public static SoundEvent page_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":creak_1")
	public static SoundEvent creak_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":creak_2")
	public static SoundEvent creak_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":tool_1")
	public static SoundEvent tool_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":tool_2")
	public static SoundEvent tool_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":fly_1")
	public static SoundEvent fly_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":fly_2")
	public static SoundEvent fly_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":gore_1")
	public static SoundEvent gore_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":gore_2")
	public static SoundEvent gore_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":wind_1")
	public static SoundEvent wind_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":wind_2")
	public static SoundEvent wind_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":write_1")
	public static SoundEvent write_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":write_2")
	public static SoundEvent write_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":shock_1")
	public static SoundEvent shock_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":shock_2")
	public static SoundEvent shock_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":swing_1")
	public static SoundEvent swing_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":swing_2")
	public static SoundEvent swing_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":squeek_1")
	public static SoundEvent squeek_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":squeek_2")
	public static SoundEvent squeek_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":zap_1")
	public static SoundEvent zap_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":zap_2")
	public static SoundEvent zap_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":hh_on")
	public static SoundEvent hh_on;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":hh_off")
	public static SoundEvent hh_off;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":learn")
	public static SoundEvent learn;
	
	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":infuser_start")
	public static SoundEvent infuser_start;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":infuser")
	public static SoundEvent infuser;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":camera_clack_1")
	public static SoundEvent camera_clack_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":camera_clack_2")
	public static SoundEvent camera_clack_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":camera_clack_3")
	public static SoundEvent camera_clack_3;

	public static SoundEvent register(String name, IForgeRegistry<SoundEvent> registry)
	{
		ResourceLocation loc = new ResourceLocation(ArcaneMagic.MODID + ":" + name);
		SoundEvent e = new SoundEvent(loc).setRegistryName(name);
		registry.register(e);
		return e;
	}

	public static SoundEvent randomScepterSound()
	{
		int sound = rand.nextInt(3) + 1;
		switch (sound)
		{
		case 1:
			return scepter_1;
		case 2:
			return scepter_2;
		case 3:
			return scepter_3;
		}

		return scepter_1;
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