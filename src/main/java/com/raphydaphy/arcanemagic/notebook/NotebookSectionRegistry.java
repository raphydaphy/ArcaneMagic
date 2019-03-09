package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class NotebookSectionRegistry
{
	private static final Map<Identifier, INotebookSection> REGISTRY = new HashMap<>();

	public static final INotebookSection CONTENTS = register(new ContentsNotebookSection());
	static final INotebookSection DISCOVERY = register(new DiscoveryNotebookSection());
	static final INotebookSection TRANSFIGURATION = register(new TransfigurationNotebookSection());
	static final INotebookSection CRYSTALLIZATION = register(new CrystallizationNotebookSection());

	public static INotebookSection get(Identifier id)
	{
		if (id != null && REGISTRY.containsKey(id))
		{
			return REGISTRY.get(id);
		}
		return null;
	}

	private static INotebookSection register(INotebookSection section)
	{
		REGISTRY.put(section.getID(), section);
		return section;
	}
}
