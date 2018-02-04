package com.raphydaphy.arcanemagic.api.analysis;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class AnalysisIngredient implements Predicate<Object> {

	protected Object source;

	protected AnalysisIngredient(Object source) {
		this.source = source;
	}

	@Override
	public boolean apply(Object input) {
		if (source instanceof Block && input instanceof IBlockState)
			return ((IBlockState) input).getBlock().equals(source);
		return source.equals(input);
	}

}
