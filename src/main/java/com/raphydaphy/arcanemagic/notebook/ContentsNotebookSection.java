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
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.categories").withPadding(3));
			elements.add(new NotebookElement.ItemInfoButton(DrainingNotebookSection.INSTANCE, ModRegistry.GOLDEN_SCEPTER, "Draining", "Using a Scepter to extract Soul").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.TRANSFIGURATION_TABLE, "Transfiguration", "Using Soul to craft various items").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.SOUL_PENDANT, "Soul Storage", "Binding Soul to items for storage").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.ALTAR, "Soul Collection", "Gathering Soul more efficiently").withPadding(5));
		} else if (page == 2)
		{
			elements.add(new NotebookElement.Padding(10));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.DIAMOND_CRYSTAL, "Crystallization", "Infusing Soul into various gems").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.PURE_SCEPTER, "Perfection", "Experimenting with pure Soul").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.SMELTER, "Smelting", "Using Soul to smelt items efficiently").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.IRON_DAGGER, "Armoury", "Creating new weapons and armor").withPadding(5));
		} else if (page == 3)
		{
			elements.add(new NotebookElement.Padding(10));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.CRYSTAL_INFUSER, "Infusing", "Improving equipment using Soul").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.PUMP, "Pumping", "Collecting fluids for easier usage").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.MIXER, "Liquefaction", "Creating Liquified Soul").withPadding(5));
			elements.add(new NotebookElement.ItemInfo(ModRegistry.PIPE, "Automation", "Transport systems for fluids").withPadding(5));
		}
		return elements;
	}

	@Override
	public int getPageCount()
	{
		return 3;
	}
}
