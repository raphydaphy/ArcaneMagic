package com.raphydaphy.arcanemagic.client.render;

import java.awt.Color;

import com.raphydaphy.arcanemagic.common.block.BlockInfernalSmelter;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityInfernalSmelter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
public class InfernalSmelterTESR extends TileEntitySpecialRenderer<TileEntityInfernalSmelter>
{

	@Override
	public void render(TileEntityInfernalSmelter te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();
		te.frameAge++;

		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		Vec3d ore = new Vec3d(0, 0.25, 0);
		switch (te.getWorld().getBlockState(te.getPos()).getValue(BlockInfernalSmelter.FACING).getHorizontalIndex())
		{
		case 0:
			ore = ore.addVector(0.5, 0, 0.135);
			break;
		case 1:
			ore = ore.addVector(0.85, 0, 0.5);
			break;
		case 2:
			ore = ore.addVector(0.5, 0, 0.85);
			break;
		case 3:
			ore = ore.addVector(0.135, 0, 0.5);
			break;
		}
		GlStateManager.translate(ore.x, ore.y, ore.z);
		renderItem(te, cap.getStackInSlot(TileEntityInfernalSmelter.ORE), te.frameAge, false);

		GlStateManager.popMatrix();
	}

	private void renderItem(TileEntityInfernalSmelter te, ItemStack stack, int offset, boolean rainbowBeams)
	{
		if (!stack.isEmpty())
		{

			Color c = Color.RED;

			if (rainbowBeams)
			{
				float frequency = 0.1f;
				double r = Math.sin(frequency * (offset)) * 127 + 128;
				double g = Math.sin(frequency * (offset) + 2) * 127 + 128;
				double b = Math.sin(frequency * (offset) + 4) * 127 + 128;

				c = new Color((int) r, (int) g, (int) b);
			}
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();

			GlStateManager.scale(.2f, .2f, .2f);
			GlStateManager.rotate(te.frameAge / 2, 0, 1, 0);
			//GlStateManager.translate(0, Math.sin((Math.PI / 180) * (frame)) / 3.2 + 0.1, 0);
			if (te.frameAge < 0 || te.frameAge == Integer.MAX_VALUE)
				te.frameAge = 0;
			if (rainbowBeams)
			{
				GLHelper.renderFancyBeams(0, 0, 0, c, Minecraft.getMinecraft().world.getSeed(), offset, 32, 1.4f, 30,
						10);
			}
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}
}
