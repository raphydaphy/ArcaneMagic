package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import io.github.prospector.silk.fluid.DropletValues;
import net.minecraft.util.Identifier;

public class ArcaneMagicConstants
{
	public static final int SOUL_METER_STAGES = 88;
	public static final String SOUL_KEY = "SoulStored";
	public static final int SOUL_PENDANT_MAX_SOUL = 150;
	public static final int SOUL_METER_MAX = SOUL_PENDANT_MAX_SOUL + 50;

	public static final String PARCHMENT_TYPE_KEY = "ParchmentType";
	public static final String DIED_WITH_PARCHMENT_KEY = "DiedWithParchment";
	public static final String SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY = "SendParchmentRecipeOnRespawn";

	public static final Identifier GLOW_PARTICLE_TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "particle/glow_particle");
	public static final Identifier SMOKE_PARTICLE_TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "particle/smoke_particle");
	public static final Identifier NOTEBOOK_TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "textures/gui/notebook.png");

	// Dimensions of the notebook texture with and without the bottom icons
	public static final int NOTEBOOK_WIDTH = 272;
	public static final int NOTEBOOK_HEIGHT = 180;
	public static final int NOTEBOOK_TEX_HEIGHT = 256;

	public static final String NOTEBOOK_SECTION_KEY = "NotebookSection";
	public static final String NOTEBOOK_PAGE_KEY = "NotebookPage";
	public static final String NOTEBOOK_CONTENTS_PAGE_KEY = "NotebookContentsPage";
	public static final String NOTEBOOK_UPDATES_KET = "NotebookUpdates";

	// Amount of liquified soul produced by a single unit of soul
	public static final int LIQUIFIED_SOUL_RATIO = DropletValues.NUGGET + 5;
	public static final int SOUL_PER_SMELT = 2;

	public static final String UUID_KEY = "CrystalItemIdentifier";
	public static final String PASSIVE_CRYSTAL_KEY = "PassiveCrystal";
	public static final String ACTIVE_CRYSTAL_KEY = "ActiveCrystal";
	public static final String ACTIVE_TIMER_KEY = "ActiveTimer";

	// For the Discovery parchment
	public static final String DROWNED_KILLS_KEY = "DrownedKills"; // How many Drowned Zombies the player has killed, capped at 5
	public static final String GATHER_QUEST_INDEXES_KEY = "GatherQuestIndexes"; // List of indexes in DiscoveryParchment.GATHER_QUEST_OPTIONS of new 1.13 & 1.14 items that need to be collected
	public static final String GATHER_QUEST_FINISHED_KEY = "FinishedGatherQuest"; // If the player has collected all four of the new 1.13 and 1.14 items requested
	public static final String GATHER_QUEST_ANALYZED_INDEXES_KEY = "GatherAnalysisFinishedIndexes"; // List of indexes DiscoveryParchment.GATHER_QUEST_OPTIONS of the new 1.13 & 1.14 items that have been analyzed
	public static final String PLACED_ANALYZER_KEY = "PlacedAnalyzer"; // If the player has placed an Analyzer block
	public static final String ANALYSIS_QUEST_INDEXES_KEY = "AnalysisQuestIndexes"; // List of indexes in DiscoveryParchment.ANALYSIS_QUEST_OPTIONS of items that need to be analyzed
	public static final String ANALYSIS_QUEST_ANALYZED_INDEXES_KEY = "AnalysisQuestAnalyzedIndexesKey"; // List of indexes in DiscoveryParchment.ANALYSIS_QUEST_OPTION of items which have been analyzed
	public static final String ANALYZED_STICK_KEY = "AnalyzedStick"; // If the player has placed a stick in the analyzer
	public static final String CRAFTED_SCEPTER_KEY = "CraftedScepter"; // If the player has crafted a Golden Scepter

	// For notebook progression
	public static final String ANALYZED_CRAFTING_TABLE_KEY = "AnalyzedCraftingTable";
	public static final String PLACED_TRANSFIGURATION_TABLE_KEY = "PlacedTransfigurationTable";
	public static final String CRAFTED_GOLD_CRYSTAL_KEY = "CraftedGoldCrystal";
	public static final String CRAFTED_SOUL_PENDANT_KEY = "CraftedSoulPendant";
	public static final String ANALYZED_BLAST_FURNACE_KEY = "AnalyzedBlastFurnace";
	public static final String CRAFTED_PURE_CRYSTAL_KEY = "CraftedPureCrystal";
	public static final String ANALYZED_OBSIDIAN_KEY = "AnalyzedObsidian";
	public static final String ANALYZED_SWORD_KEY = "AnalyzedSword";
}
