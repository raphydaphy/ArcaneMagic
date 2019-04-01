package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.Parchment;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import com.raphydaphy.crochet.network.PacketHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.IWorld;

import java.util.*;

public class DiscoveryParchment implements Parchment
{
	public static final String NAME = "progression.arcanemagic.discovery";
	public static final Ingredient[] GATHER_QUEST_OPTIONS = {
			Ingredient.ofItems(Items.CROSSBOW), Ingredient.ofItems(Blocks.SMOKER),
			Ingredient.ofItems(Blocks.LOOM),Ingredient.ofItems(Blocks.GRINDSTONE),
			Ingredient.ofItems(Items.LILY_OF_THE_VALLEY), Ingredient.ofItems(Blocks.LANTERN),
			Ingredient.ofItems(Items.SWEET_BERRIES), Ingredient.ofItems(Items.KELP),
			Ingredient.ofItems(Items.LEATHER_HORSE_ARMOR), Ingredient.ofItems(Blocks.FLETCHING_TABLE),
			Ingredient.ofItems(Blocks.SMITHING_TABLE), Ingredient.ofItems(Blocks.LECTERN),
			Ingredient.ofItems(Blocks.BARREL), Ingredient.ofItems(Blocks.CAMPFIRE),
			Ingredient.ofItems(Blocks.COMPOSTER), Ingredient.ofItems(Blocks.CARTOGRAPHY_TABLE),
			Ingredient.ofItems(Items.CORNFLOWER)
	};

	public static final Ingredient[] ANALYSIS_QUEST_OPTIONS = {
			Ingredient.ofItems(Items.IRON_INGOT), Ingredient.ofItems(Items.GOLD_INGOT),
			Ingredient.ofItems(Items.LEATHER), Ingredient.ofItems(Items.LAPIS_LAZULI),
			Ingredient.fromTag(ItemTags.LOGS), Ingredient.ofItems(Blocks.TNT),
			Ingredient.fromTag(ItemTags.PLANKS), Ingredient.fromTag(ItemTags.COALS),
			Ingredient.ofItems(Blocks.GLASS), Ingredient.ofItems(Blocks.CHEST),
			Ingredient.ofItems(Blocks.FURNACE), Ingredient.ofItems(Items.IRON_AXE),
			Ingredient.ofItems(Items.WOODEN_PICKAXE), Ingredient.ofItems(Items.STONE_HOE)
	};

	private int drownedKills = 0;
	private HashMap<Ingredient, Boolean> requiredItems = new HashMap<>();
	private boolean finishedNewGatherQuest = false;
	private boolean placedAnalyzer = false;
	private boolean analyzedNewItems = false;
	private boolean analyzedStick = false;
	private boolean craftedScepter = false;

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getText()
	{
		String base = "parchment.arcanemagic.discovery";
		if (craftedScepter)
		{
			return base + ".crafted_scepter";
		} else if (analyzedStick)
		{
			return base + ".analyzed_stick";
		} else if (analyzedNewItems)
		{
			return base + ".analyzed_new";
		} else if (placedAnalyzer)
		{
			return base + ".crafted_analyzer";
		} else if (finishedNewGatherQuest)
		{
			return base + ".craft_analyzer";
		} else if (drownedKills < 5)
		{
			return base + ".kill_quest";
		}
		return base + ".gather_new";
	}

	@Override
	public int getVerticalTextOffset()
	{
		return (finishedNewGatherQuest && !analyzedNewItems) ? 5 : (analyzedNewItems && !craftedScepter) ? 1 : 0;
	}

