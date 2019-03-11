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

public class InfusionNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "infusion");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_ENCHANTING_TABLE_KEY);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.infusion.title", MinecraftClient.getInstance().player.getEntityName()).withPadding(3));
			elements.add(new NotebookElement.Paragraph(false, 0.7, "notebook.arcanemagic.infusion.0"));
		} else if (page == 1)
		{
			elements.add(new NotebookElement.Padding(8));
			elements.add(new NotebookElement.Paragraph(true, 0.8,"block.arcanemagic.crystal_infuser").withPadding(10));
			elements.add(new NotebookElement.Recipe( MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "crystal_infuser")).orElse(null)));
		} else if (player.getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_INFUSER_KEY) && page == 2)
		{
			elements.add(new NotebookElement.Padding(2));
			elements.add(new NotebookElement.Paragraph(false, 0.7,"notebook.arcanemagic.infusion.1"));
		} else if (player.getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_INFUSER_KEY) && page == 3)
		{
			elements.add(new NotebookElement.Padding(2));
			elements.add(new NotebookElement.Paragraph(false, 0.7,"notebook.arcanemagic.infusion.2"));
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_INFUSER_KEY) ? 3 : 1;
	}
}
