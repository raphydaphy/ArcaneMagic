package com.raphydaphy.arcanemagic.api.anima;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class AnimaRegistry implements IForgeRegistry<Anima> {

	private final BiMap<ResourceLocation, Anima> animus = HashBiMap.create();

	protected AnimaRegistry() {
	}

	@Override
	public Iterator<Anima> iterator() {
		return animus.values().iterator();
	}

	@Override
	public Class<Anima> getRegistrySuperType() {
		return Anima.class;
	}

	@Override
	public void register(Anima value) {
		Preconditions.checkNotNull(value, "Can't add null-object to the registry!");
		ResourceLocation key = value.getRegistryName();
		Preconditions.checkNotNull(key, "Can't use a null-name for the registry, object %s.", value);

		Anima oldEntry = animus.get(key);

		if (oldEntry == value) {
			ArcaneMagic.LOGGER.error("Anima Registry: The object {} has been registered twice for the same name {}.",
					value, key);
			return;
		} else if (oldEntry != null) {
			ArcaneMagic.LOGGER.info("Overriding entry {} in the Anima Registry with {}, using name {}", oldEntry, value,
					key);
		}
		animus.put(key, value);
	}

	@Override
	public void registerAll(Anima... values) {
		for (Anima e : values)
			register(e);
	}

	@Override
	public boolean containsKey(ResourceLocation key) {
		return animus.containsKey(key);
	}

	@Override
	public boolean containsValue(Anima value) {
		return animus.containsValue(value);
	}

	@Override
	public Anima getValue(ResourceLocation key) {
		return animus.get(key);
	}

	@Override
	public ResourceLocation getKey(Anima value) {
		return animus.inverse().get(value);
	}

	@Override
	public Set<ResourceLocation> getKeys() {
		return animus.keySet();
	}

	@Override
	public List<Anima> getValues() {
		return ImmutableList.copyOf(animus.values());
	}

	@Override
	public Set<Entry<ResourceLocation, Anima>> getEntries() {
		return animus.entrySet();
	}

	@Override
	public <T> T getSlaveMap(ResourceLocation slaveMapName, Class<T> type) {
		throw new IllegalArgumentException("Slave maps are not implemented for the Anima Registry");
	}
}
