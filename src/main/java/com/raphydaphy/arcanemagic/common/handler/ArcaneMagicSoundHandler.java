package com.raphydaphy.arcanemagic.common.handler;

import java.util.Random;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(ArcaneMagic.MODID)
public final class ArcaneMagicSoundHandler
{
	private static Random rand = new Random();
	
	public static final SoundEvent spell = null;
	
	public static final SoundEvent scepter_1 = null;
	public static final SoundEvent scepter_2 = null;
	public static final SoundEvent scepter_3 = null;

	public static final SoundEvent page_1 = null;
	public static final SoundEvent page_2 = null;

	public static final SoundEvent elemental_crafting_success = null;

	public static final SoundEvent write_1 = null;
	public static final SoundEvent write_2 = null;

	public static final SoundEvent learn_1 = null;
	public static final SoundEvent learn_2 = null;

	public static final SoundEvent reconstruct = null;

	public static final SoundEvent clack = null;

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
	
	public static SoundEvent randomLearnSound()
	{
		return rand.nextInt(2) + 1 == 1 ? learn_1 : learn_2;
	}

	public static SoundEvent randomWriteSound()
	{
		return rand.nextInt(2) + 1 == 1 ? write_1 : write_2;
	}

}