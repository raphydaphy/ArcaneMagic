package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.INotebookSection;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class NotebookSectionRegistry
{
	private static final Map<Identifier, INotebookSection> REGISTRY = new HashMap<>();

	public static final INotebookSection DISCOVERY = register(new DiscoveryNotebookSection());
	public static final INotebookSection TRANSFIGURATION = register(new TransfigurationNotebookSection());
	public static final INotebookSection CRYSTALLIZATION = register(new CrystallizationNotebookSection());
	public static final INotebookSection SOUL_STORAGE = register(new SoulStorageNotebookSection());
	public static final INotebookSection PERFECTION = register(new PerfectionNotebookSection());
	public static final INotebookSection SOUL_COLLECTION = register(new SoulCollectionNotebookSection());
	public static final INotebookSection ARMOURY = register(new ArmouryNotebookSection());
	public static final INotebookSection SMELTING = register(new SmeltingNotebookSection());
	public static final INotebookSection INFUSION = register(new InfusionNotebookSection());
	public static final INotebookSection LIQUEFACTION = register(new LiquefactionNotebookSection());
	public static final INotebookSection FLUID_TRANSPORT = register(new FluidTransportNotebookSection());
	public static final INotebookSection PUMPING = register(new PumpingNotebookSection());

	public static final INotebookSection CONTENTS = register(new ContentsNotebookSection());

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
