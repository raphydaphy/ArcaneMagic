package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;

public class Categories
{
	public static final INotebookCategory ANCIENT_RELICS = new CategoryAncientRelics();
	public static final INotebookCategory BASIC_LINGUISTICS = new CategoryBasicLinguistics();
	public static final INotebookCategory ELEMENTAL_PARTICLES = new CategoryElementalParticles();
	public static final INotebookCategory MYSTICAL_ENERGY = new CategoryMysticalEnergy();
	public static final INotebookCategory ESSENCE_COLLECTION = new CategoryEssenceCollection();
	public static final INotebookCategory CRYSTALLIZATION = new CategoryCrystallization();
	public static final INotebookCategory NATURAL_HARMONY = new CategoryNaturalHarmony();
	public static final INotebookCategory MANIPULATING_MAGIC = new CategoryManipulatingMagic();	
	
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
