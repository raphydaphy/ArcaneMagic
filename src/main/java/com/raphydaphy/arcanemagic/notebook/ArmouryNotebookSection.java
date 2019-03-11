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

public class ArmouryNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "armoury");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_SWORD_KEY);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.armoury.title", MinecraftClient.getInstance().player.getEntityName()).withPadding(3));
			elements.add(new NotebookElement.Paragraph(false, 0.7, "notebook.arcanemagic.armoury.0"));
		} else if (page == 1)
		{
			elements.add(new NotebookElement.Padding(8));
			elements.add(new NotebookElement.Paragraph(true, 0.8,"item.arcanemagic.iron_dagger").withPadding(10));
			elements.add(new NotebookElement.Recipe( MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "iron_dagger")).orElse(null)));
		} else if (player.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_DAGGER_KEY) && page == 2)
		{
			elements.add(new NotebookElement.Padding(2));
			elements.add(new NotebookElement.Paragraph(false, 0.7,"notebook.arcanemagic.armoury.1"));
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_DAGGER_KEY) ? 2 : 1;
	}
}
