package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DeconstructionNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "deconstruction");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.ANALYZED_SOUL_SAND_KEY);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.deconstruction.title").withPadding(3));
		} else
		{
			elements.add(new NotebookElement.Padding(3));
		}

		int firstText = NotebookElement.textPages("notebook.arcanemagic.deconstruction.0", 2);
		elements.addAll(NotebookElement.wrapText("notebook.arcanemagic.deconstruction.0", 2, 0, page));

		if (page == firstText + 1)
		{
			elements.add(new NotebookElement.Padding(4));
			elements.add(new NotebookElement.Paragraph(true, 1, "item.arcanemagic.deconstruction_staff").withPadding(10));
			elements.add(new NotebookElement.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "deconstruction_staff")).orElse(null)));
		}

		if (page >= firstText + 2 && player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_DECONSTRUCTION_STAFF_KEY))
		{
			elements.addAll(NotebookElement.wrapText("notebook.arcanemagic.deconstruction.1", 0, firstText + 2, page));
		}

		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return NotebookElement.textPages("notebook.arcanemagic.deconstruction.0", 2) + (player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_DECONSTRUCTION_STAFF_KEY) ? NotebookElement.textPages("notebook.arcanemagic.deconstruction.1", 0) + 2 : 1);
	}
}
