package com.raphydaphy.arcanemagic.api.docs;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.util.Identifier;

import java.util.List;

public interface NotebookSection
{
	Identifier getID();

	boolean isVisibleTo(DataHolder player);

	List<NotebookElement> getElements(DataHolder player, int page);

	default boolean hasNewInfo(DataHolder player)
	{
		return player.getAdditionalData(ArcaneMagic.DOMAIN).getCompound(ArcaneMagicConstants.NOTEBOOK_UPDATES_KET).getBoolean(getID().toString());
	}

	int getPageCount(DataHolder player);
}
