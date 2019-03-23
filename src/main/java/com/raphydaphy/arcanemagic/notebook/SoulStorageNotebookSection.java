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

public class SoulStorageNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "soul_storage");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_GOLD_CRYSTAL_KEY);
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();

		if (page == 0)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.soul_storage.title").withPadding(3));
		} else
		{
			elements.add(new NotebookElement.Padding(3));
		}

		int firstText = NotebookElement.textPages("notebook.arcanemagic.soul_storage.0", 2);
		elements.addAll(NotebookElement.wrapText("notebook.arcanemagic.soul_storage.0", 2, 0, page));

		if (page == firstText + 1)
		{
			elements.add(new NotebookElement.Padding(4));
			elements.add(new NotebookElement.Paragraph(true, 1, "item.arcanemagic.soul_pendant").withPadding(10));
			elements.add(new NotebookElement.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "soul_pendant")).orElse(null)));
		}

		if (page >= firstText + 2 && player.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_SOUL_PENDANT_KEY))
		{
			elements.addAll(NotebookElement.wrapText("notebook.arcanemagic.soul_storage.1", 0, firstText + 2, page));
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return NotebookElement.textPages("notebook.arcanemagic.soul_storage.0", 2) + (player.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_SOUL_PENDANT_KEY) ? NotebookElement.textPages("notebook.arcanemagic.soul_storage.1", 0) + 2 : 1);
	}
}
