package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class DrainingNotebookSection implements INotebookSection
{
	public static DrainingNotebookSection INSTANCE = new DrainingNotebookSection();

	@Override
	public List<INotebookElement> getElements(int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("Draining", MinecraftClient.getInstance().player.getEntityName()).withPadding(3));
			elements.add(new NotebookElement.Paragraph(false, "Draining is that thing you do when you aim your scepter at a mob and hooold it down"));
		}
		return elements;
	}

	@Override
	public int getPageCount()
	{
		return 0;
	}
}
