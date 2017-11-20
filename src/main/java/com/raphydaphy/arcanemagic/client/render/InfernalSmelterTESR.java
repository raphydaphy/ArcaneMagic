package com.raphydaphy.arcanemagic.client.render;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.common.tileentity.TileEntityInfernalSmelter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
public class InfernalSmelterTESR extends TileEntitySpecialRenderer<TileEntityInfernalSmelter>
{
	private int frame = 0;
	@Override
	public void render(TileEntityInfernalSmelter te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();
		frame++;

		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		GlStateManager.translate(.5, 0.935, .5);
		renderItem(cap.getStackInSlot(TileEntityInfernalSmelter.ORE), frame);

		List<ItemStack> crystalStacks = new ArrayList<>();
		
		for (int crystalSlot = TileEntityInfernalSmelter.ORE + 1; crystalSlot < TileEntityInfernalSmelter.SIZE; crystalSlot++)
		{
			if (!cap.getStackInSlot(crystalSlot).isEmpty())
			{
				crystalStacks.add(cap.getStackInSlot(crystalSlot).copy());
			}
		}
		
		int accuracy = 60;
		double radius = 1;
		GlStateManager.pushMatrix();
		GlStateManager.rotate(-frame / 2f, 0, 1, 0);
		for (int seg = 0; seg < (360 / accuracy); seg++)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate((Math.cos(Math.toRadians(seg * accuracy)) * radius),
					0.2, (Math.sin(Math.toRadians(seg * accuracy)) * radius));
			renderItem(cap.getStackInSlot(seg + 1), -frame + (int)(Math.abs(Math.sin(seg)) * 3));
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
	
	private void renderItem(ItemStack stack, int offset) {
		if (!stack.isEmpty()) {
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();
			
			GlStateManager.scale(.2f, .2f, .2f);
			GlStateManager.rotate(frame, 0, 1, 0);
			GlStateManager.translate(0, Math.sin((Math.PI / 180) * (frame)) / 3.2 + 0.1, 0);
			if(frame < 0 || frame == Integer.MAX_VALUE) frame = 0;
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}
}
