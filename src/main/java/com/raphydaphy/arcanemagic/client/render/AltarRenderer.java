package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

// TODO: sideonly client
public class AltarRenderer<T extends TileEntityAltar> extends TileEntityRenderer<T>
{
    @Override
    public void func_199341_a(TileEntityAltar altar, double x, double y, double z, float partialTicks, int destroyStage)
    {

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.disableRescaleNormal();

        ItemStack stack = altar.getStackInSlot(TileEntityAltar.SLOT);
        if (!stack.isEmpty())
        {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();

            GlStateManager.translate(x + .5, y + .7, z + .5);

            float time = ArcaneMagicUtils.lerp(altar.getPrevTime(), altar.getTime(), partialTicks);

            GlStateManager.rotate(time * 3, 0, 1, 0);
            GlStateManager.translate(0, Math.sin((Math.PI / 180) * (time * 4)) / 15 + 0.2, 0);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}
