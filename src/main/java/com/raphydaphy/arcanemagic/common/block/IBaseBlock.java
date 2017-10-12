package com.raphydaphy.arcanemagic.common.block;

import com.raphydaphy.arcanemagic.client.IHasModel;

import net.minecraft.item.ItemBlock;

public interface IBaseBlock extends IHasModel
{

	public void init();

	public ItemBlock createItemBlock();

}
