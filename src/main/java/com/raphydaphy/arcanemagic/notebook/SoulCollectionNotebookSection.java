package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SoulCollectionNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "soul_collection");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_OBSIDIAN_KEY);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.soul_collection.title").withPadding(3));
			elements.add(new NotebookElement.Paragraph(false, 0.7, "notebook.arcanemagic.soul_collection.0"));
		} else if (page == 1)
		{
			elements.add(new NotebookElement.Padding(8));
			elements.add(new NotebookElement.Paragraph(true, 0.8,"block.arcanemagic.altar").withPadding(10));
			elements.add(new NotebookElement.Recipe( MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "altar")).orElse(null)));
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return 1;
	}
}
