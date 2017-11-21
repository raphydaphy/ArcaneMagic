package com.raphydaphy.arcanemagic.common.notebook;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryAncientRelics;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryArcaneAnalysis;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryElementalCrafting;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryEssenceManipulation;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryForgottenKnowledge;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryFoundationsOfMagic;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryMagicalInsights;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryNaturalDivisions;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryNaturalDivisionsSub;
import com.raphydaphy.arcanemagic.common.notebook.category.CategoryCrystallization;
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
	public static final NotebookCategory FOUNDATIONS_OF_MAGIC = new CategoryFoundationsOfMagic()
			.setRegistryName(ArcaneMagic.MODID, "foundations_of_magic");
	public static final NotebookCategory NATURAL_DIVISIONS = new CategoryNaturalDivisions()
			.setRegistryName(ArcaneMagic.MODID, "natural_divisions");

	public static final CategoryNaturalDivisionsSub HORIZON_CATEGORY = new CategoryNaturalDivisionsSub(Anima.HORIZON,
			1, 2);
	public static final CategoryNaturalDivisionsSub OZONE_CATEGORY = new CategoryNaturalDivisionsSub(Anima.OZONE, 2,
			2);
	public static final CategoryNaturalDivisionsSub INFERNO_CATEGORY = new CategoryNaturalDivisionsSub(Anima.INFERNO,
			3, 2);
	public static final CategoryNaturalDivisionsSub DEPTH_CATEGORY = new CategoryNaturalDivisionsSub(Anima.DEPTH, 4,
			2);
	public static final CategoryNaturalDivisionsSub CHAOS_CATEGORY = new CategoryNaturalDivisionsSub(Anima.CHAOS, 5,
			2);
	public static final CategoryNaturalDivisionsSub PEACE_CATEGORY = new CategoryNaturalDivisionsSub(Anima.PEACE, 6,
			2);

	public static final CategoryNaturalDivisionsSub[] NATURAL_DIVISION_PAGES = { HORIZON_CATEGORY, OZONE_CATEGORY,
			INFERNO_CATEGORY, DEPTH_CATEGORY, CHAOS_CATEGORY, PEACE_CATEGORY };

	public static final NotebookCategory ESSENCE_MANIPULATION = new CategoryEssenceManipulation()
			.setRegistryName(ArcaneMagic.MODID, "essence_manipulation");
	public static final NotebookCategory ELEMENTAL_CRAFTING = new CategoryElementalCrafting()
			.setRegistryName(ArcaneMagic.MODID, "elemental_crafting");
	public static final NotebookCategory CRYSTALLIZATION = new CategoryCrystallization()
			.setRegistryName(ArcaneMagic.MODID, "crystallization");

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
		ArcaneMagicAPI.registerCategory(FOUNDATIONS_OF_MAGIC);
		ArcaneMagicAPI.registerCategory(NATURAL_DIVISIONS);

		ArcaneMagicAPI.registerSubCategories(NATURAL_DIVISION_PAGES);

		ArcaneMagicAPI.registerCategory(ESSENCE_MANIPULATION);
		ArcaneMagicAPI.registerCategory(ELEMENTAL_CRAFTING);
		ArcaneMagicAPI.registerCategory(CRYSTALLIZATION);
	}

}
