package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.NotebookElement;
import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryNotebookSection implements NotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "discovery");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_SCEPTER_KEY);
	}

	@Override
	public List<NotebookElement> getElements(DataHolder player, int page)
	{
		List<NotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new BasicNotebookElements.SmallHeading("notebook.arcanemagic.discovery.title").withPadding(3));
		} else
		{
			elements.add(new BasicNotebookElements.Padding(3));
		}
		elements.addAll(BasicNotebookElements.wrapText("notebook.arcanemagic.discovery.0", 2, 0, page));
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return BasicNotebookElements.textPages("notebook.arcanemagic.discovery.0", 2);
	}
}
