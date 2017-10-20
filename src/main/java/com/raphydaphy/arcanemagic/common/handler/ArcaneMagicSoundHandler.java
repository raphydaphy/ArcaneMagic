package com.raphydaphy.arcanemagic.common.handler;

import java.util.Random;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public final class ArcaneMagicSoundHandler
{
	private static Random rand = new Random();

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":spell")
	public static SoundEvent spell;

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

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":elemental_crafting_success")
	public static SoundEvent elemental_crafting_success;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":write_1")
	public static SoundEvent write_1;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":write_2")
	public static SoundEvent write_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":learn_1")
	public static SoundEvent learn_1;
	
	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":learn_2")
	public static SoundEvent learn_2;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":reconstruct")
	public static SoundEvent reconstruct;

	@GameRegistry.ObjectHolder(ArcaneMagic.MODID + ":clack")
	public static SoundEvent clack;

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