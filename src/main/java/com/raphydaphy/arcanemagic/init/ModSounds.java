package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds
{
	public static SoundEvent TRANSFIGURATION = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "transfiguration")));
	public static SoundEvent SLIDE = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "slide")));
	public static SoundEvent BURN = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "burn")));
	public static SoundEvent SPELL = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "spell")));
	public static SoundEvent DECONSTRUCT = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "deconstruct")));
	public static SoundEvent VOID_GROWL = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "void_growl")));
	public static SoundEvent VOID_ATMOSPHERE = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "void_atmosphere")));
	public static SoundEvent VOID_AMBIENT = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "void_ambient")));

	private static SoundEvent register(SoundEvent event)
	{
		return Registry.register(Registry.SOUND_EVENT, event.getId(), event);
	}
}
