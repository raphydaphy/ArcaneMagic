package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.NotebookElement;
import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PerfectionNotebookSection implements NotebookSection
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
	public List<NotebookElement> getElements(DataHolder player, int page)
	{
		List<NotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new BasicNotebookElements.SmallHeading("notebook.arcanemagic.perfection.title").withPadding(3));
		} else
		{
			elements.add(new BasicNotebookElements.Padding(3));
		}

		int firstText = BasicNotebookElements.textPages("notebook.arcanemagic.perfection.0", 2);
		elements.addAll(BasicNotebookElements.wrapText("notebook.arcanemagic.perfection.0", 2, 0, page));

		if (page == firstText + 1)
		{
			elements.add(new BasicNotebookElements.Padding(4));
			elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.pure_crystal").withPadding(10));
			elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "pure_crystal")).orElse(null)));
		}

		if (page >= firstText + 2 && player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_PURE_CRYSTAL_KEY))
		{
			int secondText = BasicNotebookElements.textPages("notebook.arcanemagic.perfection.1", 0) + firstText + 2;
			elements.addAll(BasicNotebookElements.wrapText("notebook.arcanemagic.perfection.1", 0, firstText + 2, page));

			if (page == secondText + 1)
			{
				elements.add(new BasicNotebookElements.Padding(4));
				elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.pure_scepter").withPadding(8));
				elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "pure_scepter")).orElse(null)));
			}
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return BasicNotebookElements.textPages("notebook.arcanemagic.perfection.0", 2) + (player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_PURE_CRYSTAL_KEY) ? BasicNotebookElements.textPages("notebook.arcanemagic.perfection.1", 0) + 3 : 1);
	}
}
