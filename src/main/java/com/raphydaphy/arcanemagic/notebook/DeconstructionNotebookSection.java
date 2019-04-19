package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.NotebookElement;
import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DeconstructionNotebookSection implements NotebookSection {
    @Override
    public Identifier getID() {
        return new Identifier(ArcaneMagic.DOMAIN, "deconstruction");
    }

    @Override
    public boolean isVisibleTo(DataHolder player) {
        return player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.ANALYZED_SOUL_SAND_KEY);
    }

    @Override
    public List<NotebookElement> getElements(DataHolder player, int page) {
        List<NotebookElement> elements = new ArrayList<>();
        if (page == 0) {
            elements.add(new BasicNotebookElements.SmallHeading("notebook.arcanemagic.deconstruction.title").withPadding(3));
        } else {
            elements.add(new BasicNotebookElements.Padding(3));
        }

        int firstText = BasicNotebookElements.textPages("notebook.arcanemagic.deconstruction.0", 2);
        elements.addAll(BasicNotebookElements.wrapText("notebook.arcanemagic.deconstruction.0", 2, 0, page));

        if (page == firstText + 1) {
            elements.add(new BasicNotebookElements.Padding(4));
            elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.deconstruction_staff").withPadding(10));
            elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "deconstruction_staff")).orElse(null)));
        }

        if (page >= firstText + 2 && player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_DECONSTRUCTION_STAFF_KEY)) {
            elements.addAll(BasicNotebookElements.wrapText("notebook.arcanemagic.deconstruction.1", 0, firstText + 2, page));
        }

        return elements;
    }

    @Override
    public int getPageCount(DataHolder player) {
        return BasicNotebookElements.textPages("notebook.arcanemagic.deconstruction.0", 2) + (player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_DECONSTRUCTION_STAFF_KEY) ? BasicNotebookElements.textPages("notebook.arcanemagic.deconstruction.1", 0) + 2 : 1);
    }
}
