package com.raphydaphy.arcanemagic.common.notebook;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryAncientRelics;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryBasicLinguistics;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryCrystallization;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryEssenceCollection;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryForgottenKnowledge;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryManipulatingMagic;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryMysticalEnergy;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryNaturalHarmony;

public class NotebookCategories
{
	public static final NotebookCategory ANCIENT_RELICS = new CategoryAncientRelics().setRegistryName(ArcaneMagic.MODID,
			"ancient_relics");
	public static final NotebookCategory FORGOTTEN_KNOWLEDGE = new CategoryForgottenKnowledge()
			.setRegistryName(ArcaneMagic.MODID, "basic_linguistics");
	public static final NotebookCategory BASIC_LINGUISTICS = new CategoryBasicLinguistics()
			.setRegistryName(ArcaneMagic.MODID, "elemental_particles");
	public static final NotebookCategory MYSTICAL_ENERGY = new CategoryMysticalEnergy()
			.setRegistryName(ArcaneMagic.MODID, "mystical_energy");
	public static final NotebookCategory ESSENCE_COLLECTION = new CategoryEssenceCollection()
			.setRegistryName(ArcaneMagic.MODID, "essence_collection");
	public static final NotebookCategory CRYSTALLIZATION = new CategoryCrystallization()
			.setRegistryName(ArcaneMagic.MODID, "crystallization");
	public static final NotebookCategory NATURAL_HARMONY = new CategoryNaturalHarmony()
			.setRegistryName(ArcaneMagic.MODID, "natural_harmony");
	public static final NotebookCategory MANIPULATING_MAGIC = new CategoryManipulatingMagic()
			.setRegistryName(ArcaneMagic.MODID, "manipulating_magic");

	private static boolean done = false;

	public static void register()
	{
		if (done)
			return;
		done = true;

		ArcaneMagicAPI.registerCategory(ANCIENT_RELICS);
		ArcaneMagicAPI.registerCategory(FORGOTTEN_KNOWLEDGE);
		ArcaneMagicAPI.registerCategory(BASIC_LINGUISTICS);
		ArcaneMagicAPI.registerCategory(MYSTICAL_ENERGY);
		ArcaneMagicAPI.registerCategory(ESSENCE_COLLECTION);
		ArcaneMagicAPI.registerCategory(CRYSTALLIZATION);
		ArcaneMagicAPI.registerCategory(NATURAL_HARMONY);
		ArcaneMagicAPI.registerCategory(MANIPULATING_MAGIC);
	}

}
