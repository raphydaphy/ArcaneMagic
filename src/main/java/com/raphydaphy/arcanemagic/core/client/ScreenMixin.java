package com.raphydaphy.arcanemagic.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.raphydaphy.arcanemagic.item.SoulStorageItem;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement {
    private static Identifier TEXTURE = new Identifier(ArcaneMagic.DOMAIN, "textures/misc/soul_tooltip.png");
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    protected TextRenderer font;
    @Shadow
    protected ItemRenderer itemRenderer;

    @Shadow
    public abstract List<String> getTooltipFromItem(ItemStack itemStack_1);

    @Inject(at = @At("HEAD"), method = "renderTooltip(Lnet/minecraft/item/ItemStack;II)V", cancellable = true)
    private void drawStackTooltip(ItemStack stack, int int_1, int int_2, CallbackInfo info) {
        if (stack.getItem() instanceof SoulStorageItem && stack.hasTag()) {
            drawArcaneMagicSoulTooltip(stack, getTooltipFromItem(stack), int_1, int_2);
            info.cancel();
        }
    }

    private void drawArcaneMagicSoulTooltip(ItemStack stack, List<String> text, int x, int y) {
        if (!text.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            GuiLighting.disable();
            GlStateManager.disableLighting();
            GlStateManager.disableDepthTest();
            int int_3 = 0;

            for (String line : text) {
                int int_4 = this.font.getStringWidth(line);
                if (int_4 > int_3) {
                    int_3 = int_4;
                }
            }

            int drawX = x + 12;
            int drawY = y - 12;
            int height = 20; // Default 8
            if (text.size() > 1) {
                height += 2 + (text.size() - 1) * 10;
            }

            if (drawX + int_3 > this.width) {
                drawX -= 28 + int_3;
            }

            if (drawY + height + 6 > this.height) {
                drawY = this.height - height - 6;
            }

            this.blitOffset = 300;
            this.itemRenderer.zOffset = 300.0F;
            int int_9 = -267386864;
            this.fillGradient(drawX - 3, drawY - 4, drawX + int_3 + 3, drawY - 3, -267386864, -267386864);
            this.fillGradient(drawX - 3, drawY + height + 3, drawX + int_3 + 3, drawY + height + 4, -267386864, -267386864);
            this.fillGradient(drawX - 3, drawY - 3, drawX + int_3 + 3, drawY + height + 3, -267386864, -267386864);
            this.fillGradient(drawX - 4, drawY - 3, drawX - 3, drawY + height + 3, -267386864, -267386864);
            this.fillGradient(drawX + int_3 + 3, drawY - 3, drawX + int_3 + 4, drawY + height + 3, -267386864, -267386864);
            int int_10 = 1347420415;
            int int_11 = 1344798847;
            this.fillGradient(drawX - 3, drawY - 3 + 1, drawX - 3 + 1, drawY + height + 3 - 1, 1347420415, 1344798847);
            this.fillGradient(drawX + int_3 + 2, drawY - 3 + 1, drawX + int_3 + 3, drawY + height + 3 - 1, 1347420415, 1344798847);
            this.fillGradient(drawX - 3, drawY - 3, drawX + int_3 + 3, drawY - 3 + 1, 1347420415, 1347420415);
            this.fillGradient(drawX - 3, drawY + height + 2, drawX + int_3 + 3, drawY + height + 3, 1344798847, 1344798847);

            this.font.drawWithShadow(text.get(0), (float) drawX, (float) drawY, -1);
            drawY += 12;

            MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
            RenderUtils.texRect(drawX, drawY, 0, 0, 50, 8, 50, 8, 50, 12);

            int soul = 0;
            int max = stack.getItem() == ModRegistry.SOUL_PENDANT ? ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL : ((ScepterItem) stack.getItem()).maxSoul;
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                soul = tag.getInt(ArcaneMagicConstants.SOUL_KEY);
            }
            int stage = Math.round(((float) soul / (float) max) * 46);

            RenderUtils.texRect(drawX + 2, drawY + 2, 0, 8, stage, 4, stage, 4, 50, 12);
            drawY += 12;
            if (text.size() > 1) {
                for (int lineNumber = 1; lineNumber < text.size(); ++lineNumber) {
                    String string_2 = text.get(lineNumber);
                    this.font.drawWithShadow(string_2, (float) drawX, (float) drawY, -1);
                    drawY += 10;
                }
            }

            this.blitOffset = 0;
            this.itemRenderer.zOffset = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepthTest();
            GuiLighting.enable();
            GlStateManager.enableRescaleNormal();
        }
    }
}
