package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class NotebookSectionRegistry {
    private static final Map<Identifier, NotebookSection> REGISTRY = new HashMap<>();

    public static final NotebookSection DISCOVERY = register(new DiscoveryNotebookSection());
    public static final NotebookSection TRANSFIGURATION = register(new TransfigurationNotebookSection());
    public static final NotebookSection CRYSTALLIZATION = register(new CrystallizationNotebookSection());
    public static final NotebookSection SOUL_STORAGE = register(new SoulStorageNotebookSection());
    public static final NotebookSection PERFECTION = register(new PerfectionNotebookSection());
    public static final NotebookSection SOUL_COLLECTION = register(new SoulCollectionNotebookSection());
    public static final NotebookSection ARMOURY = register(new ArmouryNotebookSection());
    public static final NotebookSection SMELTING = register(new SmeltingNotebookSection());
    public static final NotebookSection INFUSION = register(new InfusionNotebookSection());
    public static final NotebookSection LIQUEFACTION = register(new LiquefactionNotebookSection());
    public static final NotebookSection FLUID_TRANSPORT = register(new FluidTransportNotebookSection());
    public static final NotebookSection PUMPING = register(new PumpingNotebookSection());
    public static final NotebookSection TREMORS = register(new TremorsNotebookSection());
    public static final NotebookSection DECONSTRUCTION = register(new DeconstructionNotebookSection());

    public static final NotebookSection CONTENTS = register(new ContentsNotebookSection());

    public static NotebookSection get(Identifier id) {
        if (id != null && REGISTRY.containsKey(id)) {
            return REGISTRY.get(id);
        }
        return null;
    }

    private static NotebookSection register(NotebookSection section) {
        REGISTRY.put(section.getID(), section);
        return section;
    }
}