	@Override
	public void onOpened(IWorld world, PlayerEntity player, Hand hand, ItemStack stack)
	{
		CompoundTag data = ((DataHolder) player).getAdditionalData(ArcaneMagic.DOMAIN);

		drownedKills = data.getInt(ArcaneMagicConstants.DROWNED_KILLS_KEY);
		finishedNewGatherQuest = drownedKills > 4 && data.getBoolean(ArcaneMagicConstants.GATHER_QUEST_FINISHED_KEY);
		placedAnalyzer = finishedNewGatherQuest && data.getBoolean(ArcaneMagicConstants.PLACED_ANALYZER_KEY);
		analyzedNewItems = placedAnalyzer && data.getIntArray(ArcaneMagicConstants.GATHER_QUEST_ANALYZED_INDEXES_KEY).length >= 4;
		analyzedStick = analyzedNewItems && data.getBoolean(ArcaneMagicConstants.ANALYZED_STICK_KEY);
		craftedScepter = analyzedStick && data.getBoolean(ArcaneMagicConstants.CRAFTED_SCEPTER_KEY);

		if (analyzedNewItems && !analyzedStick)
		{
			int[] analysisQuestIndexes = data.getIntArray(ArcaneMagicConstants.ANALYSIS_QUEST_INDEXES_KEY);
			int[] analysisQuestCompletedIndexes = data.getIntArray(ArcaneMagicConstants.ANALYSIS_QUEST_ANALYZED_INDEXES_KEY);
			if (analysisQuestIndexes.length >= 4)
			{
				requiredItems = new HashMap<>();
				for (int index : analysisQuestIndexes)
				{
					if (index == -1)
					{
						requiredItems.put(Ingredient.ofItems(Items.STICK), false);
					} else
					{
						boolean analyzed = false;
						for (int analyzedIndex : analysisQuestCompletedIndexes)
						{
							if (analyzedIndex == index)
							{
								analyzed = true;
								break;
							}
						}
						requiredItems.put(ANALYSIS_QUEST_OPTIONS[index], analyzed);
					}
				}
			}
		} else if ((drownedKills > 4 && !finishedNewGatherQuest) || (placedAnalyzer && !analyzedNewItems))
		{
			int[] gatherQuestIndexes = data.getIntArray(ArcaneMagicConstants.GATHER_QUEST_INDEXES_KEY);
			if (gatherQuestIndexes.length >= 4)
			{
				List<Ingredient> ingredients = Arrays.asList(GATHER_QUEST_OPTIONS[gatherQuestIndexes[0]], GATHER_QUEST_OPTIONS[gatherQuestIndexes[1]],
				                                             GATHER_QUEST_OPTIONS[gatherQuestIndexes[2]], GATHER_QUEST_OPTIONS[gatherQuestIndexes[3]]);

				requiredItems = new HashMap<>();

				if (placedAnalyzer && !analyzedNewItems)
				{
					for (int index : data.getIntArray(ArcaneMagicConstants.GATHER_QUEST_ANALYZED_INDEXES_KEY))
					{
						requiredItems.put(GATHER_QUEST_OPTIONS[index], true);
					}
				} else if (drownedKills > 4 && !finishedNewGatherQuest)
				{
					for (int i = 0; i < player.inventory.getInvSize(); i++)
					{
						for (Ingredient ingredient : ingredients)
						{
							// If the player has this item in their inventory
							if (ingredient.method_8093(player.inventory.getInvStack(i)))
							{
								requiredItems.put(ingredient, true);
							}
						}
					}

					if (requiredItems.size() == ingredients.size())
					{
						if (!world.isClient())
						{
							PacketHandler.sendToClient(new ProgressionUpdateToastPacket(false), (ServerPlayerEntity) player);
							ArcaneMagicUtils.unlockRecipe(player, "analyzer");
							((DataHolder) player).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.GATHER_QUEST_FINISHED_KEY, true);
							((DataHolder) player).markAdditionalDataDirty();
						}
						finishedNewGatherQuest = true;
					}
				}

				requiredItems.putIfAbsent(ingredients.get(0), false);
				requiredItems.putIfAbsent(ingredients.get(1), false);
				requiredItems.putIfAbsent(ingredients.get(2), false);
				requiredItems.putIfAbsent(ingredients.get(3), false);
			}
		}
	}

	@Override
	public Recipe<? extends Inventory> getRecipe(RecipeManager manager)
	{
		return (finishedNewGatherQuest && !placedAnalyzer) ? manager.get(new Identifier(ArcaneMagic.DOMAIN, "analyzer")).orElse(null) :
		       (analyzedStick && !craftedScepter) ? manager.get(new Identifier(ArcaneMagic.DOMAIN, "golden_scepter")).orElse(null) :
		       (craftedScepter) ? manager.get(new Identifier(ArcaneMagic.DOMAIN, "notebook")).orElse(null) : null;
	}

	@Override
	public int getVerticalFeatureOffset()
	{
		return ((finishedNewGatherQuest && !analyzedNewItems)) ? -4 : (analyzedNewItems && !craftedScepter) ? -1 : 0;
	}

	@Override
	public boolean showProgressBar()
	{
		return drownedKills < 5;
	}

	@Override
	public double getProgressPercent()
	{
		return (drownedKills - 1) / 4d;
	}

	public Map<Ingredient, Boolean> getRequiredItems()
	{
		return ((drownedKills > 4 && !finishedNewGatherQuest) || (placedAnalyzer && !analyzedStick)) ? requiredItems : Collections.emptyMap();
	}
}
