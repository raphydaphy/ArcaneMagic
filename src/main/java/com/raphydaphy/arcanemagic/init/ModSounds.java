package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static SoundEvent TRANSFIGURATION;
    public static SoundEvent SLIDE;
    public static SoundEvent BURN;
    public static SoundEvent SPELL;
    public static SoundEvent DECONSTRUCT;
    public static SoundEvent VOID_GROWL;
    public static SoundEvent VOID_ATMOSPHERE;
    public static SoundEvent VOID_AMBIENT;
    public static SoundEvent CUTSCENE_START;

    public static void init() {
        TRANSFIGURATION = register("transfiguration");
        SLIDE = register("slide");
        BURN = register("burn");
        SPELL = register("spell");
        DECONSTRUCT = register("deconstruct");
        VOID_GROWL = register("void_growl");
        VOID_ATMOSPHERE = register("void_atmosphere");
        VOID_AMBIENT = register("void_ambient");
        CUTSCENE_START = register("cutscene_start");
    }

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(ArcaneMagic.DOMAIN, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }
}
