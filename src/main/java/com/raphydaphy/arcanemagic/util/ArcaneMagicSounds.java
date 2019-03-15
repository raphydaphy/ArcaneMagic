package com.raphydaphy.arcanemagic.util;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ArcaneMagicSounds
{
	public static SoundEvent TRANSFIGURATION = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "transfiguration")));
	public static SoundEvent SLIDE = register(new SoundEvent(new Identifier(ArcaneMagic.DOMAIN, "slide")));

	private static SoundEvent register(SoundEvent event)
	{
		return Registry.register(Registry.SOUND_EVENT, event.getId(), event);
	}
}
