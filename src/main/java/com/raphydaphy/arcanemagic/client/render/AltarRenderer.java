package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.init.Blocks;
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

        ItemStack stack = altar.getStackInSlot(0);
        if (!stack.isEmpty())
        {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();

            GlStateManager.translate(x + .5, y + .9, z + .5);
            GlStateManager.scale(.4f, .4f, .4f);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}
