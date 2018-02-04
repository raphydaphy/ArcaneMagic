package com.raphydaphy.arcanemagic.api.analysis;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AnalysisManager {

	private final Map<AnalysisIngredient, List<NotebookCategory>> REGISTRY = new HashMap<>();

	@Nullable
	public AnalysisIngredient getIngredient(Object toAnalyze) {
		for (AnalysisIngredient ingred : REGISTRY.keySet())
			if (ingred.apply(toAnalyze))
				return ingred;
		return null;
	}

	@Nonnull
	public List<NotebookCategory> getAnalysisResults(Object toAnalyze) {
		return getAnalysisResults(getIngredient(toAnalyze));
	}

	@Nullable
	public List<NotebookCategory> getAnalysisResults(AnalysisIngredient ingred) {
		return REGISTRY.getOrDefault(ingred, Collections.emptyList());
	}

	public void registerForAnalysis(AnalysisIngredient ingred, NotebookCategory... cats) {
		if (REGISTRY.get(ingred) == null)
			REGISTRY.put(ingred, Arrays.asList(cats));
	}

	public void registerForAnalysis(String ore, NotebookCategory... cats) {
		registerForAnalysis(new AnalysisStack(ore), cats);
	}

	public void registerForAnalysis(ItemStack stack, NotebookCategory... cats) {
		registerForAnalysis(new AnalysisStack(stack), cats);
	}

	public void registerForAnalysis(Item item, NotebookCategory... cats) {
		registerForAnalysis(new AnalysisStack(item), cats);
	}

	public void registerForAnalysis(IBlockState state, NotebookCategory... cats) {
		registerForAnalysis(new AnalysisIngredient(state), cats);
	}

	public void registerForAnalysis(Block block, NotebookCategory... cats) {
		registerForAnalysis(new AnalysisIngredient(block), cats);
	}

	public void registerForAnalysisWithItem(Block block, NotebookCategory... cats) {
		registerForAnalysis(block, cats);
		registerForAnalysis(Item.getItemFromBlock(block), cats);
	}

}
