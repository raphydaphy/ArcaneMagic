package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.NotebookElement;
import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ContentsNotebookSection implements NotebookSection {
    private static List<BasicNotebookElements.ItemInfoButton> buttons = new ArrayList<>();

    static {
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.DISCOVERY, ModRegistry.GOLDEN_SCEPTER, "notebook.arcanemagic.discovery.title", "notebook.arcanemagic.discovery.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.TRANSFIGURATION, ModRegistry.TRANSFIGURATION_TABLE, "notebook.arcanemagic.transfiguration.title", "notebook.arcanemagic.transfiguration.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.CRYSTALLIZATION, ModRegistry.GOLD_CRYSTAL, "notebook.arcanemagic.crystallization.title", "notebook.arcanemagic.crystallization.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.SOUL_STORAGE, ModRegistry.SOUL_PENDANT, "notebook.arcanemagic.soul_storage.title", "notebook.arcanemagic.soul_storage.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.SMELTING, ModRegistry.SMELTER, "notebook.arcanemagic.smelting.title", "notebook.arcanemagic.smelting.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.PERFECTION, ModRegistry.PURE_CRYSTAL, "notebook.arcanemagic.perfection.title", "notebook.arcanemagic.perfection.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.SOUL_COLLECTION, ModRegistry.ALTAR, "notebook.arcanemagic.soul_collection.title", "notebook.arcanemagic.soul_collection.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.ARMOURY, ModRegistry.IRON_DAGGER, "notebook.arcanemagic.armoury.title", "notebook.arcanemagic.armoury.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.INFUSION, ModRegistry.CRYSTAL_INFUSER, "notebook.arcanemagic.infusion.title", "notebook.arcanemagic.infusion.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.LIQUEFACTION, ModRegistry.MIXER, "notebook.arcanemagic.liquefaction.title", "notebook.arcanemagic.liquefaction.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.FLUID_TRANSPORT, ModRegistry.PIPE, "notebook.arcanemagic.fluid_transport.title", "notebook.arcanemagic.fluid_transport.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.PUMPING, ModRegistry.PUMP, "notebook.arcanemagic.pumping.title", "notebook.arcanemagic.pumping.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.TREMORS, Blocks.BEDROCK, "notebook.arcanemagic.tremors.title", "notebook.arcanemagic.tremors.desc").withPadding(5));
        buttons.add((BasicNotebookElements.ItemInfoButton) new BasicNotebookElements.ItemInfoButton(NotebookSectionRegistry.DECONSTRUCTION, ModRegistry.DECONSTRUCTION_STAFF, "notebook.arcanemagic.deconstruction.title", "notebook.arcanemagic.deconstruction.desc").withPadding(5));
    }

    @Override
    public Identifier getID() {
        return new Identifier(ArcaneMagic.DOMAIN, "contents");
    }

    @Override
    public boolean isVisibleTo(DataHolder player) {
        return true;
    }

    @Override
    public List<NotebookElement> getElements(DataHolder player, int page) {
        List<NotebookElement> elements = new ArrayList<>();

        int textPages = BasicNotebookElements.textPages("notebook.arcanemagic.intro", 2);

        if (page == 0) {
            String name = MinecraftClient.getInstance().player.getEntityName();
            elements.add(new BasicNotebookElements.BigHeading("notebook.arcanemagic.title", name.substring(0, 1).toUpperCase() + name.substring(1)).withPadding(-3));
        }

        if (page <= textPages) {
            elements.addAll(BasicNotebookElements.wrapText("notebook.arcanemagic.intro", 2, 0, page));
        } else {
            int number = 0;

            if (page == textPages + 1) {
                elements.add(new BasicNotebookElements.SmallHeading("notebook.arcanemagic.categories").withPadding(3));
            } else {
                elements.add(new BasicNotebookElements.Padding(10));
            }

            int thisPage = page - textPages;
            for (BasicNotebookElements.ItemInfoButton button : buttons) {
                if (button.link.isVisibleTo(player)) {
                    number++;
                    int properPage = (int) Math.ceil(number / 4f);
                    if (properPage == thisPage) {
                        elements.add(button);
                    } else if (properPage > thisPage) {
                        break;
                    }
                }
            }
        }
        return elements;
    }

    @Override
    public int getPageCount(DataHolder player) {
        int amount = 0;
        for (BasicNotebookElements.ItemInfoButton button : buttons) {
            if (button.link.isVisibleTo(player)) {
                amount++;
            }
        }
        return (int) Math.ceil(amount / 4f) + BasicNotebookElements.textPages("notebook.arcanemagic.intro", 2);
    }
}
