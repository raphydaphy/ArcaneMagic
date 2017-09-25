package com.raphydaphy.thaumcraft.block;

import com.raphydaphy.thaumcraft.client.IHasModel;

import net.minecraft.item.ItemBlock;

public interface IBaseBlock extends IHasModel {

	public void init();
	public ItemBlock createItemBlock();
	
}
