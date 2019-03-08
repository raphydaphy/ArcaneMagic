package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.INotebookElement;
import com.raphydaphy.arcanemagic.api.docs.INotebookPage;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class TitleNotebookPage implements INotebookPage
{
	public static TitleNotebookPage INSTANCE = new TitleNotebookPage();

	@Override
	public List<INotebookElement> getElements()
	{
		List<INotebookElement> elements = new ArrayList<>();
		elements.add(new NotebookElement.BigHeading("notebook.arcanemagic.title", MinecraftClient.getInstance().player.getEntityName()).withPadding(-3));
		elements.add(new NotebookElement.Paragraph(false, "notebook.arcanemagic.intro"));
		return elements;
	}
}
