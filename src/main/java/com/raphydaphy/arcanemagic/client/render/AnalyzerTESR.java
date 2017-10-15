package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnalyzer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
public class AnalyzerTESR extends TileEntitySpecialRenderer<TileEntityAnalyzer>
{
	@Override
	public void render(TileEntityAnalyzer te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();
		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		GlStateManager.translate(0, 0, 0);

		renderItem(cap.getStackInSlot(0), te);

		GlStateManager.popMatrix();
	}

	private void renderItem(ItemStack stack, TileEntityAnalyzer te)
	{
		if (stack != null && !stack.isEmpty())
		{
			GlStateManager.pushMatrix();

			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.translate(0.5, .54, 0.5);
			GlStateManager.scale(.18f, .18f, .18f);

			if (!(stack.getItem() instanceof ItemBlock))
			{
				GlStateManager.scale(1.2f, 1.2f, 1.2f);
				GlStateManager.rotate(90, 1, 0, 0);

				GlStateManager.rotate(-te.getAge() * 1.5f, 0, 0, 1);
			} else
			{
				GlStateManager.rotate(te.getAge() * 1.5f, 0, 1, 0);
			}

			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

			GlStateManager.popMatrix();
		}
	}
}
