package com.raphydaphy.arcanemagic.api;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;

import net.minecraft.util.ResourceLocation;

public class ArcaneMagicAPI
{
	public static final String VERSION = "0.1";

	private static final BiMap<ResourceLocation, INotebookCategory> CATEGORIES = HashBiMap.create();
	private static int TOTAL_NOTEBOOK_CATEGORIES = 0;

	public static void registerCategory(INotebookCategory category)
	{
		Preconditions.checkNotNull(category, "Cannot register a null object!");
		Preconditions.checkNotNull(category.getRegistryName(), "Cannot register without a registry name!");
		Preconditions.checkArgument(CATEGORIES.get(category.getRegistryName()) == null, "Cannot use an already existing registry name!");
		TOTAL_NOTEBOOK_CATEGORIES++;
		CATEGORIES.put(category.getRegistryName(), category);
	}
	
	public static void registerEssence(Essence e) {
		Essence.REGISTRY.register(e);
	}

	public static int getCategoryCount()
	{
		return TOTAL_NOTEBOOK_CATEGORIES;
	}

	public static Set<INotebookCategory> getNotebookCategories()
	{
		return ImmutableSet.copyOf(CATEGORIES.values());
	}
	
	@Nullable
	public static INotebookCategory getCategory(ResourceLocation name) {
		return CATEGORIES.get(name);
	}

}
