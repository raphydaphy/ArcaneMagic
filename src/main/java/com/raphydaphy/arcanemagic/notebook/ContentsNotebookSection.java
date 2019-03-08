package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class ContentsNotebookSection implements INotebookSection
{
	public static ContentsNotebookSection INSTANCE = new ContentsNotebookSection();

	@Override
	public List<INotebookElement> getElements(int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.BigHeading("notebook.arcanemagic.title", MinecraftClient.getInstance().player.getEntityName()).withPadding(-3));
			elements.add(new NotebookElement.Paragraph(false, "notebook.arcanemagic.intro"));
		} else if (page == 1)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.categories", MinecraftClient.getInstance().player.getEntityName()).withPadding(3));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.GOLDEN_SCEPTER, "Draining", "Using a Scepter to extract Soul").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.TRANSFIGURATION_TABLE, "Transfiguration", "Using Soul to craft various items").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.SOUL_PENDANT, "Soul Storage", "Binding Soul to items for storage").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.ALTAR, "Soul Collection", "Gathering Soul more efficiently").withPadding(5));
		} else if (page == 2)
		{
			elements.add(new NotebookElement.Paragraph(true,"Page 3"));
		} else if (page == 3)
		{
			elements.add(new NotebookElement.Paragraph(true, "Page 4"));
		} else if (page == 4)
		{
			elements.add(new NotebookElement.Paragraph(true, "Page 5"));
		}
		return elements;
	}

	@Override
	public int getPageCount()
	{
		return 4;
	}
}
