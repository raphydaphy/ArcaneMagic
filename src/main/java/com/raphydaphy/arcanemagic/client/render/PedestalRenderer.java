package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.tileentity.TileEntityPedestal;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

// TODO: sideonly client
public class PedestalRenderer extends TileEntityRenderer<TileEntityPedestal>
{
    @Override
    public void func_199341_a(TileEntityPedestal pedestal, double x, double y, double z, float partialTicks, int destroyStage)
    {
        ItemStack stack = pedestal.getStackInSlot(TileEntityPedestal.SLOT);
        if (!stack.isEmpty())
        {
            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();

            GlStateManager.disableRescaleNormal();
            GlStateManager.enableLighting();

            GlStateManager.translate(x + .5, y + .7, z + .5);

            float time = ArcaneMagicUtils.lerp(pedestal.getPrevTime(), pedestal.getTime(), partialTicks);

            GlStateManager.rotate(time * 3, 0, 1, 0);
            GlStateManager.translate(0, Math.sin((Math.PI / 180) * (time * 4)) / 15 + 0.15, 0);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        }
    }
}
