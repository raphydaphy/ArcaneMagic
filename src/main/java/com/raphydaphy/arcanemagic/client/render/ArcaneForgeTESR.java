package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneForge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ArcaneForgeTESR extends TileEntitySpecialRenderer<TileEntityArcaneForge> {
	@Override
	public void render(TileEntityArcaneForge te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();

		renderItem(te);

		GlStateManager.popMatrix();
	}

	private void renderItem(TileEntityArcaneForge te) {
		ItemStack stack = te.getStack();
		if (!stack.isEmpty()) {
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();
			GlStateManager.translate(.5, 1.01, .5);
			GlStateManager.scale(.5f, .5f, .5f);

			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.rotate(45, 0, 0, 1);
			if (te.getFrameAge() < 0 || te.getFrameAge() == Integer.MAX_VALUE) {
				te.setFrameAge(0);
			}
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

			GlStateManager.rotate(-45, 0, 0, 1);
			GlStateManager.translate(0, -0.22, -0.035);
			GlStateManager.scale(0.1, 0.1, 0.2);

			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.DIAMOND),
					ItemCameraTransforms.TransformType.NONE);

			GlStateManager.translate(0, -3.55, 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.EMERALD),
					ItemCameraTransforms.TransformType.NONE);

			GlStateManager.popMatrix();

		}
	}
}
