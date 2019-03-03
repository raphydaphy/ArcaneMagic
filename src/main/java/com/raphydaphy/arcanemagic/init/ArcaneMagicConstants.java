package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.util.Identifier;

public class ArcaneMagicConstants
{
	public static final int SOUL_METER_STAGES = 88;
	public static final String SOUL_KEY = "soul_stored";
	public static final int SOUL_PENDANT_MAX_SOUL = 150;
	public static final int SOUL_METER_MAX = SOUL_PENDANT_MAX_SOUL + 50;

	public static final String PARCHMENT_TYPE_KEY = "key.arcanemagic.parchment_type";

	public static final Identifier GLOW_PARTICLE_TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "particle/glow_particle");
	public static final Identifier SMOKE_PARTICLE_TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "particle/smoke_particle");

	public static final String PASSIVE_CRYSTAL_KEY = "PassiveCrystal";
	public static final String ACTIVE_CRYSTAL_KEY = "ActiveCrystal";
	public static final String ACTIVE_TIMER_KEY = "ActiveTimer";
}
