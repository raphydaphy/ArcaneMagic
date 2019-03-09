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

public class TransfigurationNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "transfiguration");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_CRAFTING_TABLE_KEY);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.transfiguration.title", MinecraftClient.getInstance().player.getEntityName()).withPadding(3));
			elements.add(new NotebookElement.Paragraph(false, 0.7, "notebook.arcanemagic.transfiguration.0"));
		} else if (page == 1)
		{
			elements.add(new NotebookElement.Padding(8));
			elements.add(new NotebookElement.Paragraph(true, 0.8,"block.arcanemagic.transfiguration_table").withPadding(10));
			elements.add(new NotebookElement.Recipe( MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "transfiguration_table")).orElse(null)));
		} else if (page == 2 && player.getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_TRANSFIGURATION_TABLE_KEY))
		{
			elements.add(new NotebookElement.Padding(2));
			elements.add(new NotebookElement.Paragraph(false, 0.7, "notebook.arcanemagic.transfiguration.1"));
		} else if (page == 3 && player.getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_TRANSFIGURATION_TABLE_KEY))
		{
			elements.add(new NotebookElement.Padding(7));
			elements.add(new NotebookElement.Paragraph(true, 0.8,"item.arcanemagic.gold_crystal").withPadding(8));
			elements.add(new NotebookElement.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "gold_crystal")).orElse(null)));
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_TRANSFIGURATION_TABLE_KEY) ? 2 : 1;
	}
}
