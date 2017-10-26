package com.raphydaphy.arcanemagic.common.block;

import com.raphydaphy.arcanemagic.client.IHasModel;

import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;

public interface IBaseBlock extends IHasModel
{

	public void setup(String name, float hardness, float resist, SoundType sound);

	public void init();

	public ItemBlock createItemBlock();

}
