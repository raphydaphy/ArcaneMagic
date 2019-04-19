package com.raphydaphy.arcanemagic.integration;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import me.shedaniel.rei.api.DisplaySettings;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.widget.ItemSlotWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class TransfigurationCategory implements RecipeCategory<TransfigurationDisplay> {
    private static final Identifier DISPLAY_TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "textures/gui/recipe_display.png");
    private static final Identifier SOUL_METER_TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "textures/misc/circle_hud.png");

    @Override
    public Identifier getIdentifier() {
        return ArcaneMagicREIPlugin.TRANSFIGURATION;
    }

    @Override
    public ItemStack getCategoryIcon() {
        return new ItemStack(ModRegistry.TRANSFIGURATION_TABLE);
    }

    @Override
    public String getCategoryName() {
        return I18n.translate("category." + ArcaneMagic.DOMAIN + ".transfiguration");
    }

    @Override
    public List<Widget> setupDisplay(Supplier<TransfigurationDisplay> recipeDisplaySupplier, Rectangle bounds) {
        Point startPoint = new Point((int) bounds.getCenterX() - 62, (int) bounds.getCenterY() - 27);
        List<Widget> widgets = new LinkedList<>(Collections.singletonList(new RecipeBaseWidget(bounds) {
            @Override
            public void render(int mouseX, int mouseY, float partialTicks) {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(DISPLAY_TEXTURE);
                blit(startPoint.x, startPoint.y, 0, 0, 124, 54);
                MinecraftClient.getInstance().getTextureManager().bindTexture(SOUL_METER_TEXTURE);

                float percent = ((float) recipeDisplaySupplier.get().getSoul() / (ArcaneMagicConstants.SOUL_METER_MAX));
                int stage = Math.round(percent * ArcaneMagicConstants.SOUL_METER_STAGES);
                int row = stage / 10;
                int col = stage % 10;

                blit(startPoint.x + 58, startPoint.y + 9,
                        36 * col, 36 + 36 * row,
                        36, 36,
                        360, 360);
            }
        }));
        List<List<ItemStack>> input = recipeDisplaySupplier.get().getInput();
        List<ItemSlotWidget> slots = Lists.newArrayList();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                slots.add(new ItemSlotWidget(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18, Lists.newArrayList(), true, true, true));
            }
        }
        for (int i = 0; i < input.size(); i++) {
            if (!input.get(i).isEmpty()) {
                slots.get(i).setItemList(input.get(i));
            }

        }
        widgets.addAll(slots);
        widgets.add(new ItemSlotWidget(startPoint.x + 103, startPoint.y + 19, recipeDisplaySupplier.get().getOutput(), false, true, true) {
            @Override
            protected String getItemCountOverlay(ItemStack currentStack) {
                if (currentStack.getAmount() == 1)
                    return "";
                if (currentStack.getAmount() < 1)
                    return "§c" + currentStack.getAmount();
                return currentStack.getAmount() + "";
            }
        });
        return widgets;
    }

    @Override
    public DisplaySettings getDisplaySettings() {
        return new DisplaySettings<TransfigurationDisplay>() {
            @Override
            public int getDisplayHeight(RecipeCategory category) {
                return 66;
            }

            @Override
            public int getDisplayWidth(RecipeCategory category, TransfigurationDisplay display) {
                return 158;
            }

            @Override
            public int getMaximumRecipePerPage(RecipeCategory category) {
                return 99;
            }
        };
    }
}
