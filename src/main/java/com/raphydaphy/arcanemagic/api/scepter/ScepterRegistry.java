package com.raphydaphy.arcanemagic.api.scepter;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.scepter.ScepterCap;
import com.raphydaphy.arcanemagic.scepter.ScepterCore;

import net.minecraft.util.ResourceLocation;

public class ScepterRegistry
{

	private static final BiMap<ResourceLocation, ScepterPart> REGISTRY = HashBiMap.create();
	private static boolean isDone = false;

	public static void registerDefaults()
	{
		if (!isDone)
			registerAll(ScepterCap.IRON, ScepterCap.GOLD, ScepterCap.THAUMIUM, ScepterCore.WOOD, ScepterCore.GREATWOOD,
					ScepterCore.SILVERWOOD);
		isDone = true;
	}

	public static boolean register(ScepterPart part)
	{

		Preconditions.checkNotNull(part, "Can't add null-object to the registry!");
		ResourceLocation key = part.getRegistryName();
		Preconditions.checkNotNull(key, "Can't use a null-name for the registry, object %s.", part);

		ScepterPart oldEntry = REGISTRY.get(key);

		if (oldEntry == part)
		{
			ArcaneMagic.LOGGER.error("Scepter Registry: The object {} has been registered twice for the same name {}.",
					part, key);
			return false;
		} else if (oldEntry != null)
		{
			ArcaneMagic.LOGGER.error(
					"Attempting to register {} in the Scepter Registry, but it already existed! Old part: {}, using name {}",
					part, oldEntry, key);
			return false;
		}
		REGISTRY.put(key, part);
		return true;
	}

	public static void registerAll(ScepterPart... parts)
	{
		for (ScepterPart p : parts)
			register(p);
	}

	public static ScepterPart getPart(ResourceLocation name)
	{
		return REGISTRY.get(name);
	}
}
