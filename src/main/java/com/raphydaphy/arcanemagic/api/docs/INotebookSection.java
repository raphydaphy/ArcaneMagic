package com.raphydaphy.arcanemagic.api.docs;

import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.util.Identifier;

import java.util.List;

public interface INotebookSection
{
	Identifier getID();

	boolean isVisibleTo(DataHolder player);

	List<INotebookElement> getElements(DataHolder player, int page);

	default boolean hasNewInfo(DataHolder player)
	{
		return player.getAdditionalData().getCompound(ArcaneMagicConstants.NOTEBOOK_UPDATES_KET).getBoolean(getID().toString());
	}

	int getPageCount(DataHolder player);
}
