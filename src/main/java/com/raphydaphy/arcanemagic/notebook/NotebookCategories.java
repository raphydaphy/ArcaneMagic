package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.notebook.category.CategoryAncientRelics;
import com.raphydaphy.arcanemagic.notebook.category.CategoryBasicLinguistics;
import com.raphydaphy.arcanemagic.notebook.category.CategoryCrystallization;
import com.raphydaphy.arcanemagic.notebook.category.CategoryElementalParticles;
import com.raphydaphy.arcanemagic.notebook.category.CategoryEssenceCollection;
import com.raphydaphy.arcanemagic.notebook.category.CategoryManipulatingMagic;
import com.raphydaphy.arcanemagic.notebook.category.CategoryMysticalEnergy;
import com.raphydaphy.arcanemagic.notebook.category.CategoryNaturalHarmony;

public class NotebookCategories
{
	public static final NotebookCategory ANCIENT_RELICS = new CategoryAncientRelics();
	public static final NotebookCategory BASIC_LINGUISTICS = new CategoryBasicLinguistics();
	public static final NotebookCategory ELEMENTAL_PARTICLES = new CategoryElementalParticles();
	public static final NotebookCategory MYSTICAL_ENERGY = new CategoryMysticalEnergy();
	public static final NotebookCategory ESSENCE_COLLECTION = new CategoryEssenceCollection();
	public static final NotebookCategory CRYSTALLIZATION = new CategoryCrystallization();
	public static final NotebookCategory NATURAL_HARMONY = new CategoryNaturalHarmony();
	public static final NotebookCategory MANIPULATING_MAGIC = new CategoryManipulatingMagic();	
	
	private static boolean done = false;

	public static void register()
	{
		if (done)
			return;
		done = true;

		ArcaneMagicAPI.registerCategory(ANCIENT_RELICS);
		ArcaneMagicAPI.registerCategory(BASIC_LINGUISTICS);
		ArcaneMagicAPI.registerCategory(ELEMENTAL_PARTICLES);
		ArcaneMagicAPI.registerCategory(MYSTICAL_ENERGY);
		ArcaneMagicAPI.registerCategory(ESSENCE_COLLECTION);
		ArcaneMagicAPI.registerCategory(CRYSTALLIZATION);
		ArcaneMagicAPI.registerCategory(NATURAL_HARMONY);
		ArcaneMagicAPI.registerCategory(MANIPULATING_MAGIC);
	}

}
