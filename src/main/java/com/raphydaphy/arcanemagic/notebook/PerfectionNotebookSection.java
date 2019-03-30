package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PerfectionNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "perfection");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return ArcaneMagicUtils.craftedAllCrystals(player);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.perfection.title").withPadding(3));
		} else
		{
			elements.add(new NotebookElement.Padding(3));
		}

		int firstText = NotebookElement.textPages("notebook.arcanemagic.perfection.0", 2);
		elements.addAll(NotebookElement.wrapText("notebook.arcanemagic.perfection.0", 2, 0, page));

		if (page == firstText + 1)
		{
			elements.add(new NotebookElement.Padding(4));
			elements.add(new NotebookElement.Paragraph(true, 1, "item.arcanemagic.pure_crystal").withPadding(10));
			elements.add(new NotebookElement.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "pure_crystal")).orElse(null)));
		}

		if (page >= firstText + 2 && player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_PURE_CRYSTAL_KEY))
		{
			int secondText = NotebookElement.textPages("notebook.arcanemagic.perfection.1", 0) + firstText + 2;
			elements.addAll(NotebookElement.wrapText("notebook.arcanemagic.perfection.1", 0, firstText + 2, page));

			if (page == secondText + 1)
			{
				elements.add(new NotebookElement.Padding(4));
				elements.add(new NotebookElement.Paragraph(true, 1, "item.arcanemagic.pure_scepter").withPadding(8));
				elements.add(new NotebookElement.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "pure_scepter")).orElse(null)));
			}
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return NotebookElement.textPages("notebook.arcanemagic.perfection.0", 2) + (player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_PURE_CRYSTAL_KEY) ? NotebookElement.textPages("notebook.arcanemagic.perfection.1", 0) + 3 : 1);
	}
}
