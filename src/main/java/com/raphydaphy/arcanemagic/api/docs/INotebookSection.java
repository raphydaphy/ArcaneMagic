package com.raphydaphy.arcanemagic.api.docs;

import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.util.Identifier;

import java.util.List;

public interface INotebookSection
{
	Identifier getID();

	boolean isVisibleTo(DataHolder player);

	List<INotebookElement> getElements(DataHolder player, int page);

	int getPageCount(DataHolder player);
}
