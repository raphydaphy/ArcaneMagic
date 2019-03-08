package com.raphydaphy.arcanemagic.api.docs;

import java.util.List;

public interface INotebookSection
{
	List<INotebookElement> getElements(int page);

	int getPageCount();
}
