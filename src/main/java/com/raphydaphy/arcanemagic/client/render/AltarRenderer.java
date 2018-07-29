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
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.EnumFacing;

// TODO: sideonly client
public class AltarRenderer<T extends TileEntityAltar> extends TileEntityRenderer<T>
{
    @Override
    public void func_199341_a(T p_199341_1_, double p_199341_2_, double p_199341_4_, double p_199341_6_, float p_199341_8_, int p_199341_9_)
    {
        System.out.println("hello please work");
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        IBlockState lvt_10_1_ = p_199341_1_.hasWorld() ? p_199341_1_.getBlockState() : (IBlockState)Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.SOUTH);
        ChestType lvt_11_1_ = lvt_10_1_.hasProperty(BlockChest.field_196314_b) ? (ChestType)lvt_10_1_.getValue(BlockChest.field_196314_b) : ChestType.SINGLE;
        if (lvt_11_1_ != ChestType.LEFT) {
            boolean lvt_12_1_ = lvt_11_1_ != ChestType.SINGLE;
            if (p_199341_9_ >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scale(lvt_12_1_ ? 8.0F : 4.0F, 4.0F, 1.0F);
                GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                GlStateManager.matrixMode(5888);
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translate((float)p_199341_2_, (float)p_199341_4_ + 1.0F, (float)p_199341_6_ + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            float lvt_14_1_ = EnumFacing.SOUTH.getHorizontalAngle();
            if ((double)Math.abs(lvt_14_1_) > 1.0E-5D) {
                GlStateManager.translate(0.5F, 0.5F, 0.5F);
                GlStateManager.rotate(lvt_14_1_, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (p_199341_9_ >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }

        }
    }
    /*
    @Override
    public void func_199341_a(TileEntityAltar altar, double x, double y, double z, float partialTicks, int destroyStage)
    {
        System.out.println("render render render");

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
    }*/
}
