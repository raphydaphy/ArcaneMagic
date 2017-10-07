package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceConcentrator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EssenceConcentratorTESR extends TileEntitySpecialRenderer<TileEntityEssenceConcentrator>
{
	@Override
	public void render(TileEntityEssenceConcentrator te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();
		
		renderItem(te);
		
		GlStateManager.popMatrix();
	}

	private void renderItem(TileEntityEssenceConcentrator te)
	{
		ItemStack stack = te.getStack();
		if (!stack.isEmpty())
		{
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();
			GlStateManager.translate(.5, .6, .5);
			GlStateManager.scale(.4f, .4f, .4f);
			GlStateManager.rotate(Minecraft.getMinecraft().world.getWorldInfo().getWorldTime() * 5, 0, 1, 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}
}
