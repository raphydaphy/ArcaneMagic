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
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_CRAFTING_TABLE);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.transfiguration.title", MinecraftClient.getInstance().player.getEntityName()).withPadding(3));
			elements.add(new NotebookElement.Paragraph(false, "notebook.arcanemagic.transfiguration.0"));
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return 0;
	}
}
