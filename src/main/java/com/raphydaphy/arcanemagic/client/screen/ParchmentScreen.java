package com.raphydaphy.arcanemagic.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.Parchment;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ParchmentScreen extends Screen {
    // x and y size of the parchment texture
    private static final int DIMENSIONS = 64;
    private static final int TEX_HEIGHT = 69;

    private static final int PROGRESS_BAR_LENGTH = 48;
    private static final int FULL_PROGRESS = PROGRESS_BAR_LENGTH - 2;

    private static final float SCALE = 3;
    private static final int SCALED_DIMENSIONS = (int) (DIMENSIONS * SCALE);

    private Identifier BACKGROUND = new Identifier(ArcaneMagic.DOMAIN, "textures/gui/parchment.png");

    private Parchment parchment;

    public ParchmentScreen(ItemStack stack, Parchment parchment) {
        super(new TranslatableComponent("item.arcanemagic.written_parchment"));
        this.parchment = parchment;
        parchment.initScreen(stack);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        MinecraftClient client = MinecraftClient.getInstance();
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();

        client.getTextureManager().bindTexture(BACKGROUND);

        // The start x and y coords of the notebook on the screen
        int screenCenterX = (client.window.getScaledWidth() / 2) - (SCALED_DIMENSIONS / 2);
        int screenCenterY = (client.window.getScaledHeight() / 2) - (SCALED_DIMENSIONS / 2);

        RenderUtils.texRect(screenCenterX, screenCenterY, 0, 0, DIMENSIONS, DIMENSIONS, SCALED_DIMENSIONS, SCALED_DIMENSIONS, DIMENSIONS, TEX_HEIGHT);

        drawCenteredUnlocalizedText(parchment.getText(), parchment.verticallyCenteredText(), screenCenterY + (parchment.verticallyCenteredText() ? SCALED_DIMENSIONS / 2f : 4 * SCALE) + parchment.getVerticalTextOffset() * SCALE);

        int percent = (int) (parchment.getProgressPercent() * FULL_PROGRESS);

        if (parchment.getRecipe(MinecraftClient.getInstance().world.getRecipeManager()) != null) {
            RenderUtils.drawRecipe(this, (x, y) ->
            {
                client.getTextureManager().bindTexture(BACKGROUND);
                RenderUtils.texRect(x, y, 48, 64, 7, 5, 22, 15, DIMENSIONS, TEX_HEIGHT);
            }, parchment.getRecipe(MinecraftClient.getInstance().world.getRecipeManager()), screenCenterX + 31, (int) (screenCenterY + 37 * SCALE + parchment.getVerticalFeatureOffset() * SCALE), mouseX, mouseY);
        }

        if (parchment.showProgressBar()) {
            drawProgressBar(percent, screenCenterX, screenCenterY, parchment.getVerticalFeatureOffset());
        }

        if (parchment.getRequiredItems() != null && !parchment.getRequiredItems().isEmpty()) {
            RenderUtils.drawRequiredItems(this, parchment.getRequiredItems(), screenCenterX + SCALED_DIMENSIONS / 2 - (parchment.getRequiredItems().size() * 35) / 2, (int) (screenCenterY + 51 * SCALE + parchment.getVerticalFeatureOffset() * SCALE), mouseX, mouseY);
        }

        GlStateManager.popMatrix();
    }

    private void drawCenteredUnlocalizedText(String unlocalizedText, boolean verticallyCentered, float top) {
        RenderUtils.drawSplitString(MinecraftClient.getInstance().textRenderer, I18n.translate(unlocalizedText), MinecraftClient.getInstance().window.getScaledWidth() / 2f, (int) (top), 160, 0, verticallyCentered, true);
    }

    private void drawProgressBar(int progress, int screenCenterX, int screenCenterY, int verticalOffset) {
        GlStateManager.pushMatrix();

        MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);

        int y = (int) (screenCenterY + 54 * SCALE + verticalOffset * SCALE);

        RenderUtils.texRect(
                (int) (screenCenterX + 8 * SCALE), y, 0, DIMENSIONS,
                PROGRESS_BAR_LENGTH, 5, (int) (PROGRESS_BAR_LENGTH * SCALE), (int) ((5) * SCALE), DIMENSIONS, TEX_HEIGHT);

        if (progress > 0) {
            DrawableHelper.fill(
                    (int) (screenCenterX + 9 * SCALE),
                    (int) (y + SCALE),
                    (int) (screenCenterX + 9 * SCALE + progress * SCALE),
                    (int) (y + SCALE + 3 * SCALE), 0xff926527);
        }

        GlStateManager.popMatrix();

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
