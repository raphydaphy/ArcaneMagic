package com.raphydaphy.arcanemagic.api.notebook;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class CategoryRegistry implements IForgeRegistry<NotebookCategory>
{

	private static boolean frozen = false;

	private final BiMap<ResourceLocation, NotebookCategory> categories = HashBiMap.create();

	protected CategoryRegistry()
	{
	}

	@Override
	public Iterator<NotebookCategory> iterator()
	{
		return categories.values().iterator();
	}

	@Override
	public Class<NotebookCategory> getRegistrySuperType()
	{
		return NotebookCategory.class;
	}

	@Override
	public void register(NotebookCategory value)
	{
		Preconditions.checkArgument(!frozen, "The category registry is frozen.  Register earlier.");
		Preconditions.checkNotNull(value, "Can't add null-object to the registry!");
		ResourceLocation key = value.getRegistryName();
		Preconditions.checkNotNull(key, "Can't use a null-name for the registry, object %s.", value);

		NotebookCategory oldEntry = categories.get(key);

		if (oldEntry == value)
		{
			ArcaneMagic.LOGGER.error("Category Registry: The object {} has been registered twice for the same name {}.",
					value, key);
			return;
		} else if (oldEntry != null)
		{
			ArcaneMagic.LOGGER.info("Overriding entry {} in the Essence Registry with {}, using name {}", oldEntry,
					value, key);
		}
		categories.put(key, value);
	}

	@Override
	public void registerAll(NotebookCategory... values)
	{
		for (NotebookCategory e : values)
			register(e);
	}

	@Override
	public boolean containsKey(ResourceLocation key)
	{
		return categories.containsKey(key);
	}

	@Override
	public boolean containsValue(NotebookCategory value)
	{
		return categories.containsValue(value);
	}

	@Override
	public NotebookCategory getValue(ResourceLocation key)
	{
		return categories.get(key);
	}

	@Override
	public ResourceLocation getKey(NotebookCategory value)
	{
		return categories.inverse().get(value);
	}

	@Override
	public Set<ResourceLocation> getKeys()
	{
		return categories.keySet();
	}

	@Override
	public List<NotebookCategory> getValues()
	{
		return ImmutableList.copyOf(categories.values());
	}

	@Override
	public Set<Entry<ResourceLocation, NotebookCategory>> getEntries()
	{
		return ImmutableSet.copyOf(categories.entrySet());
	}

	@Override
	public <T> T getSlaveMap(ResourceLocation slaveMapName, Class<T> type)
	{
		throw new IllegalArgumentException("Slave maps are not implemented for the Category Registry");
	}

	private static void freeze()
	{
		ArcaneMagic.LOGGER
				.info("Freezing Category Registry - being called from " + Thread.currentThread().getStackTrace()[0]);
		frozen = true;
	}

	public static void sortCategories()
	{
		List<NotebookCategory> ours = NotebookCategory.REGISTRY.getValues().stream()
				.filter((c) -> c.getRegistryName().getResourceDomain().equals(ArcaneMagic.MODID))
				.collect(Collectors.toList());
		List<ResourceLocation> theirNames = new ArrayList<>();
		NotebookCategory.REGISTRY.getValues().stream()
				.filter((c) -> !c.getRegistryName().getResourceDomain().equals(ArcaneMagic.MODID))
				.sorted(Collator.getInstance()).forEach((c) -> theirNames.add(c.getRegistryName()));
		theirNames.sort(Collator.getInstance());
		ImmutableList.Builder<NotebookCategory> bob = ImmutableList.builder();
		bob.addAll(ours);
		for (ResourceLocation loc : theirNames)
			bob.add(NotebookCategory.REGISTRY.getValue(loc));
		ArcaneMagicAPI.setCategoryList(bob.build());
		freeze();
	}
}
