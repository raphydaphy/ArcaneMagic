package com.raphydaphy.arcanemagic.api.docs;

import net.minecraft.util.Identifier;

import java.util.List;

public interface INotebookSection
{
	Identifier getID();

	List<INotebookElement> getElements(int page);

	int getPageCount();
}
