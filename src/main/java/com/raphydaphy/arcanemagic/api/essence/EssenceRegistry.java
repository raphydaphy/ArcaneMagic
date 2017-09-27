package com.raphydaphy.arcanemagic.api.essence;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.raphydaphy.arcanemagic.ArcaneMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class EssenceRegistry implements IForgeRegistry<Essence>
{

	private final BiMap<ResourceLocation, Essence> essences = HashBiMap.create();

	protected EssenceRegistry()
	{
	}

	@Override
	public Iterator<Essence> iterator()
	{
		return essences.values().iterator();
	}

	@Override
	public Class<Essence> getRegistrySuperType()
	{
		return Essence.class;
	}

	@Override
	public void register(Essence value)
	{
		Preconditions.checkNotNull(value, "Can't add null-object to the registry!");
		ResourceLocation key = value.getRegistryName();
		Preconditions.checkNotNull(key, "Can't use a null-name for the registry, object %s.", value);

		Essence oldEntry = essences.get(key);

		if (oldEntry == value)
		{
			ArcaneMagic.LOGGER.error("Essence Registry: The object {} has been registered twice for the same name {}.",
					value, key);
			return;
		} else if (oldEntry != null)
		{
			ArcaneMagic.LOGGER.info("Overriding entry {} in the Essence Registry with {}, using name {}", oldEntry,
					value, key);
		}
		essences.put(key, value);
	}

	@Override
	public void registerAll(Essence... values)
	{
		for (Essence e : values)
			register(e);
	}

	@Override
	public boolean containsKey(ResourceLocation key)
	{
		return essences.containsKey(key);
	}

	@Override
	public boolean containsValue(Essence value)
	{
		return essences.containsValue(value);
	}

	@Override
	public Essence getValue(ResourceLocation key)
	{
		return essences.get(key);
	}

	@Override
	public ResourceLocation getKey(Essence value)
	{
		return essences.inverse().get(value);
	}

	@Override
	public Set<ResourceLocation> getKeys()
	{
		return essences.keySet();
	}

	@Override
	public List<Essence> getValues()
	{
		return ImmutableList.copyOf(essences.values());
	}

	@Override
	public Set<Entry<ResourceLocation, Essence>> getEntries()
	{
		return essences.entrySet();
	}

	@Override
	public <T> T getSlaveMap(ResourceLocation slaveMapName, Class<T> type)
	{
		throw new IllegalArgumentException("Slave maps are not implemented for the Essence Registry");
	}
}
