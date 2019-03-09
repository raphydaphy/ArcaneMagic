package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ContentsNotebookSection implements INotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "contents");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return true;
	}

	@Override
	public List<INotebookElement> getElements(DataHolder player, int page)
	{
		List<INotebookElement> elements = new ArrayList<>();
		List<NotebookElement.ItemInfoButton> buttons = new ArrayList<>();

		buttons.add((NotebookElement.ItemInfoButton) new NotebookElement.ItemInfoButton(NotebookSectionRegistry.DISCOVERY, ModRegistry.GOLDEN_SCEPTER, "notebook.arcanemagic.discovery.title", "notebook.arcanemagic.discovery.desc").withPadding(5));
		buttons.add((NotebookElement.ItemInfoButton) new NotebookElement.ItemInfoButton(NotebookSectionRegistry.TRANSFIGURATION, ModRegistry.TRANSFIGURATION_TABLE, "notebook.arcanemagic.transfiguration.title", "notebook.arcanemagic.transfiguration.desc").withPadding(5));
		buttons.add((NotebookElement.ItemInfoButton) new NotebookElement.ItemInfoButton(NotebookSectionRegistry.CRYSTALLIZATION, ModRegistry.GOLD_CRYSTAL, "notebook.arcanemagic.crystallization.title", "notebook.arcanemagic.crystallization.desc").withPadding(5));
		buttons.add((NotebookElement.ItemInfoButton) new NotebookElement.ItemInfoButton(NotebookSectionRegistry.SOUL_STORAGE, ModRegistry.SOUL_PENDANT, "notebook.arcanemagic.soul_storage.title", "notebook.arcanemagic.soul_storage.desc").withPadding(5));

		if (page == 0)
		{
			String name = MinecraftClient.getInstance().player.getEntityName();
			elements.add(new NotebookElement.BigHeading("notebook.arcanemagic.title", name.substring(0, 1).toUpperCase() + name.substring(1)).withPadding(-3));
			elements.add(new NotebookElement.Paragraph(false, 0.7, "notebook.arcanemagic.intro"));
		} else if (page == 1)
		{
			elements.add(new NotebookElement.SmallHeading("notebook.arcanemagic.categories").withPadding(3));
			int amount = 0;

			for (NotebookElement.ItemInfoButton button : buttons)
			{
				if (button.link.isVisibleTo(player))
				{
					elements.add(button);
					amount++;
				}

				if (amount >= 4)
				{
					break;
				}
			}
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return 1;
	}
}
