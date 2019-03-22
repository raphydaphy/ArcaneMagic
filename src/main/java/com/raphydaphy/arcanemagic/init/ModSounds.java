package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds
{
	public static SoundEvent TRANSFIGURATION = register("transfiguration");
	public static SoundEvent SLIDE = register("slide");
	public static SoundEvent BURN = register("burn");
	public static SoundEvent SPELL = register("spell");
	public static SoundEvent DECONSTRUCT = register("deconstruct");
	public static SoundEvent VOID_GROWL = register("void_growl");
	public static SoundEvent VOID_ATMOSPHERE = register("void_atmosphere");
	public static SoundEvent VOID_AMBIENT = register("void_ambient");

	private static SoundEvent register(String name)
	{
		Identifier id = new Identifier(ArcaneMagic.DOMAIN, name);
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
	}
}
