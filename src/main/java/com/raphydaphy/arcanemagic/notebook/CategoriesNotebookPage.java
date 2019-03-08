package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookPage;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class CategoriesNotebookPage implements INotebookPage
{
	public static CategoriesNotebookPage INSTANCE = new CategoriesNotebookPage();

	@Override
	public List<INotebookElement> getElements()
	{
		List<INotebookElement> elements = new ArrayList<>();
		elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.categories", MinecraftClient.getInstance().player.getEntityName()).withPadding(3));
		elements.add(new NotebookElement.ItemInfo(ModRegistry.GOLDEN_SCEPTER, "Draining", "Using a Scepter to extract Soul").withPadding(5));
		elements.add(new NotebookElement.ItemInfo(ModRegistry.TRANSFIGURATION_TABLE, "Transfiguration", "Using Soul to craft various items").withPadding(5));
		elements.add(new NotebookElement.ItemInfo(ModRegistry.SOUL_PENDANT, "Soul Storage", "Binding Soul to items for storage").withPadding(5));
		elements.add(new NotebookElement.ItemInfo(ModRegistry.ALTAR, "Soul Collection", "Gathering Soul more efficiently").withPadding(5));
		return elements;
	}
}
