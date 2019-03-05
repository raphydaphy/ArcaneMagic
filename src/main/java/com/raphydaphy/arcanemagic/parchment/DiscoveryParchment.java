package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.parchment.IParchment;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.IWorld;

import java.util.*;

public class DiscoveryParchment implements IParchment
{
	public static final String NAME = "progression.arcanemagic.discovery";
	public static final Ingredient[] GATHER_QUEST_NEW_OPTIONS = {
			Ingredient.ofItems(Items.HORN_CORAL), Ingredient.ofItems(Items.TUBE_CORAL),
			Ingredient.ofItems(Items.FIRE_CORAL), Ingredient.ofItems(Items.BRAIN_CORAL),
			Ingredient.ofItems(Items.BUBBLE_CORAL), Ingredient.ofItems(Items.CROSSBOW),
			Ingredient.ofItems(Items.BELL), Ingredient.ofItems(Items.BAMBOO),
			Ingredient.ofItems(Items.LILY_OF_THE_VALLEY), Ingredient.ofItems(Blocks.LANTERN),
			Ingredient.ofItems(Items.SWEET_BERRIES), Ingredient.ofItems(Items.KELP),
			Ingredient.ofItems(Blocks.BRAIN_CORAL), Ingredient.ofItems(Blocks.BUBBLE_CORAL),
			Ingredient.ofItems(Blocks.FIRE_CORAL), Ingredient.ofItems(Blocks.HORN_CORAL),
			Ingredient.ofItems(Blocks.TUBE_CORAL), Ingredient.ofItems(Items.LEATHER_HORSE_ARMOR),
			Ingredient.ofItems(Blocks.BARREL), Ingredient.ofItems(Blocks.CAMPFIRE),
			Ingredient.ofItems(Blocks.COMPOSTER), Ingredient.ofItems(Blocks.CARTOGRAPHY_TABLE),
			Ingredient.ofItems(Items.CORNFLOWER), Ingredient.ofItems(Blocks.GRINDSTONE),
			Ingredient.ofItems(Blocks.LECTERN), Ingredient.ofItems(Blocks.LOOM),
			Ingredient.ofItems(Blocks.SCAFFOLDING), Ingredient.ofItems(Blocks.SMOKER),
			Ingredient.ofItems(Blocks.BLAST_FURNACE)
	};

	private int drownedKills = 0;
	private HashMap<Ingredient, Boolean> requiredItems = new HashMap<>();
	private boolean finishedNewGatherQuest = false;
	private boolean placedAnalyzer = false;

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getText()
	{
		String base = "parchment.arcanemagic.discovery";
		if (placedAnalyzer)
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
		return ((finishedNewGatherQuest && !placedAnalyzer) || (placedAnalyzer)) ? 5 : 0;
	}

	@Override
	public void onOpened(IWorld world, PlayerEntity player, Hand hand, ItemStack stack)
	{
		CompoundTag data = ((DataHolder)player).getAdditionalData();

		drownedKills = data.getInt(ArcaneMagicConstants.DROWNED_KILLS_KEY);
		finishedNewGatherQuest = data.getBoolean(ArcaneMagicConstants.NEW_GATHER_QUEST_FINISHED_KEY);
		placedAnalyzer = data.getBoolean(ArcaneMagicConstants.PLACED_ANALYZER);

		if (!finishedNewGatherQuest || placedAnalyzer)
		{
			int[] newGatherQuestIndexes = data.getIntArray(ArcaneMagicConstants.NEW_GATHER_QUEST_INDEXES_KEY);
			if (newGatherQuestIndexes.length >= 4)
			{
				List<Ingredient> ingredients = Arrays.asList(GATHER_QUEST_NEW_OPTIONS[newGatherQuestIndexes[0]], GATHER_QUEST_NEW_OPTIONS[newGatherQuestIndexes[1]],
						GATHER_QUEST_NEW_OPTIONS[newGatherQuestIndexes[2]], GATHER_QUEST_NEW_OPTIONS[newGatherQuestIndexes[3]]);

				requiredItems = new HashMap<>();

				if (!finishedNewGatherQuest)
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
							((DataHolder) player).getAdditionalData().putBoolean(ArcaneMagicConstants.NEW_GATHER_QUEST_FINISHED_KEY, true);
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
		return (finishedNewGatherQuest && !placedAnalyzer) ? manager.get(new Identifier(ArcaneMagic.DOMAIN, "analyzer")).orElse(null) : null;
	}

	@Override
	public int getVerticalFeatureOffset()
	{
		return ((finishedNewGatherQuest && !placedAnalyzer) || (placedAnalyzer)) ? -4 : 0;
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
		return ((drownedKills > 4 && !finishedNewGatherQuest) || (placedAnalyzer)) ? requiredItems : Collections.emptyMap();
	}
}
