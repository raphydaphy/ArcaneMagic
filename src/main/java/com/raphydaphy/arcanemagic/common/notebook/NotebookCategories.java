package com.raphydaphy.arcanemagic.common.notebook;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryAncientRelics;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryArcaneAnalysis;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryCrystallization;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryAfterLife;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryForgottenKnowledge;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryManipulatingMagic;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryMagicalInsights;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryNaturalHarmony;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryUnknownRealms;

public class NotebookCategories
{
	// the player should NOT have this category unlocked ! it is only shown if they open a notebook before obtaining an ancient parchment, which should be impossible!
	public static final NotebookCategory UNKNOWN_REALMS = new CategoryUnknownRealms().setRegistryName(ArcaneMagic.MODID,
			"unknown_realms");

	// regular categories
	public static final NotebookCategory ANCIENT_RELICS = new CategoryAncientRelics().setRegistryName(ArcaneMagic.MODID,
			"ancient_relics");
	public static final NotebookCategory FORGOTTEN_KNOWLEDGE = new CategoryForgottenKnowledge()
			.setRegistryName(ArcaneMagic.MODID, "forgotten_knowledge");
	public static final NotebookCategory ARCANE_ANALYSIS = new CategoryArcaneAnalysis()
			.setRegistryName(ArcaneMagic.MODID, "arcane_analysis");
	public static final NotebookCategory MAGICAL_INSIGHTS = new CategoryMagicalInsights()
			.setRegistryName(ArcaneMagic.MODID, "magical_insights");
	public static final NotebookCategory AFTER_LIFE = new CategoryAfterLife()
			.setRegistryName(ArcaneMagic.MODID, "after_life");
	
	// these categories are just temporary until a real category replaces them
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

		ArcaneMagicAPI.registerCategory(UNKNOWN_REALMS);
		ArcaneMagicAPI.registerCategory(ANCIENT_RELICS);
		ArcaneMagicAPI.registerCategory(FORGOTTEN_KNOWLEDGE);
		ArcaneMagicAPI.registerCategory(ARCANE_ANALYSIS);
		ArcaneMagicAPI.registerCategory(MAGICAL_INSIGHTS);
		ArcaneMagicAPI.registerCategory(AFTER_LIFE);
		ArcaneMagicAPI.registerCategory(CRYSTALLIZATION);
		ArcaneMagicAPI.registerCategory(NATURAL_HARMONY);
		ArcaneMagicAPI.registerCategory(MANIPULATING_MAGIC);
	}

}
